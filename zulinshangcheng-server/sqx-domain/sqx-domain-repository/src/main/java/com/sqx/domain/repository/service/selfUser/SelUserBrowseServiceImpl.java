package com.sqx.domain.repository.service.selfUser;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfUserBrowse;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfUser.SelfUserBrowseJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelUserBrowseServiceImpl implements SelfUserBrowseService {
    @Autowired
    private SelfUserBrowseJpaRepository jpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size, Long userId, String createTime) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Specification<SelfUserBrowse> queryCondition = new Specification<SelfUserBrowse>() {
            @Override
            public Predicate toPredicate(Root<SelfUserBrowse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (userId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (StringUtils.isNotEmpty(createTime)) {
                    predicateList.add(criteriaBuilder.like(root.get("createTime"), "%"+createTime+"%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfUserBrowse> all = jpaRepository.findAll(queryCondition, pageable);
        for (SelfUserBrowse s : all.getContent()) {
            s.setGoods(goodsJpaRepository.findById(s.getGoodsId()).orElse(null));
        }
        //浏览痕迹限制一百条
        List<SelfUserBrowse> list = all.getContent();
        List<SelfUserBrowse> deleteList = new ArrayList<>();
        if (list.size() > 100){
            for (int i = 100; i < list.size(); i++) {
                deleteList.add(list.get(i));
            }
        }
        jpaRepository.deleteAll(deleteList);
        return ResultUtil.success(all);
    }

    @Override
    public Result saveBody(SelfUserBrowse entity) {
        //查重
        SelfUserBrowse s = jpaRepository.findByGoodsIdAndUserId(entity.getGoodsId(), entity.getUserId());
        if (s != null && s.getId() != null){
            s.setCreateTime(DateUtils.createTime()); //更新时间
            return ResultUtil.success(jpaRepository.save(s));
        }
        entity.setCreateTime(DateUtils.createTime()); //创建时间
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfUserBrowse entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

}

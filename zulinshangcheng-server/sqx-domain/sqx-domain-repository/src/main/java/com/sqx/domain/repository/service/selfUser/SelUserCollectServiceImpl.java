package com.sqx.domain.repository.service.selfUser;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfUserCollect;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfUser.SelfUserCollectJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelUserCollectServiceImpl implements SelfUserCollectService {
    @Autowired
    private SelfUserCollectJpaRepository jpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;


    @Override
    public Result findAll(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<SelfUserCollect> queryCondition = new Specification<SelfUserCollect>() {
            @Override
            public Predicate toPredicate(Root<SelfUserCollect> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (userId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfUserCollect> all = jpaRepository.findAll(queryCondition, pageable);
        for (SelfUserCollect s : all.getContent()) {
            s.setGoods(goodsJpaRepository.findById(s.getGoodsId()).orElse(null));
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result saveBody(SelfUserCollect entity) {
        entity.setCreateTime(DateUtils.createTime()); //创建时间
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfUserCollect entity) {
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

    @Override
    public Result check(Long goodsId, Long userId) {
        SelfUserCollect s = jpaRepository.findByGoodsIdAndUserId(goodsId, userId);
        if (s != null && s.getId() != null){
            Result result = new Result();
            result.setData(false);
            result.setMsg(s.getId().toString());
            result.setStatus(0);
            return result;
        }
        return ResultUtil.success(true);
    }
}

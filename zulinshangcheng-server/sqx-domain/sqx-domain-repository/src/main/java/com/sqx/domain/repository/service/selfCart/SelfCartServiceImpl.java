package com.sqx.domain.repository.service.selfCart;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.SelfCart;
import com.sqx.domain.repository.mapper.selfCart.SelfCartJpaRepository;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsSku.SelfGoodsSkuJpaRepository;
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
public class SelfCartServiceImpl implements SlefCartService {
    @Autowired
    private SelfCartJpaRepository jpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;
    @Autowired
    private SelfGoodsSkuJpaRepository skuJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size, Long userId) {
        //按照时间排序
        Pageable pageable = PageRequest.of(page, size);
        //构造自定义查询条件
        Specification<SelfCart> queryCondition = new Specification<SelfCart>() {
            @Override
            public Predicate toPredicate(Root<SelfCart> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (userId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfCart> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfCart> content = all.getContent();
        //设置商品信息、sku信息
        for (SelfCart s : content) {
            s.setGoods(goodsJpaRepository.findById(s.getGoodsId()).orElse(null));
            s.setSku(skuJpaRepository.findById(s.getSkuId()).orElse(null));
            //无sku时，sku发生变化，商品状态为3
            if (s.getSku() == null){
                s.getGoods().setStatus(3); //sku不存在
            }
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result saveBody(SelfCart entity) {
        SelfCart cart = jpaRepository.findByUserIdAndGoodsIdAndSkuId(entity.getUserId(), entity.getGoodsId(), entity.getSkuId());
        if (cart != null && cart.getId() != null){
            entity.setId(cart.getId());
            entity.setNumber(cart.getNumber()+1); //购物车该商品数量+1
        }
        SelfCart save = jpaRepository.save(entity);
        return ResultUtil.success(save);
    }

    @Override
    public Result updateBody(SelfCart entity) {
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
    public Result deleteAll(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            jpaRepository.deleteById(Long.valueOf(id));
        }
        return ResultUtil.success();
    }

    @Override
    public Result changeNumber(Long id, Integer number) {
        SelfCart s = jpaRepository.getOne(id);
        s.setNumber(number);
        return ResultUtil.success(jpaRepository.save(s));
    }
}

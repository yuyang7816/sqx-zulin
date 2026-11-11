package com.sqx.domain.repository.service.selfCoupon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.SelfCoupon;
import com.sqx.domain.repository.entity.sys.SelfCouponIssue;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.selfCoupon.*;
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
public class SelfCouponUserServiceImpl implements SelfCouponUserService {
    @Autowired
    private SelfCouponUserJpaRepository jpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;
    @Autowired
    private SelfCouponMapper selfCouponMapper;
    @Autowired
    private SelfCouponIssueMapper issueMapper;
    @Autowired
    private SelfCouponUserMapper selfCouponUserMapper;
    @Autowired
    private UserDao userDao;

    @Override
    public Result findAll(Integer page, Integer size, String couponName) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(couponName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("couponName"), couponName));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfCouponUser> list = all.getContent();
        for (SelfCouponUser s : list) {
            String failureTime = s.getFailureTime();
            if (DateUtils.checkTime(failureTime)) {
                s.setStatus(3); //设置为已过期
            }
            UserEntity userEntity = userDao.selectById(s.getUserId());
            if (userEntity != null) {
                s.setNickName(userEntity.getUserName());
            }
        }
        jpaRepository.saveAll(list);
        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券列表
     */
    @Override
    public Result userList(Integer page, Integer size, Long userId, String goodsId, Integer type, Integer status) {
        IPage<SelfCouponUser> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<SelfCouponUser> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotEmpty(goodsId)) {
            wrapper.like("goods_ids", "%" + goodsId + "%");
        }
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }else {
            wrapper.eq("status", 1);
        }
        IPage<SelfCouponUser> all = selfCouponUserMapper.selectPage(pages, wrapper);
        List<SelfCouponUser> list = all.getRecords();
        //用户优惠券可用性
        if (!list.isEmpty()) {
            List<SelfCouponUser> updateList = new ArrayList<>();
            for (SelfCouponUser s : list) {
                String failureTime = s.getFailureTime();
                if (DateUtils.checkTime(failureTime)) {
                    s.setStatus(3); //设置为已过期
                    updateList.add(s); //添加到要修改的列表
                }
                SelfCoupon coupon = new SelfCoupon();
                coupon.setCouponName(s.getCouponName());
                coupon.setGoodsImages(s.getGoodsImages());
                coupon.setLessMoney(s.getLessMoney());
                coupon.setMinMoney(s.getMinMoney());
                s.setCoupon(coupon);

            }
            jpaRepository.saveAll(updateList);
        }
        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券使用列表
     */
    @Override
    public Result useList(Long userId, String goodsId) {
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                if (StringUtils.isNotEmpty(goodsId)) {
                    predicateList.add(criteriaBuilder.like(root.get("goodsIds"), "%" + goodsId + "%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //未使用
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<SelfCouponUser> list = jpaRepository.findAll(queryCondition);
        List<SelfCouponUser> type1List = jpaRepository.findAllByUSerIdAndType1(userId); //通用券列表
        list.addAll(type1List);
        //用户优惠券可用性
        if (list.size() > 0) {
            List<SelfCouponUser> updateList = new ArrayList<>();
            for (SelfCouponUser s : list) {
                String failureTime = s.getFailureTime();
                if (DateUtils.checkTime(failureTime)) {
                    s.setStatus(3); //设置为已过期
                    updateList.add(s); //添加到要修改的列表
                    list.remove(s); //移除本条数据
                }
                /*for (SelfCoupon c : couponList) {
                    if (c.getCouponId().equals(s.getCouponId())){
                        s.setCoupon(c);
                    }
                }*/
                SelfCoupon coupon = new SelfCoupon();
                coupon.setCouponName(s.getCouponName());
                coupon.setGoodsImages(s.getGoodsImages());
                coupon.setLessMoney(s.getLessMoney());
                coupon.setMinMoney(s.getMinMoney());
                s.setCoupon(coupon);
            }
            jpaRepository.saveAll(updateList);
        }
        return ResultUtil.success(list);
    }

    /**
     * 领取优惠券
     *
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(SelfCouponUser entity) {
        SelfCouponIssue couponIssue = couponIssueJpaRepository.findById(entity.getCouponIssueId()).orElse(null);
        if (couponIssue != null) {
            if (couponIssue.getRemainNumber() < 1) {
                return ResultUtil.error(-1, "优惠券领取完了");
            }
            String time = DateUtils.createTime();
            entity.setCreateTime(time); //创建时间
            entity.setStatus(1); //状态（1未使用 2已使用 3已过期）
            //失效时间
            SelfCoupon coupon = couponJpaRepository.findById(entity.getCouponId()).orElse(null);
            if (coupon != null) {
                Integer days = coupon.getValidDay();
                entity.setFailureTime(DateUtils.addDays(time, days));
                entity.setGoodsIds(coupon.getGoodsIds()); //商品id
                entity.setGoodsImages(coupon.getGoodsImages());
                entity.setCouponName(coupon.getCouponName());
                entity.setLessMoney(coupon.getLessMoney());
                entity.setMinMoney(coupon.getMinMoney());
                entity.setType(coupon.getType());
                entity.setUserId(entity.getUserId());
                //优惠券剩余数量-1
                couponIssue.setRemainNumber(couponIssue.getRemainNumber() - 1);
                jpaRepository.save(entity);
                return ResultUtil.success();
            }
        }
        return ResultUtil.error(-1, "优惠券领取失败");
    }


    @Override
    public Result updateBody(SelfCouponUser entity) {
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

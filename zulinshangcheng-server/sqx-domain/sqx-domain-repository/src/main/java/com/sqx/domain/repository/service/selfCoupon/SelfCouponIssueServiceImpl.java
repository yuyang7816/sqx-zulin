package com.sqx.domain.repository.service.selfCoupon;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfCoupon;
import com.sqx.domain.repository.entity.sys.SelfCouponIssue;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponIssueJpaRepository;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponJpaRepository;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SelfCouponIssueServiceImpl implements SelfCouponIssueService {
    @Autowired
    private SelfCouponIssueJpaRepository jpaRepository;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResultUtil.success(jpaRepository.findAll(pageable));
    }

    /**
     * 用户可领取的优惠券列表
     * @param goodsId
     * @param userId
     * @return
     */
    @Override
    public Result useList(String goodsId, Long userId) {
        List<SelfCouponIssue> list = jpaRepository.selectSelfCouponIssueList(goodsId==null?"":goodsId, userId);
        for (SelfCouponIssue c : list) {
            SelfCoupon coupon=new SelfCoupon();
            coupon.setCouponName(c.getCouponName());
            coupon.setGoodsImages(c.getGoodsImages());
            coupon.setLessMoney(c.getLessMoney());
            coupon.setMinMoney(c.getMinMoney());
            c.setCoupon(coupon);
            c.setGetStatus(1);
        }
        return ResultUtil.success(list);
    }

    @Override
    public Result saveBody(SelfCouponIssue entity) {
        SelfCoupon c = couponJpaRepository.findById(entity.getCouponId()).orElse(null);
        entity.setCouponName(c.getCouponName());
        entity.setGoodsImages(c.getGoodsImages());
        entity.setLessMoney(c.getLessMoney());
        entity.setMinMoney(c.getMinMoney());
        entity.setCreateTime(DateUtils.format(new Date()));
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCouponIssue entity) {
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

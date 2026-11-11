package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbCouponUser;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.sys.TbCoupon;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
public interface TbCouponUserService extends IService<TbCouponUser> {

    Result businessCallback(Integer payType, Long userId, Long couponId, Integer buyNum, BigDecimal totalPrice);
    IPage<TbCouponUser> getMyCouponList(Integer page, Integer limit, TbCouponUser couponUser);


    Result receiveActivity(Long userId, Long couponId);


    void giveCoupon(TbCoupon tbCoupon, Long userId);

    Result giveUserCoupon(String userIds, Long couponId, Integer num);

    IPage<TbCouponUser> getMyOrderCouponList(Integer page, Integer limit, TbOrders tbspOrder);
}

package com.sqx.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbCouponUser;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.service.sys.TbCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@Api(value = "用户端-优惠券", tags = {"用户端-优惠券"})
@RequestMapping(value = "/app/couponUser/")
public class AppTbCouponUserController{

    @Autowired
    private TbCouponUserService couponUserService;


    @Login
    @ApiOperation("查看我的优惠券列表")
    @GetMapping(value = "getMyCouponList")
    public Result getMyCouponList(@RequestAttribute Long userId, Integer page, Integer limit, TbCouponUser couponUser) {
        couponUser.setUserId(userId);
        return Result.success(couponUserService.getMyCouponList(page, limit, couponUser));
    }

    @Login
    @ApiOperation("获取订单可用优惠券")
    @GetMapping(value = "getMyOrderCouponList")
    public Result getMyOrderCouponList(@RequestAttribute Long userId, Integer page, Integer limit, TbOrders tbOrders) {
        tbOrders.setUserId(userId);
        return Result.success(couponUserService.getMyOrderCouponList(page, limit, tbOrders));
    }

    @Login
    @ApiOperation("领取活动优惠券")
    @GetMapping("receiveActivity")
    public Result receiveActivity(@RequestAttribute("userId") Long userId, Long couponId) {
        return couponUserService.receiveActivity(userId, couponId);
    }


    /**
     * 检测使优惠券过期
     */
    @Scheduled(fixedRate  = 2000, zone = "Asia/Shanghai")
    public void couponOverdue() {
        List<TbCouponUser> couponUserList = couponUserService.list(new QueryWrapper<TbCouponUser>().eq("status", 0).isNotNull("expiration_time"));
        for (TbCouponUser tbCouponUser : couponUserList) {
            if (tbCouponUser.getExpirationTime().before(new Date())) {
                tbCouponUser.setStatus(2);
                couponUserService.updateById(tbCouponUser);
            }
        }

    }
}
package com.sqx.app.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.TbCoupon;
import com.sqx.domain.repository.service.sys.TbCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户端-优惠券", tags = {"用户端-优惠券"})
@RequestMapping(value = "/app/coupon/")
public class AppTbCouponController {

    @Autowired
    private TbCouponService tbCouponService;

    @ApiOperation("获取优惠券列表")
    @GetMapping(value = "getCouponPageList")
    public Result getCouponPageList(Integer page, Integer limit, TbCoupon tbCoupon) {
        tbCoupon.setIsEnable(1);
        tbCoupon.setDeleteFlag(0);
        return Result.success(tbCouponService.getCouponPageList(page, limit, tbCoupon));
    }



}
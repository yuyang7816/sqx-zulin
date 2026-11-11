package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.TbCoupon;
import com.sqx.domain.repository.service.sys.TbCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "管理端-优惠券", tags = {"管理端-优惠券"})
@RequestMapping(value = "/admin/coupon/")
public class AdminTbCouponController{

    @Autowired
    private TbCouponService tbCouponService;

    @ApiOperation("发布优惠券")
    @PostMapping(value = "addCoupon")
    public Result addCoupon(TbCoupon tbCoupon) {

        return tbCouponService.addCoupon(tbCoupon);
    }

    @ApiOperation("获取优惠券列表")
    @GetMapping(value = "getCouponPageList")
    public Result getCouponPageList(Integer page, Integer limit, TbCoupon tbCoupon) {
        return Result.success(tbCouponService.getCouponPageList(page, limit, tbCoupon));
    }

    @ApiOperation("删除发布的优惠券")
    @PostMapping(value = "deleteCoupon")
    public Result deleteCoupon(Long couponId) {
        return tbCouponService.deleteCoupon(couponId);
    }

    @ApiOperation("修改优惠券信息")
    @PostMapping(value = "updateCoupon")
    public Result updateCoupon(TbCoupon tbCoupon) {

        return tbCouponService.updateCoupon(tbCoupon);
    }
}
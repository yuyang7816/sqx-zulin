package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbCouponUser;
import com.sqx.domain.repository.service.sys.TbCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@Api(value = "管理端-优惠券", tags = {"管理端-用户优惠券"})
@RequestMapping(value = "/admin/couponUser/")
public class AdminTbCouponUserController{

    @Autowired
    private TbCouponUserService couponUserService;


    @ApiOperation("查看指定用户优惠券列表")
    @GetMapping(value = "getMyCouponList")
    public Result getMyCouponList(Integer page, Integer limit, TbCouponUser couponUser) {
        return Result.success(couponUserService.getMyCouponList( page, limit, couponUser));
    }

    @ApiOperation("赠送用户优惠券")
    @PostMapping(value = "giveUserCoupon")
    public Result giveUserCoupon(String userIds,Long couponId,Integer num){
        return couponUserService.giveUserCoupon(userIds,couponId,num);
    }

}
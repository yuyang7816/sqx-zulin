package com.sqx.system.selfCoupon;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;
import com.sqx.domain.repository.service.selfCoupon.SelfCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券领取",tags={"自营商城优惠券领取"})
@RequestMapping(value = "/selfCouponUser")
public class SelfCouponUserController {
    @Autowired
    private SelfCouponUserService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size, String couponName) {
        return service.findAll(page, size, couponName);
    }

    @Login
    @GetMapping("/userList")
    @ApiOperation("用户端用户优惠券列表")
    public Result userList(Integer page, Integer size,@RequestAttribute Long userId,@RequestParam(required = false) String goodsId,
                           @RequestParam(required = false)Integer type,Integer status) {
        return service.userList(page, size, userId, goodsId, type,status);
    }
    @Login
    @GetMapping("/useList")
    @ApiOperation("优惠券使用列表")
    public Result useList(@RequestAttribute Long userId,@RequestParam(required = false) String goodsId) {
        return service.useList(userId, goodsId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }
    @Login
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestAttribute Long userId,@RequestBody SelfCouponUser entity) {
        entity.setUserId(userId);
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponUser entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

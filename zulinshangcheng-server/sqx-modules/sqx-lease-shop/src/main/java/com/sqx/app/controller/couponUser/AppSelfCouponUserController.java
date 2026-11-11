package com.sqx.app.controller.couponUser;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;
import com.sqx.domain.repository.service.selfCoupon.SelfCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/12/24 13:53
 */
@RestController
@RequestMapping("/app/selfCouponUser/")
@Api(value="商城优惠券领取---用户端",tags={"商城优惠券领取---用户端"})
public class AppSelfCouponUserController {

    @Autowired
    private SelfCouponUserService service;

    @Login
    @GetMapping("userList")
    @ApiOperation("用户端用户优惠券列表")
    public Result userList(Integer page, Integer size, @RequestAttribute Long userId, @RequestParam(required = false) String goodsId,
                           @RequestParam(required = false)Integer type, Integer status) {
        return service.userList(page, size, userId, goodsId, type,status);
    }
    @Login
    @GetMapping("useList")
    @ApiOperation("优惠券使用列表")
    public Result useList(@RequestAttribute Long userId,@RequestParam(required = false) String goodsId) {
        return service.useList(userId, goodsId);
    }

    @GetMapping("find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @Login
    @PostMapping("save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponUser entity,@RequestAttribute Long userId) {
        entity.setUserId(userId);
        return service.saveBody(entity);
    }

}

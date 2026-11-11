package com.sqx.app.controller.couponIssue;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponIssue;
import com.sqx.domain.repository.service.selfCoupon.SelfCouponIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 17:58
 */
@RestController
@Api(value="商城优惠券发布---用户端",tags={"商城优惠券发布---用户端"})
@RequestMapping(value = "/app/selfCouponIssue")
public class AppCouponIssueController {

    @Autowired
    private SelfCouponIssueService service;

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponIssue entity) {
        return service.saveBody(entity);
    }

    @Login
    @GetMapping("/useList")
    @ApiOperation("用户端领券列表")
    public Result useList( @RequestParam(required = false) String goodsId,@RequestAttribute @RequestParam(required = false) Long userId) {
        return service.useList(goodsId, userId);
    }

}

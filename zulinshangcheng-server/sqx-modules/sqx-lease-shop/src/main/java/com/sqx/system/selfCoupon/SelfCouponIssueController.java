package com.sqx.system.selfCoupon;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponIssue;
import com.sqx.domain.repository.service.selfCoupon.SelfCouponIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券发布",tags={"自营商城优惠券发布"})
@RequestMapping(value = "/selfCouponIssue")
public class SelfCouponIssueController {
    @Autowired
    private SelfCouponIssueService service;

    @GetMapping("/list")
    @ApiOperation("后台列表")
    public Result findAll(Integer page, Integer size) {
        return service.findAll(page, size);
    }

    @Login
    @GetMapping("/useList")
    @ApiOperation("用户端领券列表")
    public Result useList( @RequestParam(required = false) String goodsId,@RequestAttribute @RequestParam(required = false) Long userId) {
        return service.useList(goodsId, userId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponIssue entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponIssue entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

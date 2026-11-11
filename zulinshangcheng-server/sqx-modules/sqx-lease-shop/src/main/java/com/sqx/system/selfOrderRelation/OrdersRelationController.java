package com.sqx.system.selfOrderRelation;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.service.selfOrderRelation.OrdersRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="自营商城订单分销",tags={"自营商城订单分销管理"})
@RequestMapping(value = "/ordersRelation")
public class OrdersRelationController {
    @Autowired
    private OrdersRelationService service;

    @GetMapping("/list")
    @ApiOperation("订单分销列表")
    public Result findAll(Integer page, Integer size,
                          @ApiParam("用户id") @RequestParam(required = false) Long userId,
                          @ApiParam("用户手机号") @RequestParam(required = false) String phone,
                          @ApiParam("分销 0全部 1未到账 2已到账") @RequestParam(required = false) Integer status,
                          @ApiParam("佣金来源 0全部 1直属 2自己 3非直属)") @RequestParam(required = false) Integer moneyFrom) {
        return service.findAll(page, size, userId, phone, status, moneyFrom);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @GetMapping("/statistaical")
    @ApiOperation("我的佣金统计")
    public Result statistaical(Long userId) {
        return service.statistaical(userId);
    }

    @GetMapping("/tuanStatistaical")
    @ApiOperation("团队佣金统计")
    public Result tuanStatistaical(Long userId) {
        return service.tuanStatistaical(userId);
    }

    @GetMapping(value = "/tuanSum")
    @ApiOperation("获取团队总收益")
    public Result tuanSum(Long userId) {
        return ResultUtil.success(service.tuanSum(userId));
    }

    @GetMapping(value = "/team")
    @ApiOperation("团队列表")
    public Result getTeam(Long userId, Integer type) {
        return ResultUtil.success(service.getTeam(userId, type));
    }

}

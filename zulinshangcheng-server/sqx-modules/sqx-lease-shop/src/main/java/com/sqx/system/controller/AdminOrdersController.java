package com.sqx.system.controller;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.service.sys.TbOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/24 16:20
 */
@RestController
@RequestMapping("/admin/orders/")
@Api(value = "订单相关-管理端", tags = {"订单相关-管理端"})
public class AdminOrdersController {

    @Autowired
    private TbOrdersService tbOrdersService;

    @GetMapping("getOrdersList")
    @ApiOperation("用户订单列表")
    public Result getOrdersList(Integer page, Integer limit, TbOrders tbOrders) {
        return tbOrdersService.getOrdersList(page, limit, tbOrders);
    }

    @GetMapping("selectOrdersByBusiness")
    @ApiOperation("商家订单列表")
    public Result selectOrdersByBusiness(Integer page, Integer limit, TbOrders tbOrders) {
        return tbOrdersService.selectOrdersByBusiness(page, limit, tbOrders);
    }

}

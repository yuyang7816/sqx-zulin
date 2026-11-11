package com.sqx.app.controller.selfOrder;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.SysEvaluate;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 14:33
 */
@RestController
@Api(value = "自营商城订单---用户端", tags = {"自营商城订单管理---用户端"})
@RequestMapping(value = "/app/orders")
public class AppOrdersController {

    @Autowired
    private OrdersService service;

    @Login
    @GetMapping("/findMyList")
    @ApiOperation("我的订单列表")
    public Result findMyList(Integer page, Integer size,
                             @ApiParam("用户id") @RequestParam String userId,
                             @ApiParam(value = "订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价") String status,
                             @ApiParam("发货方式：1包邮 2邮寄 3自取")String isExpress) {
        return service.findMyList(page, size, userId, status,isExpress);
    }


    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存订单")
    public Result saveBody(@RequestBody Orders entity, @RequestAttribute Long userId) {
        entity.setUserId(userId);
        return service.saveBody(entity);
    }

    @Login
    @PostMapping("/countMoney")
    @ApiOperation("计算订单价格")
    public Result countMoney(Orders entity){
        return service.countMoney(entity);
    }



    @Login
    @PostMapping("/update")
    @ApiOperation("零钱修改(买断租赁的商品)")
    public Result updateBody(@RequestBody Orders entity,@RequestAttribute Long userId) {
        return service.updateBody(entity,userId);
    }

    @Login
    @PostMapping("/continuingLease")
    @ApiOperation("支付完后调这个继续租赁商品的接口)")
    public Result continuingLease(@RequestBody Orders entity,@RequestAttribute Long userId) {
        return service.continuingLease(entity,userId);
    }

    @Login
    @PostMapping("/returnGoods")
    @ApiOperation("归还商品)")
    public Result returnGoods(@RequestBody Orders entity,@RequestAttribute Long userId) {
        return service.returnGoods(entity,userId);
    }

    @Login
    @GetMapping("/calculateTheNumberDay")
    @ApiOperation("计算该订单是否超出当前时间，超出多久，计算金额")
    public Result calculateTheNumberDay(Long id,@RequestAttribute Long userId) {
        return service.calculateTheNumberDay(id,userId);
    }

    @Login
    @ApiOperation(value = "取消订单，orderStatus=1时可取消")
    @GetMapping("/cancel")
    public Result orderCancel(@ApiParam("id") @RequestParam Long id) {
        return service.orderCancel(id);
    }

    @Login
    @ApiOperation(value = "确认收货，orderStatus=3是收货")
    @GetMapping("/confirm")
    public Result orderConfirm(@ApiParam("id") @RequestParam Long id) {
        return service.orderConfirm(id);
    }

    @Login
    @ApiOperation("零钱支付")
    @GetMapping("/changePay")
    public Result changePay(String orderId,BigDecimal money, Integer classify,Integer orderType,@RequestAttribute Long userId) {
        return service.changePay(orderId,money,classify,orderType,userId);
    }

    @Login
    @ApiOperation("零钱补缴租金")
    @PostMapping("/changePaySupplementMoney")
    public Result changePaySupplementMoney(@RequestAttribute Long userId, String orderId, BigDecimal supplementMoney) {
        return service.changePaySupplementMoney(userId,orderId,supplementMoney);
    }

    @Login
    @ApiOperation("用户端提醒发货")
    @GetMapping("/remind")
    public Result remind(Long orderId) {
        return service.remind(orderId);
    }

    @Login
    @ApiOperation("查快递")
    @GetMapping("/findExpress")
    public Result findExpress(String expressNumber) {
        return service.findExpress(expressNumber);
    }

    @Login
    @ApiOperation("查快递")
    @GetMapping("/findExpressByOrderNum")
    public Result findExpressByOrderNum(String expressNumber) {
        return Result.success(service.findExpressByOrderNum(expressNumber));
    }

    @Login
    @ApiOperation("用户端修改订单")
    @PostMapping("/updateOrder")
    public Result updateOrder(Orders orders) {
        return service.updateOrder(orders);
    }


    /**
     * 用户端买家评价
     *
     * @param userId
     * @param orderId
     * @param evaluateMessage  评价内容
     * @param satisfactionFlag 评级 1:非常差 2:差 3:一般 4:满意 5:非常满意
     * @param evaluateImg      评价图片
     * @return
     */
    @Login
    @PostMapping("/appEvaluate")
    @ApiOperation("用户端买家评价")
    public Result appEvaluate(@ApiParam("用户id") @RequestAttribute Long userId,
                              @ApiParam("订单id") Long orderId,
                              @ApiParam("被评价人id")Long byUserId,
                              @ApiParam("评价内容") String evaluateMessage,
                              @ApiParam("评级 1:非常差 2:差 3:一般 4:满意 5:非常满意") Integer satisfactionFlag,
                              @ApiParam("评价图片") String evaluateImg) {
        return service.appEvaluate(userId, orderId, byUserId,evaluateMessage, satisfactionFlag, evaluateImg);
    }

    @Login
    @ApiOperation("检查优惠券是否商品券")
    @GetMapping("/checkCoupon")
    public Result checkCoupon(@RequestAttribute Long userId,Long id,Long couponUserId) {
        return service.checkCoupon(userId,id,couponUserId);
    }

    @ApiOperation("用户查看订单评价列表")
    @GetMapping(value = "/getEvaluateList")
    public Result getEvaluateList(@ApiParam("页码") Integer page,
                                  @ApiParam("条数") Integer limit,
                                  @ApiParam("商品id")Long goodId) {
        return service.getOrderEvaluateList(page, limit,goodId);
    }

    @ApiOperation("用户端查看继续租赁的历史记录")
    @GetMapping(value = "/getContinuingLeaseList")
    public Result getContinuingLeaseList(@ApiParam("页码") Integer page,
                                         @ApiParam("条数") Integer limit,
                                         @ApiParam("订单id")Long orderId) {
        return service.getContinuingLeaseList(page, limit,orderId);
    }

}

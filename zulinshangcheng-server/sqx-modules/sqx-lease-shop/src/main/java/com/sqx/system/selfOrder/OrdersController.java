package com.sqx.system.selfOrder;

import com.sqx.auth.app.annotation.Login;
import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.excel.ExcelData;
import com.sqx.common.core.utils.excel.ExportExcelUtils;
import com.sqx.domain.repository.entity.app.SysEvaluate;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(value = "自营商城订单", tags = {"自营商城订单管理"})
@RequestMapping(value = "/orders")
public class OrdersController {
    @Autowired
    private OrdersService service;

    @GetMapping("/list")
    @ApiOperation("后台管理列表")
    public Result findAll(Integer page, Integer size,
                          @ApiParam("订单号") @RequestParam(required = false) String orderNum,
                          @ApiParam("订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价") @RequestParam String status,
                          @ApiParam("商品名称") @RequestParam(required = false) String title,
                          @ApiParam("手机号") @RequestParam(required = false) String mobile,
                          @ApiParam("店铺Id") @RequestParam(required = false) Long storeId,
                          @ApiParam("是否需要发货(1包邮 2邮寄 3自取）") @RequestParam(required = false) Integer isExpress,
                          String startTime, String endTime,
                          @ApiParam("订单类型（0全部 1普通订单 2拼团 3秒杀 4积分订单 5租赁 6买断") @RequestParam Integer orderType,
                          @ApiParam("下单人") @RequestParam(required = false)String consignee,
                          @ApiParam("商家发货的快递单号")@RequestParam(required = false)String sellerNumber,
                          @ApiParam("用户归还发货的快递单号")@RequestParam(required = false)String expressNumber) {
        return service.findAll(page, size, orderNum, status, title, mobile, storeId, isExpress, orderType,consignee,sellerNumber,expressNumber,startTime, endTime);
    }

    @IgnoreSecurity
    @GetMapping("/listExcel")
    @ApiOperation("后台管理列表")
    public void listExcel(
            @ApiParam("订单号") @RequestParam(required = false) String orderNum,
            @ApiParam("订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价") @RequestParam String status,
            @ApiParam("商品名称") @RequestParam(required = false) String title,
            @ApiParam("手机号") @RequestParam(required = false) String mobile,
            @ApiParam("店铺Id") @RequestParam(required = false) Long storeId,
            @ApiParam("3核销商品") @RequestParam(required = false) Integer isExpress,
            String startTime, String endTime,
            @ApiParam("订单类型（0全部 1普通订单 2拼团 3秒杀 4积分订单 5租赁 6买断") @RequestParam Integer orderType,
            HttpServletResponse response) throws Exception {
        ExcelData data = service.listExcel(orderNum, status, title, mobile, storeId, isExpress, orderType,startTime,endTime);
        ExportExcelUtils.exportExcel(response, "订单列表.xlsx", data);
    }

    @Login
    @GetMapping("/findMyList")
    @ApiOperation("我的订单列表")
    public Result findMyList(Integer page, Integer size,
                             @ApiParam("用户id") @RequestParam String userId,
                             @ApiParam(value = "订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价") String status,
                             @ApiParam("发货方式：1包邮 2邮寄 3自取")String isExpress) {
        return service.findMyList(page, size, userId, status,isExpress);
    }

    @GetMapping("/findMyRefundList")
    @ApiOperation("我的售后订单列表")
    public Result findMyRefundList(Integer page, Integer size,
                                   @ApiParam("用户id") @RequestParam String userId) {
        return service.findMyRefundList(page, size, userId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存订单")
    public Result saveBody(@RequestBody Orders entity,@RequestAttribute Long userId) {
        entity.setUserId(userId);
        return service.saveBody(entity);
    }

    @PostMapping("/saveAll")
    @ApiOperation("购物车结算多个订单")
    public Result saveBody(@RequestBody List<Orders> list) {
        return service.saveAll(list);
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改(买断租赁的商品)")
    public Result updateBody(@RequestBody Orders entity,@RequestAttribute Long userId) {
        return service.updateBody(entity,userId);
    }

    @Login
    @PostMapping("/continuingLease")
    @ApiOperation("继续租赁商品)")
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

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @ApiOperation(value = "取消订单，orderStatus=1时可取消")
    @GetMapping("/cancel")
    public Result orderCancel(@ApiParam("id") @RequestParam Long id) {
        return service.orderCancel(id);
    }

    @ApiOperation("后台管理去发货")
    @GetMapping("/express")
    public Result express(Long id, String expressName, String sellerNumber) {
        return service.express(id, expressName, sellerNumber);
    }

    @ApiOperation("后台管理虚拟去发货")
    @GetMapping("/expressVirtual")
    public Result expressVirtual(Long id) {
        return service.expressVirtual(id);
    }

    @ApiOperation(value = "管理端点击确认收货，status改为4待归还")
    @GetMapping("/confirm")
    public Result orderConfirm(@ApiParam("id") @RequestParam Long id,Integer status) {
        return service.confirmOrder(id,status);
    }

    @ApiOperation(value = "用户归还商品，管理端点status改为6已完成")
    @PostMapping("/orderComplete")
    public Result orderComplete(@ApiParam("id") Long id, @ApiParam("status") Integer status) {
        return service.orderComplete(id, status);
    }

    @ApiOperation(value = "收入金额统计")
    @GetMapping("/income")
    public Result income(@ApiParam("data") @RequestParam String data, @ApiParam("way： 1年 2月 3日") @RequestParam int way,
                         @ApiParam("店铺Id") @RequestParam(required = false) Long storeId) {
        return service.income(data, way, storeId);
    }

    @ApiOperation(value = "订单成交量统计")
    @GetMapping("/statistical")
    public Result statistical(@ApiParam("data") @RequestParam String data,
                              @ApiParam("店铺Id") @RequestParam(required = false) Long storeId) {
        return service.statistical(data, storeId);
    }

    @ApiOperation("检测是否完成支付")
    @GetMapping("/wxPay")
    public Result wxPayWeb(Long ordersId) {
        return service.checkPay(ordersId);
    }

    @ApiOperation("零钱支付")
    @GetMapping("/changePay")
    public Result changePay(String orderId) {
        return service.changePay(orderId,null,null,null,null);
    }

    @ApiOperation("积分支付")
    @GetMapping("/jiFenPay")
    public Result jiFenPay(String ordersId) {
        return service.jiFenPay(ordersId);
    }

    @ApiOperation("申请退款")
    @GetMapping("/refund")
    public Result refund(Long ordersId, String refund) {
        return service.refund(ordersId, refund);
    }

    @ApiOperation("后台管理确认退款")
    @GetMapping("/refundMoney")
    public Result refundMoney(Long ordersId) {
        return service.refundMoney(ordersId);
    }

    @ApiOperation("后台管理拒绝退款")
    @GetMapping("/refusedRefund")
    public Result refusedRefund(Long ordersId, String refusedRefund) {
        return service.refusedRefund(ordersId, refusedRefund);
    }

    @ApiOperation("后台管理待处理订单")
    @GetMapping("/pendingOrder")
    public Result pendingOrder() {
        return service.pendingOrder();
    }

    @ApiOperation("用户端提醒发货")
    @GetMapping("/remind")
    public Result remind(Long orderId) {
        return service.remind(orderId);
    }

    @ApiOperation("后台管理被提醒发货")
    @GetMapping("/remindOrder")
    public Result remindOrder(Long userId) {
        return service.remindOrder();
    }

    @ApiOperation("后台管理 用户付款统计")
    @GetMapping("/payCount")
    public Result payCount(Long userId) {
        return service.payCount(userId);
    }

    @ApiOperation("用户端 订单统计")
    @GetMapping("/count")
    public Result count(Long userId) {
        return service.count(userId);
    }

    @ApiOperation("查快递")
    @GetMapping("/findExpress")
    public Result findExpress(String expressNumber) {
        return service.findExpress(expressNumber);
    }

    @ApiOperation("批量核销订单")
    @GetMapping("/isExpress")
    public Result isExpress(String ids) {
        return service.isExpress(ids);
    }

    @ApiOperation("管理端修改订单状态")
    @PostMapping("/updateOrderStatus")
    public Result updateOrderStatus(Long id,Integer status) {
        return service.updateOrderStatus(id,status);
    }

    @ApiOperation("查看评价列表")
    @GetMapping(value = "/getEvaluateList")
    public Result getEvaluateList(@ApiParam("页码") Integer page,
                                  @ApiParam("条数") Integer limit,
                                  @ApiParam("评价实体") SysEvaluate sysEvaluate) {
        return service.getEvaluateList(page, limit, sysEvaluate);
    }
}

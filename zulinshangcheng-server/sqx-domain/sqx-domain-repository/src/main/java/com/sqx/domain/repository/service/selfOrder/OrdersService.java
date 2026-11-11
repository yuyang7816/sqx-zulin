package com.sqx.domain.repository.service.selfOrder;


import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.excel.ExcelData;
import com.sqx.domain.repository.entity.app.SysEvaluate;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.SelfGoodsVirtual;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;
import java.util.List;

public interface OrdersService {
    //列表
    Result findAll(Integer page, Integer size, String orderNum, String status, String title, String mobile, Long storeIdm,
                   Integer isExpress, Integer orderType, String consignee,String sellerNumber,String expressNumber,String startTime, String endTime);

    ExcelData listExcel(String orderNum, String status, String title, String mobile, Long storeId, Integer isExpress, Integer orderType, String startTime, String endTime);

    //我的订单
    Result findMyList(Integer page, Integer size, String userId, String status,String isExpress);

    //我的售后订单接口
    Result findMyRefundList(Integer page, Integer size, String userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(Orders entity);

    //多订单添加
    Result saveAll(List<Orders> list);

    // 修改(买断租赁的商品)
    Result updateBody(Orders entity,Long userId);

    //收入
    Result income(String data, int way, Long storeId);

    //订单数统计
    Result statistical(String data, Long storeId);

    //取消订单
    Result orderCancel(Long id);

    //后台管理去发货
    Result express(Long id, String expressName, String sellerNumber);

    Result expressVirtual(Long id);

    //确认收货
    Result orderConfirm(Long id);

    //检测完成支付
    Result checkPay(Long id);

    //根据订单查询订单
    Orders findByOrderNum(String orderNum);

    //根据支付单号查询多个订单
    List<Orders> findByPayNum(String payNum);

    //零钱支付
    Result changePay(String orderId,BigDecimal money, Integer classify,Integer orderType, Long userId);

    //积分支付
    Result jiFenPay(String id);

    //用户申请退款
    Result refund(Long id, String refund);

    //退款到余额
    Result refundMoney(Long id);

    //后台管理拒绝退款
    Result refusedRefund(Long ordersId, String refusedRefund);

    /**
     * 订单分销
     * @param ordersId
     */
    void fenXiao(Long ordersId);

    //后台管理待处理订单
    Result pendingOrder();

    //提醒发货
    Result remind(Long orderId);

    //后台管理被提醒发货
    Result remindOrder();

    //拼团订单处理
    void pink(Orders orders);

    //用户订单支付统计
    Result payCount(Long userId);

    //处理虚拟商品
    SelfGoodsVirtual sendExpress(Orders orders);

    //用户端订单统计
    Result count(Long userId);

    //查快递
    Result findExpress(String expressNumber);

    String findExpressByOrderNum(String expressNumber);

    //核销订单
    Result isExpress(String orderNum);

    //购物车订单取消处理
    void cancel(List<Orders> list);

    //计算订单是否超出当前时间，超出多久，计算金额
    Result calculateTheNumberDay(Long id,Long userId);

    void payOrderBack(Long id, BigDecimal payMoney, Integer classify);

    void supplementMoneyBack(Long id, BigDecimal payMoney, Integer classify);

    Result continuingLease(Orders entity, Long userId);

    void continuingLeaseOrder(Orders entity, Long userId);

    Result returnGoods(Orders entity, Long userId);

    Integer selectOrdersCountStatisticsByYear(Integer flag,String time,Integer status);

    Result updateOrderStatus(Long id, Integer status);

    Result changePaySupplementMoney(Long userId,String orderId, BigDecimal supplementMoney);

    Result updateOrder(Orders orders);

    Result appEvaluate(Long userId, Long orderId, Long byUserId, String evaluateMessage, Integer satisfactionFlag, String evaluateImg);


    Result getEvaluateList(Integer page, Integer limit, SysEvaluate sysEvaluate);

    Result confirmOrder(Long id, Integer status);

    Result orderComplete(Long id, Integer status);

    Result checkCoupon(Long userId,Long id,Long couponUserId);

    Result getOrderEvaluateList(Integer page, Integer limit,Long goodId);

    Result countMoney(Orders entity);

    Result getContinuingLeaseList(Integer page, Integer limit, Long orderId);
}

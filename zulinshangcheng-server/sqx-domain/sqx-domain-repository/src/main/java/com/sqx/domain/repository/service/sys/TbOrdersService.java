package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbOrders;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
* @author Admin
* @description 针对表【tb_orders(用户订单表)】的数据库操作Service
* @createDate 2024-09-14 14:43:13
*/
public interface TbOrdersService extends IService<TbOrders> {

    Result addOrders(TbOrders tbOrders);

    Result getOrdersList(Integer page, Integer limit,TbOrders tbOrders);

    Result getOrdersById(Long id,Long userId);

    Result selfPickupByGoods(Long userId,String orderNumber);

    Result merchantShipping(Long userId,Long id);

    Result selectOrdersByBusiness(Integer page, Integer limit,TbOrders tbOrders);

    Result receiveGoods(Long userId,Long id);

    Result continuingLease(TbOrders tbOrders);

    Result buyout(Long userId,Long id);

    Result returnOrder(TbOrders tbOrders);

    Result businessDelivery(Long userId,Long id);

    Result payOrderBack(Long orderId, BigDecimal money, Integer classify);
}

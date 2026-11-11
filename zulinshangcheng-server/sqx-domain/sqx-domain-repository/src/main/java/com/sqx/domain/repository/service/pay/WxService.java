package com.sqx.domain.repository.service.pay;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Orders;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    String payBack(String resXml,Integer type);

    Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify,Integer type) throws Exception;

    boolean refund(String ordersNo, BigDecimal refundMoney);

    Result payOrder(Long userId, Long orderId, BigDecimal money,Integer classify,Integer orderType) throws Exception;

    Result paySupplement(Long userId, Long orderId, BigDecimal supplementMoney, Integer classify) throws Exception;

    Result continuingLease(Long userId, Orders entity, Integer classify);

    Result updateBody(Orders entity, Long userId, Integer classify);

    //boolean refund(Orders orders);

//    Result payCoupon(Long userId, Long couponId, Integer buyNum, Integer payType, HttpServletRequest request) throws Exception;

}
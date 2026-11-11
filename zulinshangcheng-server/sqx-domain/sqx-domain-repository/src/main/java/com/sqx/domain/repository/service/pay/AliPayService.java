package com.sqx.domain.repository.service.pay;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Orders;

import java.math.BigDecimal;

public interface AliPayService {
    /**
     *
     * @param userId
     * @param money 支付金额
     * @param classify 4支付宝APP支付 5支付宝H5支付
     * @param parentId 关联id
     * @return
     */
    Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify,Integer type);

    boolean aliRefund(String orderId, BigDecimal refundMoney);

    Result payOrder(Long userId, Long orderId,BigDecimal money, Integer classify,Integer orderType);

    /**
     * 补缴租金
     * @param userId
     * @param orderId
     * @param supplementMoney
     * @param classify
     * @return
     */
    Result payRent(Long userId, Long orderId, BigDecimal supplementMoney, Integer classify);

    Result continuingLease(Long userId, Orders entity, Integer classify);

    Result updateBody(Orders entity, Long userId, Integer classify);
}

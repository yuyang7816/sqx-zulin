package com.sqx.domain.repository.service.selfWXPay;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public interface SelfWXService {

    /**
     * 微信支付
     * @param type
     * @return
     * @throws Exception
     */
    Map doUnifiedOrder(String payNum, Integer type, Long userId, BigDecimal payMoney, HttpServletRequest request) throws Exception;

    /**
     * 支付回调
     * @param resXml
     * @return
     */
    String payBack(String resXml);
    String payBacks(String resXml);


    //退款
    boolean refund(Long ordersId);

    /**
     * 小程序支付
     * @param payNum
     * @param userId
     * @param payMoney
     * @return
     */
    Map wxPayMp(String payNum, Long userId, BigDecimal payMoney) throws Exception ;

    //小程序支付回调
    String payBackMp(String resXml);

}
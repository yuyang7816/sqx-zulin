package com.sqx.domain.repository.service.selfAliPay;

public interface SelfAliPayService {

    //退款
    boolean refund(Long ordersId);
}

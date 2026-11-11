package com.sqx.domain.repository.entity.app.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/4/1 9:20
 */
@Data
public class PaymentFormVO {
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 支付方式 1H5 2App
     */
    private String paymentType;
    /**
     * 会员类型id
     */
    private Long vipDetailsId;
    /**
     * 业务类型 1充值 2会员
     */
    private Integer businessType;

    /**
     * classify 支付方式 0余额支付 1app  2 二维码支付  3小程序 公众号支付 4支付宝APP支付 5支付宝H5支付  6stripe支付  7payPal支付
     */
    private Integer classify;

    /**
     * stripe支付的路径
     */
    private String success;

    /**
     * stripe支付取消的路径
     */
    private String cancel;

}

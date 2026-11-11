package com.sqx.domain.repository.entity.app;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetails {
    /**
     * 本月订单数量
      */
    private int monthlyOrderNum;
    /**
     * 本月充值金额
     */
    private BigDecimal monthlyRechargeMoney;
    /**
     *本月提现数量
     */
    private int monthWithdrawalNum;
    /**
     * 本月提现金额
     */
    private  BigDecimal monthlyWithdrawalMoney;

    //用户详情id
    private Long id;
    //用户id
    private Long userID;
    //1用户足迹 2收藏
    private Integer classify;
    //订单id
    private Long orderID;
    //创建时间
    private String createTime;


}

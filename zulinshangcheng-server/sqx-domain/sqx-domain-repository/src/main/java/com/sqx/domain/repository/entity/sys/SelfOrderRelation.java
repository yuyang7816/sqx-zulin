package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 自营商城订单分销
 */
@Data
@Entity
public class SelfOrderRelation {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long orderId; //订单id
    /**收益人的信息*/
    @Column
    private Long userId; //用户id
    @Column
    private String userName; //用户姓名
    @Column
    private String phone; //用户手机号
    @Column
    private BigDecimal commissionPrice; //订单佣金
    @Column
    private String createAt; //创建时间
    @Column
    private String detail; //分销明细
    @Column
    private Integer status; //1未到账 2已到账
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal payMoney; //订单实付金额
    @Column
    private Long goodsId; //商品id
    @Column
    private String goodsImg; //商品照片
    @Column
    private String goodsTitle; //商品标题
    @Column
    private Integer moneyFrom; //佣金来源（1直属 2自己 3非直属）
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal commissionMoney; //代理佣金/团长佣金
    @Column
    private Long lowerUserId; //下级用户id
    @Column
    private String lowerUserName; //下级用户名称
    @Column
    private String finishTime; //收货时间
}

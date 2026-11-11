package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城优惠券发布
 */
@Data
@Entity
public class SelfCouponIssue {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponIssueId; //优惠券发布id
    @Column
    private String startTime; //领取开启时间
    @Column
    private String endTime; //领券结束时间
    @Column
    private Integer issueNumber; //发布数量
    @Column
    private Integer remainNumber; //剩余数量
    @Column
    private Integer isLimit; //是否限量（1限量 2不限量）
    @Column
    private Integer status; //状态（1开启 2关闭）
    @Column
    private String createTime; //创建时间
    /**优惠券信息*/
    @Column
    private Long couponId; //优惠券id
    @Column
    private Integer type; //优惠券类型（1通用券 2商品券）
    @Column
    private String couponName; //优惠券名称
    @Column
    private String goodsImages;
    @Column
    private String lessMoney;
    @Column
    private String minMoney;
    @Transient
    private SelfCoupon coupon; //优惠券实体
    /**商品信息*/
    @Column
    private String goodsIds; //商品ids(多个)
    /**领取状态*/
    @Transient
    private Integer getStatus; //领取状态（1可领取 2已领取）

}

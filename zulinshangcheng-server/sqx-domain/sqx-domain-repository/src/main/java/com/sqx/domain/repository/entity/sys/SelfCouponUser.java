package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 自营商城优惠券用户领取记录
 */
@Data
@Entity
public class SelfCouponUser implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponUserId; //优惠券用户id
    @Column
    private String failureTime; //失效时间
    @Column
    private Integer status; //状态（1未使用 2已使用 3已过期）
    @Column
    private String createTime; //创建时间
    /**优惠券信息*/
    @Column
    private Long couponId; //优惠券id
    /**
     * 优惠券类型（1通用券 2商品券）
     */
    @Column
    private Integer type; //优惠券类型（1通用券 2商品券）
    @Column
    private String goodsIds; //商品id
    @Column
    private String couponName; //优惠券名称
    @Column
    private String lessMoney; //优惠券面值
    @Column
    private String minMoney; //优惠券最低消费
    @Column
    private String goodsImages;
    @Transient
    @TableField(exist = false)
    private SelfCoupon coupon; //优惠券实体
    /**优惠券发布信息*/
    @Column
    private Long couponIssueId; //优惠券发布id
    /**用户信息*/
    @Column
    private Long userId; //用户id
    @Column
    private String nickName; //用户昵称

    private static final long serialVersionUID = 1L;
    public SelfCouponUser() {
    }
}

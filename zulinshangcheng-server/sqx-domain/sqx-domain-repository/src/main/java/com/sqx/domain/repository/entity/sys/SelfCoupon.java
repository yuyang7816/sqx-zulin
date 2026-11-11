package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城优惠券制作
 */
@Data
@Entity
public class SelfCoupon {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId; //优惠券id
    /**
     * 优惠券类型（1通用券 2商品券）
     */
    @Column
    private Integer type; //优惠券类型（1通用券 2商品券）
    @Column
    private String couponName; //优惠券名称
    @Column
    private String lessMoney; //优惠券面值
    @Column
    private String minMoney; //优惠券最低消费
    @Column
    private Integer validDay; //优惠券有效期限(天)
    @Column
    private Integer sort; //排序
    @Column
    private Integer status; //状态（1开启 2关闭）
    @Column
    private String createTime; //创建时间
    /**商品信息*/
    @Column
    private String goodsIds; //商品ids(多个)
    @Column
    private String goodsImages; //商品图片(多个)

}

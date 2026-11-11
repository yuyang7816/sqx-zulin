package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城购物车
 */
@Data
@Entity
public class SelfCart {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long userId; //用户id
    @Column
    private Long goodsId; //商品id
    @Column
    private Long skuId; //商品skuId
    @Column
    private Integer number; //个数
    @Transient
    private Goods goods; //商品信息
    @Transient
    private SelfGoodsSku sku; //商品sku信息
    @Transient
    private Integer status; //商品状态 1正常 2下架 3失效
}

package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品拼团SKU
 */
@Data
@Entity
public class SelfGroupSku {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long groupId; //拼团id
    @Column
    private Long secKillId; //秒杀id
    @Column
    private Long goodsId; //商品id
    @Column
    private String skuImg; //sku图片
    @Column
    private Double skuOriginalPrice; //sku原价
    @Column
    private Double skuPrice; //sku商品售价
    @Column
    private Double memberPrice; //会员价格
    @Column
    private Integer stock; //库存
    @Column
    private Integer sales; //销量
    @Column
    private String detailJson; //sku信息，json封装
    @Column
    private Double pinkPrice; //拼团价格
    @Column
    private Integer pinkStock; //拼团库存
    @Column
    private Double secKillPrice; //秒杀价格
    @Column
    private Integer secKillStock; //秒杀库存
}

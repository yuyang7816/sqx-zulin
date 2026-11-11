package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品品牌
 */
@Data
@Entity
public class SelfGoodsBrand {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId; //品牌id
    @Column
    private String brandName; //品牌名称
    @Column
    private String names; //品牌别名
    @Column
    private String createTime; //创建时间
}

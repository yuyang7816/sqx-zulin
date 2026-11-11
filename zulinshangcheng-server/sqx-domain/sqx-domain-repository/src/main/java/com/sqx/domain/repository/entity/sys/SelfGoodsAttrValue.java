package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品属性值
 */
@Data
@Entity
public class SelfGoodsAttrValue {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long goodsId; //商品id
    @Column
    private Long attrId; //商品规格id
    @Column
    private String detail; //属性值组合:{尺寸: "7寸", 颜色: "红底"}
    @Column
    private String value; //规格属性名称

}

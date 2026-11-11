package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城商品属性
 */
@Data
@Entity
public class SelfGoodsAttr {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long goodsId; //商品id
    @Column
    private Long ruleId; //规格模板id
    @Column
    private String attrName; //属性名称
    @Transient
    private List<SelfGoodsAttrValue> attrValue; //规格值
}

package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品规格属性
 */
@Entity
@Data
public class SelfGoodsRuleValue {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long ruleId; //规格id
    @Column
    private String value; //规格属性名称
    @Column
    private String detail; //规格属性值
}

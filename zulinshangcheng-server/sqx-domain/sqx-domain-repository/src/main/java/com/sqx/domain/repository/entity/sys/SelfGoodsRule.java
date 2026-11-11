package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城商品规格
 */
@Data
@Entity
public class SelfGoodsRule {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private String ruleName; //规格名称
    @Column
    private String createTime; //创建时间
    @Transient
    private List<SelfGoodsRuleValue> ruleValue; //规格值
}

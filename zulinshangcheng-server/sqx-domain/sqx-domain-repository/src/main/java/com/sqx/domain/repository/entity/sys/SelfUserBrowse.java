package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城用户浏览
 */
@Data
@Entity
public class SelfUserBrowse {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private String createTime; //创建时间
    /**商品*/
    @Column
    private Long goodsId; //商品id
    @Column
    private String title; //商品标题
    @Column
    private String coverImg; //商品封面图片
    @Column
    private Double price; //商品价格
    @Transient
    private Goods goods; //商品实体
    /**用户*/
    @Column
    private Long userId; //用户id
}

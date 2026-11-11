package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品SKU
 */
@Data
@Entity
public class SelfGoodsSku {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
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
    private Double dailyRent; //日租金
    @Column
    private Double memberRent; //会员日租金

    /**
     * 新旧程度
     */
    @ApiModelProperty("新旧程度")
    private String oldDegree;
}

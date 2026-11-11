package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租赁物品的规格表
 * @TableName goods_specifications
 */
@Data
public class GoodsSpecifications implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodId;

    /**
     * 新旧程度（如7成新 99新等）
     */
    @ApiModelProperty("新旧程度（如7成新 99新等）")
    private String degree;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     * 尺寸（或手机容量大小）
     */
    @ApiModelProperty("尺寸（或手机容量大小）")
    private String size;

    /**
     * 重量
     */
    @ApiModelProperty("重量")
    private BigDecimal weight;

    /**
     * 网络类型（全网通等）
     */
    @ApiModelProperty("网络类型（全网通等）")
    private String networkType;

    /**
     * 版本类型（中国大陆等）
     */
    @ApiModelProperty("版本类型（中国大陆等）")
    private String versionType;

    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Integer stockQuantity;

//    /**
//     * 租赁价格（单位：元）
//     */
//    @ApiModelProperty("租赁价格（单位：元）")
//    private BigDecimal rentalPrice;

    /**
     * 是否删除：0未删除 1已删除
     */
    @ApiModelProperty("是否删除：0未删除 1已删除")
    private Integer deleteFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

//    /**
//     * 买断价格
//     */
//    @ApiModelProperty("买断价格")
//    private BigDecimal purchasePrice;

//    /**
//     * 押金
//     */
//    @ApiModelProperty("押金")
//    private BigDecimal antecedentMoney;

    private static final long serialVersionUID = 1L;
}
package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 二手物品回收估价表
 * @TableName goods_recovery
 */
@Data
public class GoodsRecovery implements Serializable {
    /**
     * 
     */
    @ApiModelProperty("回收估价id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 回收的商品名称
     */
    @ApiModelProperty("回收的商品名称")
    private String name;

    /**
     * 商品的id
     */
    @ApiModelProperty("商品的id")
    private Long goodId;

    /**
     * 商品品牌id
     */
    @ApiModelProperty("商品品牌id")
    private Long brandId;


    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否删除：0未删除  1已删除
     */
    @ApiModelProperty("是否删除：0未删除  1已删除")
    private Integer deleteFlag;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 品牌名称
     */
    @TableField(exist = false)
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 商品的实体
     */
    @TableField(exist = false)
    @ApiModelProperty("商品的实体")
    private Goods goods;

    /**
     * 拼接后的回收规格属性id
     */
    @ApiModelProperty("拼接后的回收规格属性id,暂未使用")
    private String specsId;

    /**
     * 拼接后的回收规格属性的值
     */
    @ApiModelProperty("拼接后的回收规格属性的值,暂未使用")
    private String specsValue;

    private static final long serialVersionUID = 1L;
}
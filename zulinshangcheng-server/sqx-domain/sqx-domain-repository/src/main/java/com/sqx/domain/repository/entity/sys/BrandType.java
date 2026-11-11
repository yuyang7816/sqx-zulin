package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌分类表
 * @TableName brand_type
 */
@Data
public class BrandType implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商城专区id
     */
    @ApiModelProperty("商城专区id")
    private Long sectionId;

    /**
     * 品牌类型名称
     */
    @ApiModelProperty("品牌类型名称")
    private String brandTypeName;

    /**
     * 品牌类型图片
     */
    @ApiModelProperty("品牌类型图片")
    private String img;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 状态 1正常 2隐藏
     */
    @ApiModelProperty("状态 1正常 2隐藏")
    private Integer state;

    /**
     * 分类 1banner图 2金刚区分类
     */
    @ApiModelProperty("分类 1banner图 2金刚区分类")
    private Integer classify;

    private static final long serialVersionUID = 1L;
}
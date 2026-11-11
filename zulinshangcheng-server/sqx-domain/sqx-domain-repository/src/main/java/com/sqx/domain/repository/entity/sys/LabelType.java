package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类标签
 * @TableName label_type
 */
@Data
public class LabelType implements Serializable {
    /**
     * 标签id
     */
    @TableId(value = "label_id", type = IdType.AUTO)
    private Long labelId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String label;

    /**
     * 是否已启用0否 1是
     */
    @ApiModelProperty("是否已启用0否 1是")
    private Integer isEnable;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否是回收类型 0否 1是
     */
    @ApiModelProperty("是否是回收类型 0否 1是")
    private Integer isRecovery;

    /**
     * 是否是商品分类 0否 1是
     */
    @ApiModelProperty("是否是商品分类 0否 1是")
    private Integer isType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
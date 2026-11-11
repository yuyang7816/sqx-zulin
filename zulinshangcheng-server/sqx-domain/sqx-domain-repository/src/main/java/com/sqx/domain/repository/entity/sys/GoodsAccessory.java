package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品配件表
 * @TableName goods_accessory
 */
@TableName(value ="goods_accessory")
@Data
public class GoodsAccessory implements Serializable {
    /**
     * 配件id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配件名称
     */
    private String accessoryName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 配件数量
     */
    private Integer accessoryNum;

    /**
     * 一级商品分类id
     */
    private Long typeId;

    /**
     * 商品分类名称
     */
    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
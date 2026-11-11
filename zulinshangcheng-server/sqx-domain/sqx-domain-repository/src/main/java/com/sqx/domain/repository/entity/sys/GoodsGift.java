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
 * 赠品数量
 * @TableName goods_gift
 */
@TableName(value ="goods_gift")
@Data
public class GoodsGift implements Serializable {
    /**
     * 赠品id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 赠品数量
     */
    private Integer giftNum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

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
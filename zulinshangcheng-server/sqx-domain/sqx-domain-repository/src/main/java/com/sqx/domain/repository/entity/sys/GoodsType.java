package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品分类
 * @TableName goods_type
 */
@Data
@Entity
public class GoodsType implements Serializable {
    /**
     * 分类id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 或者使用其他适合的策略
    private Long id;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    @Transient
    @TableField(exist = false)
    private Long labelId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 分类图片
     */
    @ApiModelProperty("分类图片")
    private String img;

    /**
     * 是否启用 0否 1是
     */
    @ApiModelProperty("是否启用 0否 1是")
    @Transient
    @TableField(exist = false)
    private Integer isEnable;

    /**
     * 是否是回收类型 0否 1是
     */
    @ApiModelProperty("是否是回收类型 0否 1是")
    @Transient
    @TableField(exist = false)
    private Integer isRecovery;

    /**
     * 是否是商品分类 0否 1是
     */
    @ApiModelProperty("是否是商品分类 0否 1是")
    @Transient
    @TableField(exist = false)
    private Integer isType;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createAt;

    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    private Long parentId;

    /**
     * 下级分类id
     */
    @Transient
    @ApiModelProperty("下级分类id")
    @TableField(exist = false)
    private Long value;
    /**
     * 下级分类名称
     */
    @ApiModelProperty("下级分类名称")
    @Transient
    @TableField(exist = false)
    private String label;

    /**
     * 下级分类
     */
    @ApiModelProperty("下级分类")
    @Transient
    @TableField(exist = false)
    private List<GoodsType> children; //下级分类

    /**
     * 上级分类
     */
    @ApiModelProperty("上级分类")
    @Transient
    @TableField(exist = false)
    private List<GoodsType> parent;

    private static final long serialVersionUID = 1L;
}
package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.domain.repository.entity.sys.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户浏览足迹表
 * @TableName tb_footprint
 */
@Data
public class TbFootprint implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodId;

    /**
     * 访问时间
     */
    @ApiModelProperty("访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date visitTime;

    /**
     * 商品实体
     */
    @ApiModelProperty("商品实体")
    @Transient
    @TableField(exist = false)
    private Goods goods;

    /**
     * 提取的日期部分
     */
    @ApiModelProperty("提取的日期部分")
    @Transient
    @TableField(exist = false)
    private String visitDate ;

    private static final long serialVersionUID = 1L;
}
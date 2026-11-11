package com.sqx.domain.repository.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租赁须知表
 * @TableName rental_guidelines
 */
@Data
public class RentalGuidelines implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 服务标题
     */
    @ApiModelProperty("服务标题")
    private String title;

    /**
     * 服务内容
     */
    @ApiModelProperty("服务内容")
    private String content;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
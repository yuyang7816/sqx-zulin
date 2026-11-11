package com.sqx.domain.repository.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 常见问题表
 * @TableName faqs
 */
@Data
public class Faqs implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 常见问题
     */
    @ApiModelProperty("常见问题")
    private String question;

    /**
     * 回复
     */
    @ApiModelProperty("回复")
    private String answer;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
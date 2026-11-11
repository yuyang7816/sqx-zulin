package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description help_word
 * @author fang
 * @date 2022-06-06
 */
@Data
public class HelpWord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 帮助文档id
     */
    @ApiModelProperty(value = "帮助文档id")
    @TableId(type = IdType.AUTO)
    private Long helpWordId;

    /**
     * 帮助标题
     */
    @ApiModelProperty(value = "帮助标题")
    private String helpWordTitle;

    /**
     * 帮助分类
     */
    @ApiModelProperty(value = "帮助分类id")
    private Integer helpClassifyId;

    /**
     * 帮助文档内容
     */
    @ApiModelProperty(value = "帮助文档内容")
    private String helpWordContent;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    public HelpWord() {}
}

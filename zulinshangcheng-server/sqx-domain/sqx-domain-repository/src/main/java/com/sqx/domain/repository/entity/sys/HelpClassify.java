package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description help_classify
 * @author fang
 * @date 2022-06-06
 */
@Data
public class HelpClassify implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 帮助中心分类
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "帮助中心分类id")
    private Long helpClassifyId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String helpClassifyName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private Long parentId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "帮助中心分类列表")
    private List<HelpClassify> helpClassifyList;

    @TableField(exist = false)
    @ApiModelProperty(value = "帮助文档列表")
    private List<HelpWord> helpWordList;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<HelpClassify> children;

    public HelpClassify() {}
}
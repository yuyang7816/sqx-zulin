package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价表 用户
 * @TableName sys_evaluate
 */
@Data
public class SysEvaluate implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "evaluate_id", type = IdType.AUTO)
    private Long evaluateId;

    /**
     * 用户id(评分人)
     */
    @ApiModelProperty(value = "用户id(评分人)")
    private Long userId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String indentNumber;

    /**
     * 评价内容
     */
    @ApiModelProperty(value = "评价内容")
    private String evaluateMessage;

    /**
     * 评级 1:非常差 2:差 3:一般 4:满意 5:非常满意
     */
    @ApiModelProperty(value = "评级 1:非常差 2:差 3:一般 4:满意 5:非常满意")
    private Integer satisfactionFlag;

    /**
     * 评价时间
     */
    @ApiModelProperty(value = "评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 评价图片
     */
    @ApiModelProperty(value = "评价图片")
    private String evaluateImg;

    /**
     * 是否已删除0否 -1是
     */
    @ApiModelProperty(value = "是否已删除0否 -1是")
    private Integer isDelete;

//    /**
//     * 是否回收订单: 0否 1是
//     */
//    @ApiModelProperty(value = "是否回收订单：0否 1是")
//    private Integer recycleStatus;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField(exist = false)
    private String userName;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @TableField(exist = false)
    private String avatar;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    @TableField(exist = false)
    private String goodsTitle;

//    /**
//     * 评价审核: 0待审核 1已通过 2未通过
//     */
//    @ApiModelProperty(value = "评价审核: 0待审核 1已通过 2未通过")
//    private Integer feedbackStatus;

//    /**
//     * 审核内容
//     */
//    @ApiModelProperty(value = "审核内容")
//    private String feedbackContent;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    @TableField(exist = false)
    private Integer total;

    /**
     * type 1普通商品评论 2回收商品评论 3动态评论
     */
    @ApiModelProperty(value = "type 1普通商品评论 2回收商品评论 3动态评论")
    @TableField(exist = false)
    private Integer type;

    /**
     * 被评价人id
     */
    @ApiModelProperty(value = "被评价人id")
    private Long byUserId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @TableField(exist = false)
    private Long goodId;



    private static final long serialVersionUID = 1L;
}
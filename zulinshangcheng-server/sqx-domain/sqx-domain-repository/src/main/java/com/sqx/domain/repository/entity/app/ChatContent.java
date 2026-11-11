package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 聊天内容对象 chat_content
 * 
 * @author fang
 * @date 2020-03-17
 */
@Data
@TableName("chat_content")
public class ChatContent {
    private static final long serialVersionUID = 1L;

    /** 聊天内容id */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "聊天内容id")
    private Long chatContentId;

    /** 聊天会话id */
    @ApiModelProperty(value = "聊天会话id")
    private Long chatConversationId;

    /** 聊天内容 */
    @ApiModelProperty(value = "聊天内容")
    private String content;

    /** 聊天类型 */
    @ApiModelProperty(value = "聊天类型")
    private String messageType;

    /** 宽度 */
    @ApiModelProperty(value = "宽度")
    private String width;

    /** 高度 */
    @ApiModelProperty(value = "高度")
    private String height;

    /** 语音时长 */
    @ApiModelProperty(value = "语音时长")
    private String voiceLength;

    /** 发送人 */
    @ApiModelProperty(value = "发送人")
    private Long userId;

    /** 状态（0未读 1已读） */
    @ApiModelProperty(value = "状态（0未读 1已读）")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String goodId;

    /**
     * 是否删除 1是
     */
    private Integer isDelete;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @TableField(exist = false)
    private String phone;

}

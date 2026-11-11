package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 聊天会话对象 chat_conversation
 * 
 * @author fang
 * @date 2020-03-17
 */
@Data
@TableName("chat_conversation")
public class ChatConversation implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 聊天会话id */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "聊天会话id")
    private Long chatConversationId;

    /** 发起人id */
    @ApiModelProperty(value = "发起人id")
    private Long userId;

    /** 接收人id */
    @ApiModelProperty(value = "接收人id")
    private Long focusedUserId;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private Long status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否发送消息")
    private Integer isSendMsg;

    @ApiModelProperty(value = "是否微信消息")
    private Integer isWxMsg;

    /**
     * 是否删除 1是
     */
    private Integer isDelete;

    /**
     * 商品id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品id")
    private String goodId;

}

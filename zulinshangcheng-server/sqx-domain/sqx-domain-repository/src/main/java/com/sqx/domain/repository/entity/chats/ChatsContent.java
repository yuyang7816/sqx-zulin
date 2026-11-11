package com.sqx.domain.repository.entity.chats;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 聊天会话内容
 */

@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "chats_content",comment = "聊天会话内容")
public class ChatsContent implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigInt(20) comment '会话内容id'")
    @ApiModelProperty(value = "会话内容id")
    private Long chatContentId;
    @Column(columnDefinition = "text comment '聊天内容'")
    @ApiModelProperty(value = "聊天内容")
    private String content;
    @Column(columnDefinition = "int(1) comment '类型（1文字 2图片 3订单 4商品）'")
    @ApiModelProperty(value = "类型（1文字 2图片 3订单 4商品）")
    private Integer type;
    @Column(columnDefinition = "int default 1  comment '是否已读(1未读 2已读)'")
    @ApiModelProperty(value = "是否已读(1未读 2已读)")
    private Integer status;
    @Column(columnDefinition = "varchar(255) comment '创建时间'")
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @Column(columnDefinition = "int(1) comment '消息来源（1用户消息 2后台消息）'")
    @ApiModelProperty(value = "消息来源（1用户消息 2后台消息）")
    private Integer sendType;
    @Column(columnDefinition = "bigInt(20) comment '用户id'")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @Column(columnDefinition = "bigInt(20) comment '后台id（总后台传0）'")
    @ApiModelProperty(value = "后台id（总后台传0）")
    private Long storeId;
    /**会话信息*/
    @Column(columnDefinition = "bigInt(20) comment '会话id'")
    @ApiModelProperty(value = "会话id")
    private Long chatId;
    @Transient
    @ApiModelProperty(value = "聊天会话")
    @TableField(exist = false)
    private Chats chat; //聊天会话

    public Long goodId;
}

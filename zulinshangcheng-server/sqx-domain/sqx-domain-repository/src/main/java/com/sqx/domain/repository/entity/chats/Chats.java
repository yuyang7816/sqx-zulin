package com.sqx.domain.repository.entity.chats;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 聊天会话
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "chats",comment = "聊天会话")
public class Chats implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigInt(20) comment '会话id'")
    @ApiModelProperty(value = "会话id")
    private Long chatId;
    @Column(columnDefinition = "varchar(255) comment '创建时间'")
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**用户信息*/
    @Column(columnDefinition = "bigInt(20) comment '用户id'")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @Column(columnDefinition = "varchar(255) comment '用户头像'")
    @ApiModelProperty(value = "用户头像")
    private String userHead;
    @Column(columnDefinition = "varchar(255) comment '用户昵称'")
    @ApiModelProperty(value = "用户昵称")
    private String userName;
    /**商户信息*/
    @Column(columnDefinition = "bigInt(20) comment '总后台id(总后台传0'")
    @ApiModelProperty(value = "总后台id(总后台传0)")
    private Long storeId;
    @Column(columnDefinition = "varchar(255) comment '后台头像'")
    @ApiModelProperty(value = "后台头像")
    private String storeHead;
    @Column(columnDefinition = "varchar(255) comment '商户昵称'")
    @ApiModelProperty(value = "商户昵称")
    private String storeName;
    /**聊天内容*/
    @Column(columnDefinition = "int default 0 comment'用户未读条数'")
    @ApiModelProperty(value = "用户未读条数")
    private Integer userCount;
    @Column(columnDefinition = "int default 0 comment'后台未读条数'")
    @ApiModelProperty(value = "后台未读条数")
    private Integer storeCount;
    @Transient
    @ApiModelProperty(value = "聊天内容")
    private String content; //聊天内容
    @Transient
    @ApiModelProperty(value = "消息时间")
    private String contentTime; //消息时间
}

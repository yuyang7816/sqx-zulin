package com.sqx.domain.repository.entity.chat;

import lombok.Data;

import javax.persistence.*;

/**
 * 聊天会话
 */
@Data
@Entity
public class Chat {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId; //会话id
    @Column
    private String createTime; //创建时间
    /**用户信息*/
    @Column
    private Long userId; //用户id
    @Column
    private String userHead; //用户头像
    @Column
    private String userName; //用户昵称
    /**商户信息*/
    @Column
    private Long storeId; //店铺id(总后台商户传0)
    @Column
    private String storeHead; //商户头像
    @Column
    private String storeName; //商户昵称
    /**聊天内容*/
    @Column(columnDefinition = "int default 0")
    private Integer userCount; //用户未读条数
    @Column(columnDefinition = "int default 0")
    private Integer storeCount; //商户未读条数
    @Transient
    private String content; //聊天内容
    @Transient
    private String contentTime; //消息时间
}

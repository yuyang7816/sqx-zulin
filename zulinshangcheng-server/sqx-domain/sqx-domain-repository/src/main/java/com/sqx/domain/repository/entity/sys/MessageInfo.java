package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.domain.repository.entity.app.UserEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

;

/**
 * @author fang
 * @date 2020/7/13
 */
@Data
@Entity
@TableName("message_info")
public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 消息id
     */
    @Id
    private Long id;

    /**
     * 内容
     */
    @Column
    private String content;

    /**
     * 创建时间
     */
    @Column
    private String createAt;

    /**
     * 图片
     */
    @Column
    private String image;

    /**
     * is_see
     */
    @Column
    private String isSee;

    /**
     * send_state
     */
    @Column
    private String sendState;

    /**
     * send_time
     */
    @Column
    private String sendTime;

    /**
     * 分类
     */
    @Column
    private String state;

    /**
     * 标题
     */
    @Column
    private String title;

    /**
     * 地址
     */
    @Column
    private String url;

    /**
     * type
     */
    @Column
    private String type;
    /**
     * 联系方式
     */
    @Column
    private String phone;

    /**
     * platform  来源
     * 1 聊天页面新增投诉功按钮
     * 2 用户主页新增投诉按钮
     * 3 动态详情新增投诉按钮

     */
    @Column
    private Integer platform;

    /**
     * 用户id
     */
    @Column
    private String userId;

    /**
     * 用户名
     */
    @Column
    private String userName;

    /**
     * 审核内容
     */
    @Column
    private String auditContent;

    /**
     * 审核状态 1不做处理 2封号 3下架内容
     */
    @Column
    private Integer status;

    /**
     * 被举报用户
     */
    @Column
    private String byUserId;

    @TableField(exist = false)
    @Transient
    private String byUserName;

    /**
     * 来源id
     */
    @Column
    private Long platformId;
    /**
     * 服务id
     */
    @Column
    private Long dataId;

    @Transient
    @TableField(exist = false)
    private UserEntity userEntity;

    private String wxBizMsgCrypt;

    private String toUserName;

    private String fromUserName;

    public MessageInfo() {}

}
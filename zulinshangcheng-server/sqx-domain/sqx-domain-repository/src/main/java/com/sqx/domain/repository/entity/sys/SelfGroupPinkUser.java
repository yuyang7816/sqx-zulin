package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城拼团用户信息
 */
@Data
@Entity
public class SelfGroupPinkUser {
    /**基本信息*/
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long groupPinkId; //拼团团体id
    @Column
    private String createTime; //创建时间
    /**用户信息*/
    @Column
    private String headerImg; //头像
    @Column
    private String nickName; //昵称
    @Column
    private Long userId; //用户id
    @Column
    private String mobile; //手机号
    /**订单信息*/
    @Column
    private Long orderId; //订单id
}

package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城拼团团体
 */
@Data
@Entity
public class SelfGroupPink {
    /**基本信息*/
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private String createTime; //创建时间
    @Column
    private String startTime; //开团时间
    @Column
    private String endTime; //结束时间
    @Column
    private Integer needNumber; //需要人数
    @Column
    private Integer countNumber; //拼团人数
    @Column
    private Integer status; //拼团状态（1拼团中 2已完成 3未完成）
    /**拼团商品信息*/
    @Column
    private Long groupId; //拼团商品id
    @Transient
    private SelfGroup selfGroup; //拼团商品实体
    @Transient
    private List<SelfGroupPinkUser> pinkUserList; //参团用户列表
}

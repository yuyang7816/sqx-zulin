package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城发货提醒
 */
@Data
@Entity
public class SelfOrderRemind {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private Long ordersId; //订单id
    @Column
    private String createTime; //创建时间
    @Column
    private Integer status; //状态（1提醒 2收到）

}

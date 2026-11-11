package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城秒杀时段
 */
@Data
@Entity
public class SelfSecKillTime {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secKillTimeId; //秒杀时段id
    @Column
    private Integer startHour; //开始时间（整小时数）
    @Column
    private Integer holdHour; //持续时间（整小时数）
    @Transient
    private Integer status; //状态(1已结束 2抢购中 3即将开始)
}

package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城秒杀规则+商品
 */
@Data
@Entity
public class SelfSecKill {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secKillId; //秒杀商品id
    /**商品信息*/
    @Column
    private Long goodsId; //商品id
    @Column
    private String coverImg; //封面图
    @Column(columnDefinition="text")
    private String img; //轮播图
    @Column
    private String title; //活动标题
    @Column(columnDefinition="text")
    private String descrition; //商品描述
    @Column
    private Double originalPrice; //商品原价
    @Column
    private Double price; //商品价格
    @Column
    private Double memberPrice; //会员价格
    @Column
    private Double commissionPrice; //商品佣金
    @Column
    private Double postagePrice; //邮费
    @Transient
    private Goods goods; //商品对象
    @Transient
    private List<SelfGroupSku> sku; //拼团sku
    /**秒杀规则*/
    @Column
    private Long secKillTimeId; //秒杀时限id
    @Column
    private String startTime; //秒杀开始时间
    @Column
    private String endTime; //秒杀结束时间
    @Column
    private Integer number; //限购
    /**通用信息*/
    @Column
    private String createTime; //创建时间
    @Column
    private Integer status; //状态（1上架 2下架）
    @Transient
    private String statusString; //状态展示
}

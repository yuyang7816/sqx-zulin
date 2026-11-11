package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城商品评论
 */
@Data
@Entity
public class SelfGoodsComment {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; //评论id
    @Column
    private String content; //评论内容
    @Column
    private String img; //评论图片（逗号拼接）
    @Column
    private String createTime; //创建时间
    /**
     * 分数
     */
    @Column
    private Integer score; //分数
    @Column
    private Integer scoreType; //分数类型（1好评 2中评 3差评）
    /**
     * 商品
     */
    @Column
    private Long goodsId; //商品id
    @Transient
    @TableField(exist = false)
    private Goods goods;
    @Column
    private String skuId; //商品skuId
    @Column
    private String sku; //商品规格名称
    /**
     * 订单id
     */
    @Column
    private Long orderId; //订单id
    /**
     * 用户
     */
    @Column
    private Long userId; //用户id
    @Column
    private String userName; //用户昵称
    @Column
    private String userHeader; //用户头像
    /**
     * 回复
     */
    @Column
    private String reply; //回复
}

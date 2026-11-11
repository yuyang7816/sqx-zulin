package com.sqx.domain.repository.entity.sys;

import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.app.UserEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城虚拟商品
 */
@Data
@Entity
public class SelfGoodsVirtual {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //虚拟商品id
    @Column
    private String linkUrl; //访问链接
    @Column
    private String content; //卡密内容
    @Column
    private Integer status; //状态（1正常 2关闭 3已使用）
    @Column
    private String createTime; //创建时间
    /**商品信息*/
    @Column
    private Long goodsId; //商品id
    @Column
    private String title; //商品标题
    @Column
    private String coverImg; //商品封面图片
    /**订单信息*/
    @Transient
    private TbOrders orders; //订单
    @Transient
    private String orderId; //订单id
    @Transient
    private String orderNum; //订单号
    @Transient
    private String payMoney; //支付金额
    @Transient
    private String payWay; //支付方式
    @Transient
    private String expressTime; //发货时间
    /**用户信息*/
    @Transient
    private UserEntity userInfo; //用户
    @Transient
    private String nickName; //昵称
    @Transient
    private String phone; //手机号
}

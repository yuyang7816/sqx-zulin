package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 自营商城订单
 */
@Data
@Entity
public class Orders implements Serializable {
    /**
     * 订单id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("订单id")
    @TableId(type = IdType.AUTO)
    private Long id; //id

    @TableField(exist = false)
    @Transient
    private Long orderId;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    @Column
    private String orderNum; //订单号

    /**
     * 续租订单号
     */
    @ApiModelProperty("续租订单号")
    @Column
    private String reOrderNum;

    /**
     * 买断订单号
     */
    @ApiModelProperty("买断订单号")
    @Column
    private String buyOrderNum;

    /**
     * 支付单号
     */
    @ApiModelProperty("支付单号")
    @Column
    private String payNum; //支付单号

    /**
     * 订单备注
     */
    @ApiModelProperty("订单备注")
    @Column
    private String descrition; //订单备注
    /**
     * 商品个数
     */
    @Column
    @ApiModelProperty("商品个数")
    private Integer number; //商品个数
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal payMoney; //支付金额
    /**
     * 订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
     */
    @Column
    @ApiModelProperty("订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价")
    private Integer status; //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中 10已评价 单商户）
    /**
     * 支付方式：1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
     */
    @ApiModelProperty("支付方式：1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分")
    @Column
    private Integer payWay; //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分）
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column
    private String createAt; //创建时间
    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    @Column
    private String payTime; //支付时间
    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @Column
    private String finishTime; //完成时间
    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    @Column
    private String consignee; //收货人
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Column
    private String mobile; //手机号
    /**
     * 省市区
     */
    @ApiModelProperty("省市区")
    @Column
    private String provinces; //省市区
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    @Column
    private String detail; //详细地址
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @Column
    private Long userId; //用户id
    /**
     * 分销id
     */
    @ApiModelProperty("分销id(拿到返利的人)")
    @Column
    private String relationId; //分销id(拿到返利的人)
    /**
     * 是否需要发货(1包邮 2邮寄 3自取）
     */
    @ApiModelProperty("是否需要发货(1包邮 2邮寄 3自取）")
    @Column
    private Integer isExpress; //是否需要发货(1包邮 2邮寄 3自取）
    /**
     * 邮费
     */
    @ApiModelProperty("邮费")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal postagePrice; //邮费
    /**
     * 快递名称
     */
    @ApiModelProperty("快递名称")
    @Column
    private String expressName; //快递名称
    /**
     * 用户发货的快递单号
     */
    @Column
    private String expressNumber; //用户发货的快递单号
    /**
     * 发货时间
     */
    @Column
    private String expressTime; //发货时间
    @Column
    private Long virtualId; //虚拟商品id
    /**退款*/
    @Column
    private Integer isRefund; //是否可以退款（2不可退款）
    @Column
    private String refund; //退款理由
    @Column
    private String refusedRefund; //拒绝退款理由
    /**商品*/
    @Column
    private Long goodsId; //商品id

    @Transient
    @TableField(exist = false)
    private Goods goods; //商品信息
    @Column
    private Long skuId; //商品skuId
    @Column
    private String detailJson; //商品sku信息
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal commissionPrice; //订单佣金
    @Column
    private Long type; //商品类型id
    @Column
    private String title; //商品标题
    @Column
    private String img; //商品图片
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal price; //商品价格
    /**
     * 订单类型（1普通订单 2拼团订单 3秒杀订单 4积分订单 5租赁 6买断）
     */
    @ApiModelProperty("订单类型（1普通订单 2拼团订单 3秒杀订单 4积分订单 5租赁 6买断）")
    @Column
    private Integer orderType; //订单类型（1普通订单 2拼团订单 3秒杀订单 4积分订单 5租赁 6买断）
    @Column
    private Long groupId; //拼团商品Id
    @Column
    private Long groupPinkId; //加入拼团团体id
    /**秒杀*/
    @Column
    private Long secKillId; //秒杀商品id
    /**优惠券*/
    @Column
    private Long couponUserId; //用户优惠券id
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal couponMoney; //优惠券优惠金额
    /**分销*/
    @Transient
    @TableField(exist = false)
    private List<SelfOrderRelation> relationList; //订单分销列表
    /**多商户*/
    @Column
    private Long merchantId; //商户id
    /**店铺信息*/
    @Column
    private Long storeId; //店铺id
    @Column
    private String storeName; //店铺名称
    /**
     * 是否复购（1普通购买 2复购）
     */
    @Column
    private Integer isRepeat; //是否复购（1普通购买 2复购）
    /**是否购物车订单*/
    @Column(columnDefinition="int(1) comment'购物车订单 1'")
    private Integer isCart;

    /**
     * 租赁天数id
     */
    @ApiModelProperty("租赁天数id")
    private Long dayId;

    /**
     * 一级佣金
     */
    @ApiModelProperty("一级佣金")
    private BigDecimal oneMoney;

    /**
     * 一级用户
     */
    @ApiModelProperty("一级用户")
    private Long oneUserId;

    /**
     * 二级佣金
     */
    @ApiModelProperty("二级佣金")
    private BigDecimal twoMoney;

    /**
     * 二级用户
     */
    @ApiModelProperty("二级用户")
    private Long twoUserId;

    /**
     * 租赁天数
     */
    @ApiModelProperty("租赁天数")
    private Integer leaseTerm;

    /**
     * 押金
     */
    @ApiModelProperty("押金")
    private BigDecimal deposit;

    /**
     * 总租金
     */
    @ApiModelProperty("总租金")
    private BigDecimal grossRent;

    /**
     * 预计起租日
     */
    @ApiModelProperty("预计起租日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseStartDate;

    /**
     * 预计归还日
     */
    @ApiModelProperty("预计归还日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date returnDate;

    /**
     * 继续租赁日期
     */
    @ApiModelProperty("继续租赁日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date continueDate;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date cancelTime;

    /**
     * 预估价格
     */
    @ApiModelProperty("预估价格")
    private BigDecimal estimateMoney;

    /**
     * 日租金
     */
    @ApiModelProperty("日租金")
    private BigDecimal dailyRent;

    /**
     * 计算归还天数超出后超出的价格
     */
    @Transient
    @TableField(exist = false)
    @ApiModelProperty("计算归还天数超出后超出的价格")
    private BigDecimal exceedPrice;

    /**
     * 买断时间
     */
    @ApiModelProperty("买断时间")
    private Date purchaseTime;

    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long addressId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 归还时间
     */
    @ApiModelProperty("归还时间")
    private Date returnTime;

    /**
     * 补缴租金
     */
    @ApiModelProperty("补缴租金")
    private BigDecimal supplementMoney;

    /**
     * 补缴是否支付成功：1是 2否
     */
    @ApiModelProperty("补缴是否支付成功：1是 2否")
    private Integer supplementStatus;

    /**
     * 补缴时间
     */
    @ApiModelProperty("补缴时间")
    private Date supplementTime;

    /**
     * 补缴单号
     */
    @ApiModelProperty("补缴单号")
    private String supplementNum;

    /**
     * 是否已评论 0否 1是 (默认：0)
     */
    @ApiModelProperty(value = "是否已评论 0否 1是(默认：0)")
    private Integer isEvaluate;

    /**
     * 继续租赁的总金额
     */
    @ApiModelProperty("继续租赁的总金额")
    private BigDecimal totalContinueMoney;

    /**
     * 商家发货的快递单号
     */
    @ApiModelProperty("商家发货的快递单号")
    private String sellerNumber;

    /**
     * 用户收货时间
     */
    @ApiModelProperty("用户收货时间")
    private Date deliveryTime;

    /**
     * 发货人手机号
     */
    @ApiModelProperty("发货人手机号")
    private String sendPhone;

    /**
     * 评价时间
     */
    @ApiModelProperty("评价时间")
    private Date evaluationTime;

    /**
     * 是否逾期：1已逾期 2未逾期
     */
    @ApiModelProperty("是否逾期：1已逾期 2未逾期")
    private Integer overdueStatus;

    /**
     * 原始买断价格
     */
    @ApiModelProperty("原始买断价格")
    private BigDecimal buyoutPrice;

    /**
     * 续租金额
     */
    @ApiModelProperty("续租金额")
    @TableField(exist = false)
    @Transient
    private BigDecimal continuingLeaseMoney;

    private static final long serialVersionUID = 1L;
    public Orders() {
    }
}

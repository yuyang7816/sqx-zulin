package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单表
 * @TableName tb_orders
 */
@Data
public class TbOrders implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 下单人id
     */
    @ApiModelProperty("下单人id")
    private Long userId;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String consignee;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String phone;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 订单备注
     */
    @ApiModelProperty("订单备注")
    private String remark;

    /**
     * 用户优惠券id
     */
    @ApiModelProperty("用户优惠券id")
    private Long userCouponsId;

    /**
     * 优惠金额
     */
    @ApiModelProperty("优惠金额")
    private BigDecimal couponMoney;

    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long addressId;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String addressDetail;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 快递名称
     */
    @ApiModelProperty("快递名称")
    private String expressName;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String expressNumber;

    /**
     * 租赁天数id
     */
    @ApiModelProperty("租赁天数id")
    private Long dayId;

    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;

    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date finishTime;

    /**
     * 支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
     */
    @ApiModelProperty("支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）")
    private Integer payWay;

    /**
     * 订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消
     */
    @ApiModelProperty("订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消")
    private Integer payStatus;

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
     * 规格id
     */
    @ApiModelProperty("规格id")
    private Long specId;

    /**
     * 已选规格的json数据
     */
    @ApiModelProperty("已选规格的json数据")
    private String selectedSpec;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer payNum;

    /**
     * 租赁天数
     */
    @ApiModelProperty("租赁天数")
    private Integer leaseTerm;

    /**
     * 运费
     */
    @ApiModelProperty("运费")
    private BigDecimal freight;

    /**
     * 押金
     */
    @ApiModelProperty("押金")
    private BigDecimal deposit;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNumber;

    /**
     * 总租金
     */
    @ApiModelProperty("总租金")
    private BigDecimal grossRent;

    /**
     * 支付金额（合计）
     */
    @ApiModelProperty("支付金额（合计）")
    private BigDecimal payMoney;

    /**
     * 预计起租日
     */
    @ApiModelProperty("预计起租日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseStartDate;

    /**
     * 预计归还日
     */
    @ApiModelProperty("预计归还日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date returnDate;

    /**
     * 提货方式： 1商家邮寄 2到店自提
     */
    @ApiModelProperty("提货方式： 1商家邮寄 2到店自提")
    private Integer takeState;

    /**
     * 预估价格
     */
    @ApiModelProperty("预估价格")
    private BigDecimal estimateMoney;

    /**
     * 买断价格（补差）
     */
    @ApiModelProperty("买断价格（补差）")
    private BigDecimal buyoutMoney;

    /**
     * 到店自提的核销时间
     */
    @ApiModelProperty("到店自提的核销时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date cancellationTime;

    /**
     * 日租金
     */
    @ApiModelProperty("日租金")
    private BigDecimal dailyRent;

    /**
     * 自提地址
     */
    @ApiModelProperty("自提地址")
    private String pickupAddress;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 平台本单收入
     */
    @ApiModelProperty("平台本单收入")
    private BigDecimal platformMoney;

    /**
     * 商家本单收入
     */
    @ApiModelProperty("商家本单收入")
    private BigDecimal merchantIncome;

    /**
     * 商家id
     */
    @ApiModelProperty("商家id")
    private Long businessId;

    /**
     * 继续租赁日期
     */
    @ApiModelProperty("继续租赁日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date continueDate;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date cancelTime;

    private static final long serialVersionUID = 1L;
}
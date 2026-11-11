package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 订单备注
     */
    private String descrition;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 商品图片
     */
    private String img;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 商品个数
     */
    private Integer number;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
     */
    private Integer status;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 完成时间
     */
    private String finishTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
     */
    private Integer payWay;

    /**
     * 用户优惠券id
     */
    private Long couponUserId;

    /**
     * 订单佣金
     */
    private BigDecimal commissionPrice;

    /**
     * 会员邀请码id
     */
    private String relationId;

    /**
     * 省市区
     */
    private String provinces;

    /**
     * 快递名称
     */
    private String expressName;

    /**
     * 快递单号
     */
    private String expressNumber;

    /**
     * 退款理由
     */
    private String refund;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 是否需要发货(1包邮 2邮寄 3自取）
     */
    private Integer isExpress;

    /**
     * 是否可以退款（2不可退款）
     */
    private Integer isRefund;

    /**
     * 拒绝退款理由
     */
    private String refusedRefund;

    /**
     * skuid
     */
    private Long skuId;

    /**
     * 支付单号
     */
    private String payNum;

    /**
     * 商品sku信息
     */
    private String detailJson;

    /**
     * 拼团商品skuId
     */
    private Long groupSkuId;

    /**
     * 拼团id
     */
    private Long pinkId;

    /**
     * 秒杀id
     */
    private Long seckillId;

    /**
     * 拼团状态
     */
    private Integer pinkStatus;

    /**
     * 邮费
     */
    private BigDecimal postagePrice;

    /**
     * 加入拼团团体id
     */
    private Long groupPinkId;

    /**
     * 订单类型（1普通订单 2拼团 3秒杀 4积分订单 5租赁 6买断
     */
    private Integer orderType;

    /**
     * 拼团商品Id
     */
    private Long groupId;

    /**
     * 秒杀商品id
     */
    private Long secKillId;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 发货时间
     */
    private String expressTime;

    /**
     * 优惠券金额
     */
    private BigDecimal couponMoney;

    /**
     * 虚拟商品id
     */
    private Long virtualId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 是否复购（1普通购买 2复购）
     */
    private Integer isRepeat;

    /**
     * 购物车订单 1
     */
    private Integer isCart;

    /**
     * 租赁天数id
     */
    private Integer dayId;

    /**
     * 一级佣金
     */
    private BigDecimal oneMoney;

    /**
     * 一级用户
     */
    private Integer oneUserId;

    /**
     * 二级佣金
     */
    private BigDecimal twoMoney;

    /**
     * 二级用户
     */
    private Integer twoUserId;

    /**
     * 租赁天数
     */
    private String leaseTerm;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 总租金
     */
    private BigDecimal grossRent;

    /**
     * 续租日期
     */
    private Date continueDate;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 预计起租日
     */
    private Date leaseStartDate;

    /**
     * 预计归还日
     */
    private Date returnDate;

    /**
     * 预估价格
     */
    private BigDecimal estimateMoney;

    /**
     * 日租金
     */
    private BigDecimal dailyRent;

    /**
     * 买断时间
     */
    private Date purchaseTime;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 归还日期
     */
    private Date returnTime;

    /**
     * 补缴的租金
     */
    private BigDecimal supplementMoney;

    /**
     * 补缴是否支付成功：1是  2否
     */
    private Integer supplementStatus;

    /**
     * 补缴时间
     */
    private Date supplementTime;

    /**
     * 补缴单号
     */
    private String supplementNum;

    /**
     * 是否已评论 0否 1是
     */
    private Integer isEvaluate;

    /**
     * 继续租赁总金额
     */
    private BigDecimal totalContinueMoney;

    /**
     * 商家发货的快递单号
     */
    private String sellerNumber;

    /**
     * 用户收货时间
     */
    private Date deliveryTime;

    /**
     * 发货人手机号
     */
    private String sendPhone;

    /**
     * 评价时间
     */
    private Date evaluationTime;

    /**
     * 是否逾期：1已逾期  2未逾期
     */
    private Integer overdueStatus;

    /**
     * 当时买断价格
     */
    private BigDecimal buyoutPrice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
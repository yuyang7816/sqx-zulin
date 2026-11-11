package com.sqx.domain.repository.entity.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 充值记录
 *
 * @author fang 2020-05-14
 */
@Data
@Entity
@TableName("pay_details")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝APP支付  5支付宝H5支付 6零钱支付）
     */
    private Integer classify;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 充值金额
     */
    private Double money;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    private Integer state;
    /**
     * 关联id(如果是购买会员则是会员类型)
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付类型 1 余额充值  2订单支付 3购买会员 4支付补缴租金 5续租 6买断 7购买优惠券
     */
    private Integer type;

    private String remark;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String refundContent;

    @TableField(exist = false)
    private String outRequestNo;

    /**
     * 购买数量
     */
    @TableField(exist = false)
    private Integer buyNum;

    /**
     * 关联id
     */
    @TableField(exist = false)
    private Long relationId;

}

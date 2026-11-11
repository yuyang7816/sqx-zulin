package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Data
public class TbCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "coupon_id", type = IdType.AUTO)
    private Long couponId;

    /**
     * 优惠券名称
     */
    @TableField(condition = SqlCondition.LIKE)
    private String couponName;

    /**
     * 优惠券图片
     */
    private String couponPicture;

    /**
     * 有效期天数
     */
    private Integer validDays;

    /**
     * 优惠券可使用订单最低金额
     */
    private BigDecimal minMoney;

    /**
     * 优惠券抵扣金额
     */
    private BigDecimal money;
    /**
     * 是否删除 0未删除 1已删除
     */
    @TableLogic
    private Integer deleteFlag;
    /**
     * 是否启用 0未启用 1已启用
     */
    private Integer isEnable;
//    /**
//     * 优惠券类型 0全部可用 1即时代驾 2朋友代叫 3预约代驾
//     */
//    private Integer couponType;
    /**
     * 最多领取或购买数量(0为不限制数量)
     */
    private Integer maxReceive;
    /**
     * 优惠券数量
     */
    private Integer couponNum;
    /**
     * 商家昵称
     */
    @TableField(exist = false)
    private String shopName;
}

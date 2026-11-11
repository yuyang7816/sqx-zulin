package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName continuing_order
 */
@TableName(value ="continuing_order")
@Data
public class ContinuingOrder implements Serializable {
    /**
     * 继续租赁表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 起租日期
     */
    private Date startDate;

    /**
     * 结束租期
     */
    private Date endDate;

    /**
     * 租金
     */
    private BigDecimal leasePrice;

    /**
     * 租赁天数
     */
    private Integer leaseDay;

    /**
     * 租赁描述
     */
    private String remark;

    /**
     * 优惠金额
     */
    private BigDecimal discountMoney;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 排序
     */
    private Integer sort;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
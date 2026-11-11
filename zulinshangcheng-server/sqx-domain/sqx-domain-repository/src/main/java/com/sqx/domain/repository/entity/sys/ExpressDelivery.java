package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 快递公司表
 * @TableName express_delivery
 */
@TableName(value ="express_delivery")
@Data
public class ExpressDelivery implements Serializable {
    /**
     * 快递id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 快递公司名称
     */
    private String expressName;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 快递名称的缩写
     */
    private String parcelServices;

    /**
     * 快递邮寄情况描述
     */
    private String parcelDescribe;
    private static final long serialVersionUID = 1L;
}
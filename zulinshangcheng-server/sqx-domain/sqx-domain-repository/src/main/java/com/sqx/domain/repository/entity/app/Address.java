package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName address
 */
@Data
@Entity
public class Address implements Serializable {
    /**
     * 主键id
     */
    @Id()
    @TableId(type = IdType.AUTO)
    private Long addressId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 用户详细地址（用户手动输入的地址）
     */
    private String addressDetail;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 维度
     */
    private Double lat;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    private Integer deleteFlag;

    /**
     * 默认地址 0：非默认   1：默认
     */
    private Integer addressDefault;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
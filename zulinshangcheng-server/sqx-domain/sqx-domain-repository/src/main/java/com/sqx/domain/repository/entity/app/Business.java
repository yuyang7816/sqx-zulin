package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家认证表
 *
 * @TableName business
 */
@Data
public class Business implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 法人名称
     */
    @ApiModelProperty("法人名称")
    private String legalPerson;

    /**
     * 法人身份证号码
     */
    @ApiModelProperty("法人身份证号码")
    private String legalNumber;

    /**
     * 法人联系电话
     */
    @ApiModelProperty("法人联系电话")
    private String legalPhone;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String businessName;

    /**
     * 营业执照注册号
     */
    @ApiModelProperty("营业执照注册号")
    private String businessNumber;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String businessAddress;

    /**
     * 营业执照照片
     */
    @ApiModelProperty("营业执照照片")
    private String businessImg;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    /**
     * 审核原因
     */
    @ApiModelProperty("审核原因")
    private String remark;

    /**
     * 商家审核状态：0待审核  1已通过 2已拒绝
     */
    @ApiModelProperty("商家审核状态：0待审核  1已通过 2已拒绝")
    private Integer status;

    /**
     * 商家经度
     */
    @ApiModelProperty("商家经度")
    private Double lng;

    @ApiModelProperty("商家纬度")
    private Double lat;

    private static final long serialVersionUID = 1L;
}
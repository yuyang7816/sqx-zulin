package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liyuan
 * @description user_certification
 * @date 2021-08-13
 */
@Data
@ApiModel("user_certification")
public class UserCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户实名认证id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

//    /**
//     * 认证类型  1普通用户 2企业用户
//     */
//    private Integer userType;

    /**
     * 真实姓名|法人姓名
     */
    @ApiModelProperty("真实姓名|法人姓名")
    @TableField(condition = SqlCondition.LIKE)
    private String realName;

    /**
     * 证件号码|法人身份证
     */
    @ApiModelProperty("证件号码|法人身份证")
    @TableField(condition = SqlCondition.LIKE)
    private String idNumber;

//    /**
//     * 企业名称
//     */
//    @TableField(condition = SqlCondition.LIKE)
//    private String enterpriseName;

//    /**
//     * 企业联系电话
//     */
//    @TableField(condition = SqlCondition.LIKE)
//    private String enterprisePhone;

//    /**
//     * 营业执照
//     */
//    private String businessLicense;

//    /**
//     * 营业执照图片
//     */
//    private String businessLicenseImg;

    /**
     * 0待审核 1已通过 2未通过
     */
    @ApiModelProperty("0待审核 1已通过 2未通过")
    private Integer status;

    /**
     * 审核原因
     */
    @ApiModelProperty("审核原因")
    private String remark;
//    /**
//     * 个人详细地址|企业地址
//     */
//    private String enterpriseAddress;
    /**
     * 身份证正面图片
     */
    @ApiModelProperty("身份证正面图片")
    private String idCardFront;
    /**
     * 身份证反面图片
     */
    @ApiModelProperty("身份证反面图片")
    private String idCardVerso;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;
//    /**
//     * 是否允许提交0是 1否
//     */
//    private Integer isSubmit;

    @TableField(exist = false)
    private UserEntity userEntity;

    /**
     * 收货人姓名
     */
    @ApiModelProperty("收货人姓名")
    private String consignee;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String phone;


}

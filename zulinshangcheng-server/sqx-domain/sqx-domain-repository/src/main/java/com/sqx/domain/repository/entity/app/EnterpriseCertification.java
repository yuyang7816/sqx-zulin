package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("user_enterprise")
public class EnterpriseCertification implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    /**
     * 企业认证id
     */
    @ApiModelProperty("企业认证id")
    private Long id;

    /**
     * 法人姓名
     */
    @ApiModelProperty("法人姓名")
    private String name;

    /**
     * 法人身份证号码
     */
    @ApiModelProperty("法人身份证号码")
    private String idNumber;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    @TableField(exist = false)
    private UserEntity userEntity;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 状态
     */
    private Long status;

    /**
     * 说明
     */
    private String remek;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 详细地址
     */
    private String address;
    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 营业执照注册号
     */
    private String businessNumber;
}

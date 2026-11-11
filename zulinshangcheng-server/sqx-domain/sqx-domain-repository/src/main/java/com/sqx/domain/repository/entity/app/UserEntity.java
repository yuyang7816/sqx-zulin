package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
@Entity
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Id
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO,value = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;
	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * 微信App  openId
	 */
	private String accountOpenId;

	/**
	 * 收益
	 */
	private BigDecimal balance;

	/**
	 * 个人介绍
	 */
	private String profile;

	/**
	 * 加密盐
	 */
	private String salt;

	/**
	 * 苹果id
	 */
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	private Integer status;

	/**
	 * 来源 APP 小程序  公众号
	 */
	private String platform;

	/**
	 * app推送id
	 */
	private String clientid;

	/**
	 * 支付宝名称
	 */
	private String zhiFuBaoName;

	/**
	 * 支付宝账号
	 */
	private String zhiFuBao;

	/**
	 * 是否推送消息
	 */
	private Integer isSendMsg;

	/**
	 * 微信提现图片
	 */
	private String wxImg;

	/**
	 * 最近一条信息内容
	 */
	private String msg;

	/**
	 * 是否已认证 0否 1是
	 */
	private Integer isAuth;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 邀请码
	 */
	private String invitationCode;

	/**
	 * 邀请人的邀请码
	 */
	private String inviterCode;

	/**
	 * 邀请人上级的邀请码
	 */
	private String inviterInviterCode;

	/**
	 * 下级抽佣比例
	 */
	private String divideScale;

	/**
	 * 二级抽佣比例
	 */
	private String divideTwoScale;

	/**
	 * 是否是管理员 0否 1是
	 */
	private Integer isAdmin;

	/**
	 * 总金币数
	 */
	private Integer totalGold;

	@TableField(exist = false)
	private Integer isEnterpriseAuthentication;

	@TableField(exist = false)
	private BigDecimal rate;


	@TableField(exist = false)
	private String money;

	@ApiModelProperty("是否是会员：1是会员2不是")
	private Integer isVip;

	@TableField(exist = false)
	private Integer followCount;

	private LocalDateTime vipEndTime;
	@TableField(exist = false)
	private Integer statuss;
	@TableField(exist = false)
	private boolean real;
	/**
	 * 用户类型 0未认证 1普通用户 2商家
	 */
	private Integer userType;

	/**
	 * 积分
	 */
	@ApiModelProperty("积分")
	private BigDecimal orderJifen;

	@Column
	private int isInvitation;

	/**
	 * 0待审核 1已通过 2已拒绝
	 */
	@ApiModelProperty(value = "0待审核 1已通过 2已拒绝")
	@TableField(exist = false)
	private Integer status1;


	/**
	 * 信誉 1信誉差 2信誉良好 3信誉非常好
	 */
	@ApiModelProperty(value = "信誉 1信誉差 2信誉良好 3信誉非常好")
	private Integer reputationStatus;

	/**
	 * 系统给用户充值 1增加 2减少
	 */
	@TableField(exist = false)
	@Transient
	private Integer type;


}

package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("user_vip")
public class UserVip implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户会员ID
     */
    @TableId
    private Long vipId;
    /**
     * 会员类型
     */
    private Integer vipNameType;
    @TableField(exist = false)
    private VipDetails vipDetails;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 购买时间
     */
    private String createTime;
    /**
     * 到期时间
     */
    private String endTime;

    /**
     *是否是会员
     */
    private Integer  isVip;

}

package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description user_follow
 * @author liyuan
 * @date 2021-08-12
 */
@Data
@ApiModel("user_follow")
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long followId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 关注用户id
     */
    @ApiModelProperty("关注用户id")
    private Long followUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    public UserFollow() {}
}

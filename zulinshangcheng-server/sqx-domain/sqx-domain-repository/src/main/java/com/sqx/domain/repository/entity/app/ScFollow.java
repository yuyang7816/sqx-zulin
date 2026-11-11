package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @description sc_follow
 * @date 2021-08-12
 */
@Data
@ApiModel("sc_follow")
public class ScFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("信息id")
    private Long followId;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 被关注的id
     */
    @ApiModelProperty("被关注的id")
    private Long byId;
    /**
     * 类型 1商品收藏 2商品想要 3关注用户
     */
    @ApiModelProperty("类型 1商品收藏 2商品想要 3关注用户")
    private Integer classify;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String createTimeStr;

}

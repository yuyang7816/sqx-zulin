package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description user_browse
 * @author liyuan
 * @date 2021-08-12
 */
@Data
@ApiModel("user_browse")
public class UserBrowse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 浏览访客id
     */
    @ApiModelProperty("浏览访客id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 被浏览id
     */
    @ApiModelProperty("被浏览id")
    private Long byBrowseId;


    /**
     * 接单id
     */
    @ApiModelProperty("接单id")
    private Long takingId;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String updateTime;

    public UserBrowse() {}
}

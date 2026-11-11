package com.sqx.domain.repository.entity.app.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 用户
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 个人介绍
     */
    private String profile;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1男 2女
     */
    private Integer sex;

    /**
     * 是否关注（1关注  0未关注）
     */
    private Integer isFollow;
//
//    /**
//     * 商品列表
//     */
//    private List<Goods> goodsList;
//
//    /**
//     * 动态列表
//     */
//    private List<Trends> trendsList;
//
//    /**
//     * 评价列表
//     */
//    @TableField(exist = false)
//    private List<SysEvaluate> sysEvaluateList;

    /**
     * 关注数
     */
    private int followCount;

    /**
     * 粉丝数
     */
    private int fansCount;

    /**
     * 信用分
     */
    private int creditScore;

    /**
     * 商品数量
     */
    private int goodsCount;

    /**
     * 动态数量
     */
    private int trendsCount;

    /**
     * 是否点赞 1是 0否
     */
    private Integer isLike;

    /**
     * 点赞数
     */
    private int likeCount;

    /**
     * 评论数
     */
    private int commentsCount;

    /**
     * 想要数
     */
    private Integer needCount;

}

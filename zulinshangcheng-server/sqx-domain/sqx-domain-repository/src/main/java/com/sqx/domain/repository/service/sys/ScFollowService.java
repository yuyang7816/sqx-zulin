package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.ScFollow;
import com.sqx.domain.repository.entity.sys.Goods;

/**
 * @autorh liyuan
 * @description sc_follow
 * @date 2021-08-12
 */
public interface ScFollowService extends IService<ScFollow> {


    Result updateFollow(ScFollow follow);


    Page<Goods> getFollowGoodsList(Long userId, Integer page, Integer limit, ScFollow follow);
    int getMyFollowCount(Long userId);

    int getMyFollowGoodsCount(Long userId);

    /**
     * 查看我的足迹列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result selectMyFootprintList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 查看我关注的用户列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    Result getFollowList(Long userId, Integer page, Integer limit);

    /**
     * 查看我的粉丝列表,并且是否已经互相关注
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    Result getFanList(Long userId, Integer page, Integer limit);
}

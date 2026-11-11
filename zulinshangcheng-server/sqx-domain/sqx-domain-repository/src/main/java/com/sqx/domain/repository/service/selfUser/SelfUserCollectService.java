package com.sqx.domain.repository.service.selfUser;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfUserCollect;

public interface SelfUserCollectService {
    //列表
    Result findAll(Integer page, Integer size, Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfUserCollect entity);

    //修改
    Result updateBody(SelfUserCollect entity);

    //判断是否收藏
    Result check(Long goodsId, Long userId);

}

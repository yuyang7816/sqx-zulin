package com.sqx.domain.repository.service.selfUser;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfUserBrowse;

public interface SelfUserBrowseService {
    //列表
    Result findAll(Integer page, Integer size, Long userId, String createTime);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfUserBrowse entity);

    //修改
    Result updateBody(SelfUserBrowse entity);

}

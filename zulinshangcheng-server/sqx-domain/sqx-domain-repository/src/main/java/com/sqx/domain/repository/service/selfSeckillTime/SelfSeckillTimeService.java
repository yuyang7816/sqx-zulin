package com.sqx.domain.repository.service.selfSeckillTime;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfSecKillTime;

public interface SelfSeckillTimeService {
    //管理后台列表
    Result findAll(Integer page, Integer size);

    //用户端列表
    Result allList();

    //所有时段数据封装
    Result result();

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfSecKillTime entity);

    //修改
    Result updateBody(SelfSecKillTime entity);

}

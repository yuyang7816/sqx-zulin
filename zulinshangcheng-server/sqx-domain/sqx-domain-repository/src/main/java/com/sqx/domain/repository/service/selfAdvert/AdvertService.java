package com.sqx.domain.repository.service.selfAdvert;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Adverti;

public interface AdvertService {
    //列表
    Result findAll(Integer type);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(Adverti entity);

    //修改
    Result updateBody(Adverti entity);

}

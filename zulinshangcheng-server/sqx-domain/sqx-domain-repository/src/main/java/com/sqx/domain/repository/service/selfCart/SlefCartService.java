package com.sqx.domain.repository.service.selfCart;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCart;

public interface SlefCartService {
    //列表
    Result findAll(Integer page, Integer size, Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //批量删除
    Result deleteAll(String ids);

    //添加
    Result saveBody(SelfCart entity);

    //修改
    Result updateBody(SelfCart entity);

    //更改数量
    Result changeNumber(Long id, Integer number);

}

package com.sqx.domain.repository.service.selfGroup;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGroup;

public interface SelfGroupService {
    //管理后台列表
    Result findAll(Integer page, Integer size, String title);

    //用户端列表
    Result userList(Integer page, Integer size, String sort);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //上下架
    Result updateStatus(Long id);

    //添加
    Result saveBody(SelfGroup entity);

    //修改
    Result updateBody(SelfGroup entity);

    //多规格生成Sku
    Result isFormatSku(Long goodsId);

    //单规格生成sku
    Result onlyFormatSku(Long goodsId);

    //回显拼团sku
    Result showSku(Long groupId);


}

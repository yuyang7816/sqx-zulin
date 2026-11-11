package com.sqx.domain.repository.service.selfSeckill;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfSecKill;

public interface SelfSeckillService {
    //管理后台列表
    Result findAll(Integer page, Integer size, String title);

    //用户端列表
    Result userList(Integer page, Integer size, Long secKillTimeId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //上下架
    Result updateStatus(Long id);

    //添加
    Result saveBody(SelfSecKill entity);

    //修改
    Result updateBody(SelfSecKill entity);

    //多规格生成Sku
    Result isFormatSku(Long goodsId);

    //单规格生成sku
    Result onlyFormatSku(Long goodsId);

    //回显拼团sku
    Result showSku(Long secKillId);


}

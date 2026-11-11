package com.sqx.domain.repository.service.selfGoodsRule;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsRule;

public interface SelfGoodsRuleService {
    //后台管理商品列表
    Result findAll(Integer page, Integer size);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfGoodsRule entity);

    //修改
    Result updateBody(SelfGoodsRule entity);

    //所有商品规格模板
    Result info();

}

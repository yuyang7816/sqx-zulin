package com.sqx.domain.repository.service.selfGoodsBrand;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsBrand;

public interface SelfGoodsBrandService {
    //后台管理商品列表
    Result findAll(Integer page, Integer size);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfGoodsBrand entity);

    //修改
    Result updateBody(SelfGoodsBrand entity);

    //所有商品规格模板
    Result result();

}

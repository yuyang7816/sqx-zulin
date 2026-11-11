package com.sqx.domain.repository.service.selfGoodsType;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsType;

public interface GoodsTypeService {
    //列表
    Result findAll(String name);

    //数据封装
    Result findAllResult();

    //一级目录数据封装
    Result findAllResult1();

    // 二级目录数据封装
    Result findAllResult2(Long typeId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(GoodsType entity);

    //修改
    Result updateBody(GoodsType entity);

}

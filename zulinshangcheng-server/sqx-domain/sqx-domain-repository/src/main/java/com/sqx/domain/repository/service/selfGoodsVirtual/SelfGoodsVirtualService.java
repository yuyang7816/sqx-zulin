package com.sqx.domain.repository.service.selfGoodsVirtual;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsVirtual;

public interface SelfGoodsVirtualService {
    //列表
    Result findAll(Integer page, Integer size, Long goodsId, String content);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfGoodsVirtual entity);

    //修改
    Result updateBody(SelfGoodsVirtual entity);

    /**
     * 发送虚拟商品
     * @param goodsId
     * @return
     */
    SelfGoodsVirtual sendGoods(Long goodsId);

}

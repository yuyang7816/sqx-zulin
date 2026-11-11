package com.sqx.domain.repository.service.selfGoodsAttr;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsAttr;

import java.util.List;

public interface SelfGoodsAttrService {
    //添加
    Result saveBody(SelfGoodsAttr entity);

    //修改
    Result updateBody(SelfGoodsAttr entity);

    //根据商品id查询
    List<SelfGoodsAttr> findByGoodsId(Long goodsId);

    //删除所有attr和value
   void deleteAttrAndValue(Long goodsId);

}
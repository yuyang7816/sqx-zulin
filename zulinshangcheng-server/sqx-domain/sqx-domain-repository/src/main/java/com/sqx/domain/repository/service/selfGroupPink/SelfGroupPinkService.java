package com.sqx.domain.repository.service.selfGroupPink;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.SelfGroupPink;

public interface SelfGroupPinkService {
    //管理后台列表
    Result findAll(Integer page, Integer size, Integer status);

    //查询
    Result findOne(Long id);

    //查询可拼的团
    Result findByGroupId(Long groupId);

    /**
     * 发起拼团
     */
    Result createPink(Orders o);

    /**
     * 加入拼团
     */
    Result joinPink(Orders o);


    //判断拼团库存是否大于购买数量
    boolean checkPinkStock(Long skuId, Integer paNumber);

    //判断秒杀库存是否大于购买数量
    boolean checkSkillStock(Long skuId, Integer paNumber);

    //修改
    Result updateBody(SelfGroupPink entity);

}

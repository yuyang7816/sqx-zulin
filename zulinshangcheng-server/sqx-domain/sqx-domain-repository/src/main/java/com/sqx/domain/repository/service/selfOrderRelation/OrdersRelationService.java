package com.sqx.domain.repository.service.selfOrderRelation;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfOrderRelation;

import java.util.List;

public interface OrdersRelationService {
    //列表
    Result findAll(Integer page, Integer size, Long userId, String phone, Integer status, Integer moneyFrom);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfOrderRelation entity);

    //修改
    Result updateBody(SelfOrderRelation entity);

    //根据订单id查询分销记录
    List<SelfOrderRelation> findByOrdersId(Long OrdersId);

    //查询所有未结账的记录
    List<SelfOrderRelation> findAllByStatus();

    //执行零钱入账
    void addMoney(SelfOrderRelation entity);

    //我的佣金统计
    Result statistaical(Long userId);

    //团队佣金统计
    Result tuanStatistaical(Long userId);

    //团队总收益
    Result tuanSum(Long userId);

    //团队
    Result getTeam(Long userId, Integer type);
}

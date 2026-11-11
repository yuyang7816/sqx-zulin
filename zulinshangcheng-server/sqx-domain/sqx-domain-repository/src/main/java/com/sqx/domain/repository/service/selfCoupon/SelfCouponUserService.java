package com.sqx.domain.repository.service.selfCoupon;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;

public interface SelfCouponUserService {
    //列表
    Result findAll(Integer page, Integer size, String couponName);

    //用户端优惠券列表
    Result userList(Integer page, Integer size, Long userId, String goodsId, Integer type,Integer status);

    Result useList(Long userId, String goodsId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfCouponUser entity);

    //修改
    Result updateBody(SelfCouponUser entity);

}

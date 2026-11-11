package com.sqx.domain.repository.service.selfCoupon;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCouponIssue;

public interface SelfCouponIssueService {
    //列表
    Result findAll(Integer page, Integer size);

    //用户端
    Result useList(String goodsId, Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfCouponIssue entity);

    //修改
    Result updateBody(SelfCouponIssue entity);

}

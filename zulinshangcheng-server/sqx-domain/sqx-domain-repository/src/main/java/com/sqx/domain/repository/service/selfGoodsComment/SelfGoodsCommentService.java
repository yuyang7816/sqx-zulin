package com.sqx.domain.repository.service.selfGoodsComment;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsComment;

public interface SelfGoodsCommentService {
    //列表
    Result findAll(Integer page, Integer size, Long goodsId, Integer scoreType, String title, String userName, String phone, String content);

    //统计
    Result count(Long goodsId);

    //用户端我的评论
    Result userList(Integer page, Integer size, Long userId);

    //查询订单评价
    Result findByOrderId(Long orderId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfGoodsComment entity);

    //修改
    Result updateBody(SelfGoodsComment entity);

    //回复评论
    Result reply(Long commentId, String reply);

}

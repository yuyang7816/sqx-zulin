package com.sqx.domain.repository.service.sys;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.domain.repository.entity.app.UserEntity;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */
public interface IUserService extends IService<UserEntity> {

    void orderCancel(Long userId, String orderNum, String time);

    void  pushToSingle(String title, String content,String clientId);

    void orderExpress(Long userId, String orderNum, String time, String expressName, String expressNumber, String address);

    //订单支付消息
    void orderPay(Long userId, String orderNum, String payMoney, String createTime);

    //订单退款
    void orderRefund(Long userId, String payMoney,String ordersNum);
}

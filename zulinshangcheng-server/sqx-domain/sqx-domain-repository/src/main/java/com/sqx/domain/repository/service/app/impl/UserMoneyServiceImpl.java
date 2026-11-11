package com.sqx.domain.repository.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoney;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDetailsDao;
import com.sqx.domain.repository.service.app.UserMoneyService;
import com.sqx.domain.repository.service.pay.AliPayService;
import com.sqx.domain.repository.service.pay.WxService;
import com.sqx.domain.repository.service.sys.TbOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private TbOrdersService tbOrdersService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private UserDao userDao;


    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }


    @Override
    public IPage<UserMoneyDetails> balanceDetailed(Long page, Long limit, UserMoneyDetails userMoneyDetails) {
        Page<UserMoneyDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        return userMoneyDetailsDao.selectPage(pages, new QueryWrapper<>(userMoneyDetails).orderByDesc("create_time"));
    }

    @Override
    public Result rechargeMoney(Long userId, BigDecimal money, Integer classify) throws Exception {
        if (classify == 1 || classify == 2 || classify == 3) {
            //微信
            return wxService.payMoney(userId, money, null, classify,1);
        } else if (classify == 4 || classify == 5) {
            //支付宝支付
            return aliPayService.payMoney(userId, money, null, classify,1);
        } else {
            return Result.failed("支付方式错误!");
        }
    }

    @Override
    public Result moneyPayIndent(Long userId, Long orderId) {
        TbOrders order = tbOrdersService.getById(orderId);
        if (order != null) {
            if (order.getPayStatus() == 0) {
                UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
                UserEntity userEntity = userDao.selectById(userId);
                if (userEntity != null && userEntity.getBalance().compareTo(order.getPayMoney()) > 0) {
                    userEntity.setBalance(userEntity.getBalance().subtract(order.getPayMoney()));
                    userDao.updateById(userEntity);
                } else {
                    return Result.failed("余额不足，请充值");
                }
                if (userMoney.getMoney().compareTo(order.getPayMoney()) >= 0) {
                    userMoneyDao.updateMayMoney(2, order.getUserId(), order.getPayMoney().doubleValue());
                    order.setPayStatus(1);
                    order.setPayWay(6);
                    order.setPayTime(new Date());
                    tbOrdersService.updateById(order);
//                    tbOrdersService.setCommission(order.getOrderId(), 1);
//                    tbOrdersService.indentPayCallback(indentId, 0, order.getIndentMoney());
                    return Result.success();
                } else {
                    return Result.failed("余额不足，请充值");
                }
            } else {
                return Result.failed("该订单状态已变更，请刷新");
            }
        }
        return Result.failed("该订单不存在");

    }


}

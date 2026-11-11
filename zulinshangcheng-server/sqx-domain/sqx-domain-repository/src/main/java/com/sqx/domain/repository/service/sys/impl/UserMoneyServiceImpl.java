//package com.sqx.domain.repository.service.sys.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.app.TbOrders;
//import com.sqx.domain.repository.entity.app.UserMoney;
//import com.sqx.domain.repository.entity.app.UserMoneyDetails;
//import com.sqx.domain.repository.mapper.app.UserMoneyDao;
//import com.sqx.domain.repository.mapper.app.UserMoneyDetailsDao;
//import com.sqx.domain.repository.service.pay.AliPayService;
//import com.sqx.domain.repository.service.pay.WxService;
//import com.sqx.domain.repository.service.sys.TbOrdersService;
//import com.sqx.domain.repository.service.sys.UserMoneyService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//@Service
//@Slf4j
//public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
//    @Autowired
//    private UserMoneyDetailsDao userMoneyDetailsDao;
//    @Autowired
//    private WxService wxService;
//    @Autowired
//    private AliPayService aliPayService;
//    @Autowired
//    private TbOrdersService ordersService;
//
//    @Override
//    public void updateMoney(int i, Long userId, double money) {
//        baseMapper.updateMayMoney(i, userId, money);
//    }
//
//    @Override
//    public UserMoney selectUserMoneyByUserId(Long userId) {
//        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
//    }
//
//    @Override
//    public Double selectMyProfit(Long userId) {
//
//        return userMoneyDetailsDao.selectMyProfit(userId);
//    }
//
//
//    @Override
//    public IPage<UserMoneyDetails> balanceDetailed(Long page, Long limit, UserMoneyDetails userMoneyDetails) {
//        Page<UserMoneyDetails> pages;
//        if (page != null && limit != null) {
//            pages = new Page<>(page, limit);
//        } else {
//            pages = new Page<>();
//        }
//        return userMoneyDetailsDao.selectPage(pages, new QueryWrapper<>(userMoneyDetails).orderByDesc("create_time"));
//    }
//
//    @Override
//    public Result rechargeMoney(Long userId, BigDecimal money, Integer classify) throws Exception {
//        if (classify == 1 || classify == 2 || classify == 3) {
//            //微信
//            return wxService.payMoney(userId, money, null, classify);
//        } else if (classify == 4 || classify == 5) {
//            //支付宝支付
//            return aliPayService.payMoney(userId, money, null, classify);
//        } else {
//            return Result.failed("支付方式错误!");
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result moneyPayIndent(Long userId, Long orderId) {
//        TbOrders orders = ordersService.getById(orderId);
//        if (orders == null) {
//            return Result.failed("订单不存在");
//        }
//        if (orders.getPayStatus() != 0) {
//            return Result.failed("订单状态已变更,请刷新后重试");
//        }
//
//        UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
//        if (userMoney.getMoney().compareTo(orders.getPayMoney()) >= 0) {
//            baseMapper.updateMayMoney(2, userId, orders.getPayMoney().doubleValue());
//            return ordersService.payOrderBack(orderId, orders.getPayMoney(), 0);
//        } else {
//            return Result.failed("零钱余额不足");
//        }
//
//    }
//
//
//}

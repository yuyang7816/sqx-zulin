package com.sqx.domain.repository.service.pay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDetailsDao;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.pay.PayDetailsRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.pay.AliPayService;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import com.sqx.domain.repository.service.pay.WxService;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.utils.SenInfoCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 充值记录
 */
@Service
public class PayDetailsServiceImpl extends ServiceImpl<PayDetailsDao, PayDetails> implements PayDetailsService {

    /**
     * 充值记录
     */
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private PayDetailsRepository payDetailsRepository;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserService userServices;

//    @Override
//    public PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state) {
//        Page<Map<String, Object>> pages = new Page<>(page, limit);
//        if (state != null && state == -1) {
//            state = null;
//        }
//        return new PageUtils(payDetailsDao.selectPayDetails(pages, startTime, endTime, userId, state));
//    }

    @Override
    public PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        if (state != null && state == -1) {
            state = null;
        }
        return new PageUtils(payDetailsDao.selectPayDetails(pages, startTime, endTime, userId, state));
    }

    @Override
    public Double selectSumPay(String createTime, String endTime, Long userId) {
        if (userId == null || userId == -1) {
            return payDetailsDao.selectSumPay(createTime, endTime, null);
        }
        return payDetailsDao.selectSumPay(createTime, endTime, userId);
    }


    @Override
    public Double selectSumMember(String time, Integer flag) {
        return payDetailsDao.selectSumMember(time, flag);
    }

    @Override
    public Double selectSumPayByState(String time, Integer flag, Integer state) {
        return payDetailsDao.selectSumPayByState(time, flag, state);
    }

    @Override
    public Double selectSumPayByClassify(String time, Integer flag, Integer classify, Integer payClassify) {
        return payDetailsDao.selectSumPayByClassify(time, flag, classify, payClassify);
    }

    @Override
    public Double instantselectSumPay(String date, Long userId) {
        return payDetailsDao.instantselectSumPay(date, userId);
    }

    @Override
    public HashMap<String, Object> payMessage(String date, Integer type) {

        HashMap<String, Object> hashMap = new HashMap<>();
        //总金额
        BigDecimal allMoney = payDetailsDao.payMessage(date, type, null);
        //微信app支付
        BigDecimal appMoney = payDetailsDao.payMessage(date, type, 1);
        //微信公众号支付
        BigDecimal wxRwmMoney = payDetailsDao.payMessage(date, type, 2);
        //微信小程序支付
        BigDecimal wxAppMoney = payDetailsDao.payMessage(date, type, 3);
        //支付宝app支付
        BigDecimal zfbMoney = payDetailsDao.payMessage(date, type, 4);
        //支付宝H5支付
        BigDecimal zfbH5Money = payDetailsDao.payMessage(date, type, 5);
        //零钱支付
        BigDecimal lqMoney = payDetailsDao.payMessage(date, type, 6);
        //管理后台充值支付
        BigDecimal sysMoney = payDetailsDao.payMessage(date, type, 8);
        // 已退款金额
        BigDecimal refundMoney = userMoneyDetailsDao.payMessageByRefund(date, type);

        hashMap.put("allMoney", allMoney);
        hashMap.put("appMoney", appMoney);
        hashMap.put("wxRwmMoney", wxRwmMoney);
        hashMap.put("wxAppMoney", wxAppMoney);
        hashMap.put("zfbMoney", zfbMoney);
        hashMap.put("zfbH5Money", zfbH5Money);
        hashMap.put("lqMoney", lqMoney);
        hashMap.put("sysMoney", sysMoney);
        hashMap.put("refundMoney", refundMoney);
        return hashMap;
    }


    @Override
    public Result refundPayDetails(Long orderId, BigDecimal money, String remark) {
        Orders orders = ordersDao.selectById(orderId);
        if (orders != null) {
            PayDetails payDetails = baseMapper.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNum()));
//            if (payDetails.getClassify() == 1 || payDetails.getClassify() == 2 || payDetails.getClassify() == 3) {
//                if (wxService.refund(payDetails.getOrderId(), money)) {
//                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
//                    userMoneyDetails.setUserId(orders.getUserId());
//                    userMoneyDetails.setTitle("押金退款成功：" + orders.getOrderNum());
//                    if (StringUtils.isNotBlank(remark)) {
//                        userMoneyDetails.setContent("退款金额：" + money + "，退款审核说明：" + remark);
//                    } else {
//                        userMoneyDetails.setContent("退款金额：" + money);
//                    }
//                    userMoneyDetails.setType(1);
//                    userMoneyDetails.setClassify(7);
//                    userMoneyDetails.setState(2);
//                    userMoneyDetails.setMoney(money);
//                    userMoneyDetails.setRelationId(orderId);
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
//                    userMoneyDetailsDao.insert(userMoneyDetails);
//                    MessageInfo messageInfo = new MessageInfo();
//                    messageInfo.setTitle("押金退款成功：" + orders.getOrderNum());
//                    if (StringUtils.isNotBlank(remark)) {
//                        messageInfo.setContent("退款金额：" + money + "，退款审核说明：" + remark);
//                    } else {
//                        messageInfo.setContent("退款金额：" + money);
//                    }
//                    messageInfo.setUserId(String.valueOf(orders.getUserId()));
//                    messageInfo.setUserName(userDao.selectById(orders.getUserId()).getUserName());
//                    messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
//                    messageInfo.setIsSee("0");
//                    messageInfo.setState("4");
//                    messageService.saveBody(messageInfo);
//                    orders.setStatus(6);
//                    orders.setFinishTime(simpleDateFormat.format(new Date()));
//                    ordersDao.updateByOrderId(orders);
//                    UserEntity user = userDao.selectById(orders.getUserId());
//                    if (user != null && user.getOpenId() != null) {
//                        List<String> msgList = new ArrayList<>();
//                        msgList.add("您的订单已退还押金：" + orders.getOrderNum());
//                        msgList.add("您的退款押金金额：" + money);
//                        msgList.add("订单押金金额已原路返回,请注意查收！");
//                        String commonInfo = commonInfoService.findOne(236).getValue();
//                        SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 2);
//                    }
//                    if (user != null && user.getClientid() != null) {
//                        userServices.pushToSingle("您的订单押金已退还", "订单号：" + orders.getOrderNum(), user.getClientid());
//                    }
//                    return Result.success("退款成功");
//                } else {
//                    return Result.failed("退款异常,请重试");
//                }
//            } else if (payDetails.getClassify() == 4 || payDetails.getClassify() == 5) {
//                if (aliPayService.aliRefund(payDetails.getOrderId(), money)) {
//                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
//                    userMoneyDetails.setUserId(orders.getUserId());
//                    userMoneyDetails.setTitle("押金退款成功：" + orders.getOrderNum());
//                    if (StringUtils.isNotBlank(remark)) {
//                        userMoneyDetails.setContent("退款金额：" + money + "，退款审核说明：" + remark);
//                    } else {
//                        userMoneyDetails.setContent("退款金额：" + money);
//                    }
//                    userMoneyDetails.setType(1);
//                    userMoneyDetails.setClassify(7);
//                    userMoneyDetails.setState(2);
//                    userMoneyDetails.setMoney(money);
//                    userMoneyDetails.setRelationId(orderId);
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
//                    userMoneyDetailsDao.insert(userMoneyDetails);
//                    MessageInfo messageInfo = new MessageInfo();
//                    messageInfo.setTitle("押金退款成功：" + orders.getOrderNum());
//                    if (StringUtils.isNotBlank(remark)) {
//                        messageInfo.setContent("退款金额：" + money + "，退款审核说明：" + remark);
//                    } else {
//                        messageInfo.setContent("退款金额：" + money);
//                    }
//                    messageInfo.setUserId(String.valueOf(orders.getUserId()));
//                    messageInfo.setUserName(userDao.selectById(orders.getUserId()).getUserName());
//                    messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
//                    messageInfo.setIsSee("0");
//                    messageInfo.setState("4");
//                    messageService.saveBody(messageInfo);
//                    orders.setStatus(6);
//                    orders.setFinishTime(simpleDateFormat.format(new Date()));
//                    ordersDao.updateByOrderId(orders);
//                    UserEntity user = userDao.selectById(orders.getUserId());
//                    if (user != null && user.getOpenId() != null) {
//                        List<String> msgList = new ArrayList<>();
//                        msgList.add("您的订单已退还押金：" + orders.getOrderNum());
//                        msgList.add("您的退款押金金额：" + money);
//                        msgList.add("订单押金金额已原路返回,请注意查收！");
//                        String commonInfo = commonInfoService.findOne(236).getValue();
//                        SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 2);
//                    }
//                    if (user != null && user.getClientid() != null) {
//                        userServices.pushToSingle("您的订单押金已退还", "订单号：" + orders.getOrderNum(), user.getClientid());
//                    }
//                    return Result.success("退款成功");
//                } else {
//                    return Result.failed("退款异常,请重试");
//                }
//            } else {
                UserEntity userEntity = userDao.selectById(orders.getUserId());
                if (userEntity != null) {
                    userEntity.setBalance(userEntity.getBalance().add(money));
                    userDao.updateById(userEntity);
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setUserId(orders.getUserId());
                    userMoneyDetails.setTitle("押金退款成功：" + orders.getOrderNum());
                    if (StringUtils.isNotBlank(remark)) {
                        userMoneyDetails.setContent("退还押金：" + money + "，退还押金审核说明：" + remark);
                    } else {
                        userMoneyDetails.setContent("退还押金：" + money);
                    }
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setClassify(7);
                    userMoneyDetails.setState(2);
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setRelationId(orderId);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                    userMoneyDetailsDao.insert(userMoneyDetails);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setTitle("押金退款成功：" + orders.getOrderNum());
                    if (StringUtils.isNotBlank(remark)) {
                        messageInfo.setContent("退还押金：" + money + "，退还押金审核说明：" + remark);
                    } else {
                        messageInfo.setContent("退还押金：" + money);
                    }
                    messageInfo.setUserId(String.valueOf(orders.getUserId()));
                    messageInfo.setUserName(userDao.selectById(orders.getUserId()).getUserName());
                    messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
                    messageInfo.setIsSee("0");
                    messageInfo.setState("4");
                    messageService.saveBody(messageInfo);
                    orders.setStatus(6);
                    orders.setFinishTime(simpleDateFormat.format(new Date()));
                    ordersDao.updateByOrderId(orders);
                    UserEntity user = userDao.selectById(orders.getUserId());
                    if (user != null && user.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("您的订单已退还押金：" + orders.getOrderNum());
                        msgList.add("您的退款押金金额：" + money);
                        msgList.add("订单押金金额已退回钱包,请注意查收！");
                        String commonInfo = commonInfoService.findOne(236).getValue();
                        SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 2);
                    }
                    if (user != null && user.getClientid() != null) {
                        userServices.pushToSingle("您的订单押金已退还", "订单号：" + orders.getOrderNum(), user.getClientid());
                    }
                    return Result.success("退款成功");
                } else {
                    return Result.failed("未找到该用户");
                }
            }
//        }
        return Result.failed("未找到该订单");
    }
}

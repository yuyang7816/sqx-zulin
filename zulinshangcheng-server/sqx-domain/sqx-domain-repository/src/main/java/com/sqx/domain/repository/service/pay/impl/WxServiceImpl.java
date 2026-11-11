package com.sqx.domain.repository.service.pay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.HttpClientUtil;
import com.sqx.common.core.utils.MD5Util;
import com.sqx.domain.repository.entity.app.*;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.app.AddressDao;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDao;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponIssueMapper;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponUserMapper;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGroup.SelfGroupJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.ContinuingOrderDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersRepository;
import com.sqx.domain.repository.mapper.selfSeckill.SelfSeckillJpaRepository;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.mapper.sys.LeaseDaysDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.pay.WxService;
import com.sqx.domain.repository.service.pay.config.AmountCalUtils;
import com.sqx.domain.repository.service.pay.config.WXConfig;
import com.sqx.domain.repository.service.pay.config.WXConfigUtil;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.TbCouponService;
import com.sqx.domain.repository.service.sys.TbCouponUserService;
import com.sqx.domain.repository.service.vipDetails.VipDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sqx.common.core.utils.wx.WxPayUtils.getGeneralOrder;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {
    private static final String SPBILL_CREATE_IP = "127.0.0.1";
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_NATIVE = "NATIVE";
    private static final String TRADE_TYPE_JSAPI = "JSAPI";

    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private VipDetailsService vipDetailsService;

    @Autowired
    private UserMoneyDao userMoneyDao;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TbCouponService couponService;

    @Autowired
    private TbCouponUserService couponUserService;
    @Autowired
    private ContinuingOrderDao continuingOrderDao;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private LeaseDaysDao leaseDaysDao;
    @Autowired
    private SelfGroupJpaRepository selfGroupJpaRepository;
    @Autowired
    private SelfSeckillJpaRepository selfSeckillJpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;
    @Autowired
    private SelfCouponUserMapper selfCouponUserMapper;
    @Autowired
    private SelfCouponIssueMapper selfCouponIssueMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private GoodsDao goodsDao;

    private static final String Wap = "MWEB";


    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param type       类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer type, Long userId, String outTradeNo) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (type == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApi";
        } else if (type == 2) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMp";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notify";
        }
        String currentTimeMillis = (System.currentTimeMillis() / 1000) + "";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        //微信商户key
        config.setKey(commonInfoService.findOne(75).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        data.put("trade_type", type == 1 ? TRADE_TYPE_APP : TRADE_TYPE_JSAPI);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if (type == 3 || type == 2) {
            UserEntity userEntity = userService.queryByUserId(userId);
            if (type == 3) {
                if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                    data.put("openid", userEntity.getOpenId());
                }
            } else {
                data.put("openid", userEntity.getWxOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            if (type == 1) {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
                return Result.success(param);
            } else {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
                return Result.success(param);
            }
        }
        return Result.failed("获取订单失败");
    }

    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param classify   类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer classify, Long userId, String outTradeNo, HttpServletRequest request, Integer shop) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (classify == 3) {
            if (shop != null) {
                url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApiShop";
            } else {
                url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApi";
            }

        } else if (classify == 2 || classify == 4) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMp";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notify";
        }
        String currentTimeMillis = (System.currentTimeMillis() / 1000) + "";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if (classify == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (classify == 2 || classify == 4) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            if (shop != null) {
                config.setAppId(commonInfoService.findOne(239).getValue());
            } else {
                config.setAppId(commonInfoService.findOne(45).getValue());
            }

        }
        //微信商户key
        config.setKey(commonInfoService.findOne(75).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        if (classify == 1) {
            data.put("trade_type", TRADE_TYPE_APP);
        } else if (classify == 2) {
            data.put("trade_type", TRADE_TYPE_JSAPI);
        } else if (classify == 3) {
            data.put("trade_type", TRADE_TYPE_JSAPI);
        } else {
            data.put("trade_type", Wap);
            data.put("spbill_create_ip", HttpClientUtil.getIpAddress(request));
        }
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if (classify == 3 || classify == 2) {
            UserEntity userEntity = userService.queryByUserId(userId);
            if (classify == 3) {
                if (shop != null) {
//                    if (StringUtils.isNotBlank(userEntity.getShopOpenId())) {
//                        data.put("openid", userEntity.getShopOpenId());
//                    }
                } else {
                    if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                        data.put("openid", userEntity.getOpenId());
                    }
                }
            } else {
                data.put("openid", userEntity.getWxOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            Map<String, String> param = new HashMap<>();
            if (classify == 1) {
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
            } else if (classify == 2 || classify == 4 || classify == 3) {
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
            } else {
                param.put("mweb_url", response.get("mweb_url"));
            }
            return Result.success(param);
        }
        return Result.failed("获取订单失败");
    }

    @Override
    public String payBack(String resXml, Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    log.error("验证成功222！！！");
                    if (out_trade_no != null) {
                        log.error("订单号！！！" + out_trade_no);
                        reentrantReadWriteLock.writeLock().lock();
                        try {
                            PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                            if (payDetails != null) {
//                                payDetails.setState(1);
//                                payDetails.setPayTime(sdf.format(new Date()));
//                                payDetailsDao.updateById(payDetails);
                                payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                                if (payDetails.getState() == 0) {
                                    if (payDetails.getType() == 1) {
                                        //余额充值
                                        userMoneyDao.updateMayMoney(1, payDetails.getUserId(), payDetails.getMoney());
                                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                        userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                        userMoneyDetails.setUserId(payDetails.getUserId());
                                        userMoneyDetails.setContent("微信充值：" + payDetails.getMoney());
                                        userMoneyDetails.setTitle("账户充值");
                                        userMoneyDetails.setType(1);
                                        userMoneyDetails.setClassify(0);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                                        userMoneyDetailsService.save(userMoneyDetails);
                                    } else if (payDetails.getType() == 3) {
                                        vipDetailsService.callbackBusiness(payDetails.getOrderId());
                                    } else if (payDetails.getType() == 7) {
                                        couponUserService.businessCallback(2, payDetails.getUserId(), payDetails.getRelationId(), payDetails.getBuyNum(), BigDecimal.valueOf(payDetails.getMoney()));
                                    } else if (payDetails.getType() == 2) {
                                        //订单支付
                                        Orders tbOrders = ordersService.findByOrderNum(payDetails.getOrderId());
                                        ordersService.payOrderBack(tbOrders.getId(), tbOrders.getPayMoney(), payDetails.getClassify());
                                    } else if (payDetails.getType() == 4) {
                                        //补缴租金
                                        Orders tbOrders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("supplement_num", payDetails.getOrderId()));
                                        ordersService.supplementMoneyBack(tbOrders.getId(), tbOrders.getSupplementMoney(), payDetails.getClassify());
                                        // 续租
                                    }else if (payDetails.getType() == 5){
                                        Orders tbOrders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("re_order_num",payDetails.getOrderId()));
                                        ordersService.payOrderBack(tbOrders.getId(),  tbOrders.getPayMoney(), payDetails.getClassify());
                                        // 买断
                                    } else if (payDetails.getType() == 6) {
                                        Orders tbOrders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("buy_order_num",payDetails.getOrderId()));
                                        ordersService.payOrderBack(tbOrders.getId(), tbOrders.getPrice(), payDetails.getClassify());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("微信支付回调异常：" + e.getMessage(), e);
                        } finally {
                            reentrantReadWriteLock.writeLock().unlock();
                        }
                        log.error("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        log.error("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                    // 签名错误，如果数据里没有sign字段，也认为是签名错误
                    log.error("手机支付回调通知签名错误");
                    xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    return xmlBack;
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败" + e.getMessage(), e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    @Override
    public Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify, Integer type) throws Exception {
        String generalOrder = getGeneralOrder();
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        if (parentId != null) {
            payDetails.setParentId(parentId);
        }
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(type);
        payDetailsDao.insert(payDetails);

        return pay(money.doubleValue(), classify, userId, generalOrder);
    }

    @Override
    public Result payOrder(Long userId, Long orderId, BigDecimal money,Integer classify, Integer orderType) throws Exception {
        Orders orders = ordersDao.selectById(orderId);
        if (orders == null) {
            return Result.failed("订单不存在");
        }
//        if (orders.getStatus() != 0) {
//            return Result.failed("订单状态已变更,请刷新后重试");
//        }
        PayDetails payDetails;
        if (orderType == 5) {
            // 首次下单
            if (orders.getStatus() == 0) {
                payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNum()));
                if (payDetails == null) {
                    payDetails = new PayDetails();
                    payDetails.setState(0);
                    payDetails.setCreateTime(sdf.format(new Date()));
                    payDetails.setOrderId(orders.getOrderNum());
                    payDetails.setUserId(userId);
                    payDetails.setMoney(money.doubleValue());
                    payDetails.setClassify(classify);
                    payDetails.setType(2);
                    payDetailsDao.insert(payDetails);
                } else {
                    payDetails.setClassify(classify);
                    payDetailsDao.updateById(payDetails);
                }
                return pay(payDetails.getMoney(), classify, userId, payDetails.getOrderId());
                // 继续租赁订单
            } else if (orders.getStatus() == 4) {
                orders.setPayWay(classify);
                orders.setReOrderNum(getGeneralOrder());
                ordersDao.updateById(orders);
                payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getReOrderNum()));
                if (payDetails == null) {
                    payDetails = new PayDetails();
                    payDetails.setState(0);
                    payDetails.setCreateTime(sdf.format(new Date()));
                    payDetails.setOrderId(orders.getReOrderNum());
                    payDetails.setUserId(userId);
                    payDetails.setMoney(money.doubleValue());
                    payDetails.setClassify(classify);
                    payDetails.setType(5);
                    payDetailsDao.insert(payDetails);
                } else {
                    payDetails.setClassify(classify);
                    payDetailsDao.updateById(payDetails);
                }
                return pay(payDetails.getMoney(), classify, userId, payDetails.getOrderId());

            }
            // 买断订单
        } else if (orderType == 6) {
            orders.setPayWay(classify);
            orders.setBuyOrderNum(getGeneralOrder());
            ordersDao.updateById(orders);
            payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getBuyOrderNum()));
            if (payDetails == null) {
                payDetails = new PayDetails();
                payDetails.setState(0);
                payDetails.setCreateTime(sdf.format(new Date()));
                payDetails.setOrderId(orders.getBuyOrderNum());
                payDetails.setUserId(userId);
                payDetails.setMoney(money.doubleValue());
                payDetails.setClassify(classify);
                payDetails.setType(6);
                payDetailsDao.insert(payDetails);
            } else {
                payDetails.setClassify(classify);
                payDetailsDao.updateById(payDetails);
            }
            return pay(payDetails.getMoney(), classify, userId, payDetails.getOrderId());
        }
        return Result.failed("订单状态已变更，请刷新后重试");
//        PayDetails payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNum()));
//        if (payDetails == null) {
//            payDetails = new PayDetails();
//            payDetails.setState(0);
//            payDetails.setCreateTime(sdf.format(new Date()));
//            payDetails.setOrderId(orders.getOrderNum());
//            payDetails.setUserId(userId);
//            payDetails.setMoney(orders.getPayMoney().doubleValue());
//            payDetails.setClassify(classify);
//            payDetails.setType(2);
//            payDetailsDao.insert(payDetails);
//        } else {
//            payDetails.setClassify(classify);
//            payDetailsDao.updateById(payDetails);
//        }
//        return pay(payDetails.getMoney(), classify, userId, payDetails.getOrderId());
    }

    @Override
    public Result paySupplement(Long userId, Long orderId, BigDecimal supplementMoney, Integer classify) throws Exception {
        Orders orders = ordersDao.selectById(orderId);
        if (orders == null) {
            return Result.failed("订单不存在");
        }
        if (orders.getSupplementStatus() != null && orders.getSupplementStatus() != 2) {
            return Result.failed("订单状态已变更,请刷新后重试");
        }
        orders.setSupplementNum(getGeneralOrder());
        orders.setSupplementStatus(2);
        orders.setSupplementMoney(supplementMoney);
        ordersDao.updateById(orders);
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(orders.getSupplementNum());
        payDetails.setUserId(userId);
        payDetails.setMoney(supplementMoney.doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(4);
        payDetails.setParentId(orders.getId());
        payDetails.setRemark("补缴租金：" + supplementMoney);
        payDetailsDao.insert(payDetails);
        return pay(payDetails.getMoney(), classify, userId, payDetails.getOrderId());
    }

    @Override
    public Result continuingLease(Long userId, Orders entity, Integer classify) {
        try {
            Orders entityOrders = ordersDao.selectById(entity.getId());
            if (entityOrders == null) {
                return ResultUtil.error(-1, "订单不存在");
            }
            entity.setUserId(userId);
            //校验下单金额
            Result check = this.checkOrderMoney(entity);
            if (check.getStatus() != 0) {
                return check;
            }
            if (entity.getOrderType() != 5 && entity.getOrderType() != 7) {
                return ResultUtil.error(-1, "订单类型错误");
            }
            //检测复购
            entityOrders.setIsRepeat(2);
            entityOrders.setIsExpress(entity.getIsExpress());
            List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                    .eq("order_id", entityOrders.getId()));
            if (continuingOrders.isEmpty()) {
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
                }
            } else {
                ContinuingOrder continuingOrder1 = continuingOrderDao.selectOne(new QueryWrapper<ContinuingOrder>()
                        .eq("order_id", entityOrders.getId())
                        .orderByDesc("sort")
                        .last("LIMIT 1"));
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(continuingOrder1 != null ? continuingOrder1.getSort() + 1 : 1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
                }
            }
            int count = ordersRepository.checkIsRepeat(entity.getUserId(), entity.getGoodsId());
            if (count > 0) {
                entityOrders.setIsRepeat(1);
            }
            BigDecimal money = entityOrders.getGrossRent();
            if (entity.getDayId() != null) {
                LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
                if (leaseDays != null) {
                    if (entityOrders.getReturnDate() != null) {
                        LocalDate leaseEndDate = entityOrders.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                        // 计算归还日期
                        LocalDate returnDate = leaseEndDate.plusDays(daysToAdd);
                        // 设置归还日期
                        entityOrders.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                        entityOrders.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
                    }
                    if (entity.getCouponMoney() != null) {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .subtract(entity.getCouponMoney())
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    } else {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    }
                }
            }
            entityOrders.setTotalContinueMoney(entityOrders.getGrossRent().add(money));
            entityOrders.setStatus(4);
            entityOrders.setContinueDate(new Date());
            entityOrders.setPayWay(classify);
            pay(entityOrders.getTotalContinueMoney().doubleValue(), classify, userId, entityOrders.getOrderNum());
            int save = ordersDao.updateByOrderId(entityOrders);
            if (save <= 0) {
                return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
            }
            return ResultUtil.success(entityOrders.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        }
        return ResultUtil.error(-200, "系统繁忙，请稍后再试！");
    }

    @Override
    public Result updateBody(Orders entity, Long userId, Integer classify) {
        try {
            if (entity.getOrderType() == 6) {
                UserEntity user = userDao.selectById(userId);
                Orders orders = ordersDao.selectById(entity.getId());
                if (orders != null) {
                    Goods goods = goodsDao.selectById(entity.getGoodsId());
                    if (user != null) {
                        if (!user.getUserId().equals(orders.getUserId())) {
                            return ResultUtil.error(-1, "您无权操作！");
                        }
                    }
                    if (goods != null) {
                        LocalDate now = LocalDate.now();
                        LocalDate returnDate = orders.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        if (returnDate.isBefore(now)) {
                            return ResultUtil.error(-1, "您已逾期，不能买断");
                        }
                        if (user != null && user.getIsVip() == 1) {
                            // 租期内买断补缴=买断价-支付的总租金-押金
                            // 买断价格 = (买断价格:会员价)-租赁价格-超出天数价格(-有优惠券减去优惠券价格)
                            if (entity.getCouponMoney() != null) {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit())
                                                .subtract(entity.getCouponMoney()));
                            } else {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(entity.getDeposit()));
                            }
                        } else {
                            // 买断价格 = (买断价格:非会员价)-租赁价格-超出天数价格(-有优惠券减去优惠券价格)
                            if (entity.getCouponMoney() != null) {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit())
                                                .subtract(entity.getCouponMoney()));
                            } else {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit()));
                            }
                        }
                    }
                }
                if (entity.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                    return ResultUtil.error(-1, "订单金额有误");
                }
                entity.setStatus(8);
                entity.setPurchaseTime(new Date());
                entity.setPayWay(classify);
                Address address = addressDao.selectById(entity.getAddressId());
                if (address != null) {
                    entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
                }
                pay(entity.getPrice().doubleValue(), classify, userId, entity.getOrderNum());
                int result = ordersDao.updateById(entity);
                if (result <= 0) {
                    return ResultUtil.error(-1, "操作失败");
                }
                return ResultUtil.success(entity.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        }
        return ResultUtil.error(-1, "订单类型有误");
    }


//    @Override
//    public Result payCoupon(Long userId, Long couponId, Integer buyNum, Integer payType, HttpServletRequest request) throws Exception {
//        TbCoupon tbCoupon = couponService.getById(couponId);
//        if (tbCoupon == null || tbCoupon.getIsEnable() == 0 || tbCoupon.getDeleteFlag() == 1) {
//            return Result.failed("优惠券暂未出售或不存在");
//        }
////        if (tbCoupon.getCouponType() != 2) {
////            return Result.error("当前优惠券不支持购买");
////        }
//        //查看当前用户已购买或领取数量
//        Integer num = couponUserService.count(new QueryWrapper<TbCouponUser>().eq("user_id", userId).eq("coupon_id", couponId));
//        if (tbCoupon.getMaxReceive() != 0) {
//            if ((tbCoupon.getMaxReceive() - num) <= buyNum) {
//                return Result.failed("当前可购买或领取的数量已到达上限");
//            }
//        }
//        BigDecimal totalPrice = tbCoupon.getBuyMoney().multiply(new BigDecimal(buyNum));
//        PayDetails payDetails = new PayDetails();
//        payDetails.setOrderId(getGeneralOrder());
//        payDetails.setState(0);
//        payDetails.setBuyNum(buyNum);
//        payDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        payDetails.setUserId(userId);
//        payDetails.setMoney(totalPrice.doubleValue());
//        payDetails.setClassify(payType);
//        payDetails.setType(7);
//        payDetails.setRelationId(couponId);
//        payDetailsDao.insert(payDetails);
//        return pay(payDetails.getMoney(), payType, userId, payDetails.getOrderId(), request, null);
//    }

    @Override
    public boolean refund(String ordersNo, BigDecimal refundMoney) {
        WXConfigUtil config = null;
        String h5Url = commonInfoService.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/" + h5Url + "/service/apiclient_cert.p12";
//        String filePath = "D:\\ideaproject\\server\\src\\main\\resources\\weixin\\apiclient_cert.p12";
        try {
            config = new WXConfigUtil(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        if (refundMoney == null) {
            refundMoney = BigDecimal.valueOf(payDetails.getMoney());
        }
        Integer payWay = payDetails.getClassify();
        //orders.getPayWay(); //1app微信 2微信公众号 3微信小程序
        switch (payWay) {
            case 1:
                commInfoId = 74;
                break; //appId
            case 2:
                commInfoId = 5;
                break; //公众号id
            case 3:
                commInfoId = 45;
                break; //小程序id
            default:
                return false;
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        data.put("out_trade_no", payDetails.getOrderId()); //订单号,支付单号一致
        data.put("out_refund_no", getGeneralOrder()); //退款单号，同一笔用不同的退款单号
        BigDecimal multiply = refundMoney.multiply(BigDecimal.valueOf(100));
        String fee = String.valueOf(multiply.intValue());
        String total = String.valueOf(BigDecimal.valueOf(payDetails.getMoney()).multiply(BigDecimal.valueOf(100)).intValue());
        data.put("total_fee", total); //1块等于微信支付传入100);
        data.put("refund_fee", fee); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("微信退款异常：" + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 校验下单金额
     *
     * @param entity
     * @return
     */
    public Result checkOrderMoney(Orders entity) {
        if (entity.getGroupId() != null) {
            SelfGroup selfGroup = selfGroupJpaRepository.findById(entity.getGroupId()).orElse(null);
            if (selfGroup == null || selfGroup.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        } else if (entity.getSecKillId() != null) {
            SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(entity.getSecKillId()).orElse(null);
            if (selfSecKill == null || selfSecKill.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        } else {
            Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
            if (g == null || g.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        }
        //校验优惠券
        if (entity.getCouponUserId() != null) {
            SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(entity.getCouponUserId());
            Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
//            if (!entity.getUserCouponsId().equals(g.getBrandId())) { //品牌id:前端传值有误，排除商品品牌id
            if (couponUser == null) {
                return ResultUtil.error(-1, "优惠券不存在");
            }
            SelfCouponIssue selfCouponIssue = selfCouponIssueMapper.selectByCouponIssueId(couponUser.getCouponIssueId());
            if (selfCouponIssue.getStatus() != 1) {
                return ResultUtil.error(-1, "优惠券未启用，暂不能使用");
            }
            if (selfCouponIssue.getType() == 2) {
                boolean flag = false;
                if (selfCouponIssue.getGoodsIds() != null) {
                    String[] goodsIds = selfCouponIssue.getGoodsIds().split(",");
                    for (String goodsId : goodsIds) {
                        if (goodsId.trim().equals(entity.getGoodsId().toString().trim())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                    }
                } else {
                    return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                }
            }
            if (Double.parseDouble(couponUser.getLessMoney()) != entity.getCouponMoney().doubleValue()) {
                return ResultUtil.error(-1, "优惠券金额有误");
            }
//            }
        }
        //校验支付金额：商品金额：商品价格*个数+邮费=总价；支付金额：总价-优惠金额(单商户)
        //预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费
//        if (entity.getDayId() != null){
//
//        }else if (entity.getDayId() == null && entity.get){
//            return ResultUtil.error(-1, "商品金额有误");
//        }
        Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
        if (g != null && g.getIsExpress() != null) {
            String[] split = g.getIsExpress().split(",");
            if (split.length > 0) {
                boolean condition1Met = false;
                boolean condition2Met = false;
                boolean condition3Met = false;
                for (String s : split) {
                    if ("1".equals(s.trim())) {
                        condition1Met = true;
                    } else if ("2".equals(s.trim())) {
                        condition2Met = true;
                    } else if ("3".equals(s.trim())) {
                        condition3Met = true;
                    }
                }
                if (condition2Met) {
                    entity.setPostagePrice(g.getPostagePrice() == null ? BigDecimal.ZERO : g.getPostagePrice());
                    Address address = addressDao.selectById(entity.getAddressId());
                    if (address != null) {
                        entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
                    }
                } else if (condition1Met || condition3Met) {
                    entity.setPostagePrice(BigDecimal.ZERO);
                }
            }
        }
//        if (g != null && ("1".equals(g.getIsExpress()) || "2".equals(g.getIsExpress()))) {
//            if (g.getPostagePrice() == null) {
//                entity.setPostagePrice(BigDecimal.ZERO);
//            } else {
//                entity.setPostagePrice(g.getPostagePrice());
//            }
//            Address address = addressDao.selectById(entity.getAddressId());
//            if (address != null) {
//                entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
//            }
//        } else if (g != null && "3".equals(g.getIsExpress())) {
//            entity.setPostagePrice(BigDecimal.ZERO);
//        }
        LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
        if (leaseDays == null) {
            return ResultUtil.error(-1, "该租赁天数不存在，请刷新后重试");
        }
        String dayName = leaseDays.getDayName();
        long days = Long.parseLong(dayName);
        UserEntity user = userDao.selectById(entity.getUserId());
        if (user != null) {
            BigDecimal couponMoney = null;
            // 如果是租赁商品
            if (entity.getOrderType() == 5) {
                if (entity.getCouponMoney() == null) {
                    entity.setCouponMoney(BigDecimal.ZERO);
                    couponMoney = BigDecimal.ZERO;
                } else {
                    entity.setCouponMoney(entity.getCouponMoney());
                    couponMoney = entity.getCouponMoney();
                }
                // 预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费 - 优惠券
                entity.setEstimateMoney(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getDeposit())
                                .add(entity.getPostagePrice())
                                .subtract(couponMoney));
                // 总租金 = 日租金 * 天数 * 购买的数量 + 运费
                entity.setGrossRent(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getPostagePrice()));
                // 如果是买断商品
            } else if (entity.getOrderType() == 6) {
                if (entity.getCouponMoney() == null) {
                    entity.setCouponMoney(BigDecimal.ZERO);
                    couponMoney = BigDecimal.ZERO;
                } else {
                    entity.setCouponMoney(entity.getCouponMoney());
                    couponMoney = entity.getCouponMoney();
                }
                UserEntity userEntity = userDao.selectById(entity.getUserId());
                if (userEntity != null) {
                    if (userEntity.getIsVip() == 1) {
                        if (g.getMemberPrice() == null) {
                            g.setMemberPrice(BigDecimal.ZERO);
                        }
                        entity.setEstimateMoney(g.getMemberPrice()
                                .multiply(new BigDecimal(entity.getNumber()))
                                .add(entity.getPostagePrice())
                                .subtract(couponMoney));
                    } else {
                        // 预估价格 = 商品价格 * 购买数量 + 邮费 - 优惠券
                        if (g.getPrice() == null) {
                            g.setPrice(BigDecimal.ZERO);
                        }
                        entity.setEstimateMoney(
                                g.getPrice()
                                        .multiply(new BigDecimal(entity.getNumber()))
                                        .add(entity.getPostagePrice())
                                        .subtract(couponMoney));
                    }
                }
            }
//        BigDecimal sumMoney = entity.getPrice().multiply(new BigDecimal(entity.getNumber()));
//        if (entity.getPostagePrice() != null){ //邮费不为空
//            sumMoney = sumMoney.add(entity.getPostagePrice());
//        }
            BigDecimal payMoney = entity.getEstimateMoney();
            entity.setPayMoney(payMoney);
//            if (entity.getCouponMoney() != null) { //优惠券金额不为空
//                payMoney = payMoney.subtract(entity.getCouponMoney());
//            }
            if (payMoney.compareTo(BigDecimal.ZERO) < 0) {
                return ResultUtil.error(-1, "订单金额有误");
            }
            entity.setPayMoney(payMoney);
            if (payMoney.doubleValue() != entity.getPayMoney().doubleValue()) {
                return ResultUtil.error(-1, "订单金额有误");
            }
        } else {
            return ResultUtil.error(-1, "该用户未登录");
        }
        return ResultUtil.success();
    }


}
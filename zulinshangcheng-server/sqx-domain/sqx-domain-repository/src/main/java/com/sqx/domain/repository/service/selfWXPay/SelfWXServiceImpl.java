package com.sqx.domain.repository.service.selfWXPay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.common.core.utils.HttpClientUtil;
import com.sqx.common.core.utils.MD5Util;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGroup.SelfGroupJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupPink.SelfGroupPinkJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.mapper.selfOrderRemind.SelfOrdersRemindJpaRepository;
import com.sqx.domain.repository.mapper.selfSeckill.SelfSeckillJpaRepository;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import com.sqx.domain.repository.service.pay.config.WXConfig;
import com.sqx.domain.repository.service.pay.config.WXConfigUtil;
import com.sqx.domain.repository.service.selfGoods.GoodsService;
import com.sqx.domain.repository.service.selfGoodsVirtual.SelfGoodsVirtualService;
import com.sqx.domain.repository.service.selfGroupPink.SelfGroupPinkService;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sqx.common.core.utils.wx.WxPayUtils.getGeneralOrder;

@Slf4j
@Service
public class SelfWXServiceImpl implements SelfWXService {
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_H5 = "JSAPI";
    private static final String Wap = "MWEB";


    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private SelfGroupPinkService groupPinkService;
    @Autowired
    private IUserService userService;
    @Autowired
    private SelfGroupPinkJpaRepository selfGroupPinkJpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;
    @Autowired
    private SelfGroupJpaRepository selfGroupJpaRepository;
    @Autowired
    private SelfSeckillJpaRepository selfSeckillJpaRepository;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    /**
     * 微信支付
     *
     * @param payNum 支付编号
     * @param type   类型 1App支付 2Web支付
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Map doUnifiedOrder(String payNum, Integer type, Long userId, BigDecimal payMoney, HttpServletRequest request) throws Exception {
        String url;
        if (type == 2 || type == 4) {
            url = commonRepository.findOne(19).getValue() + "/tao/pay/notify";
        } else {
            url = commonRepository.findOne(19).getValue() + "/tao/pay/notifys";
        }
        log.info("回调地址：" + url);
        try {
            WXConfig config = new WXConfig();
            if (type == 1) {
                config.setAppId(commonRepository.findOne(74).getValue());
            } else if (type == 2 || type == 4) {
                config.setAppId(commonRepository.findOne(5).getValue());
            } else {
                config.setAppId(commonRepository.findOne(45).getValue());
            }
            config.setKey(commonRepository.findOne(75).getValue());
            config.setMchId(commonRepository.findOne(76).getValue());
            String generateNonceStr = WXPayUtil.generateNonceStr();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            data.put("appid", config.getAppID());
            data.put("mch_id", config.getMchID());
            data.put("nonce_str", generateNonceStr);
            String body = Config.BASE_NAME;
            List<Orders> list = ordersService.findByPayNum(payNum);
            if (list.size() > 0) {
                body = list.get(0).getTitle();
            }
            //支付金额校验
            BigDecimal paySum = new BigDecimal("0.00");
            for (Orders o : list) {
                if (o.getGroupId() != null) {
                    SelfGroup selfGroup = selfGroupJpaRepository.findById(o.getGroupId()).orElse(null);
                    if (selfGroup == null || selfGroup.getStatus() != 1) {
                        throw new Exception("商品已被下架或移除，请选择其他商品！");
                    }
                } else if (o.getSecKillId() != null) {
                    SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(o.getSecKillId()).orElse(null);
                    if (selfSecKill == null || selfSecKill.getStatus() != 1) {
                        throw new Exception("商品已被下架或移除，请选择其他商品！");
                    }
                } else {
                    Goods g = goodsJpaRepository.findById(o.getGoodsId()).orElse(null);
                    if (g == null || g.getStatus() != 1) {
                        throw new Exception("商品已被下架或移除，请选择其他商品！");
                    }
                }
                paySum = paySum.add(o.getPayMoney());
                if (o.getStatus() != 1) {
                    throw new Exception("订单已经支付过了！");
                }
                if (o.getGroupPinkId() != null) {
                    SelfGroupPink selfGroupPink = selfGroupPinkJpaRepository.findById(o.getGroupPinkId()).orElse(null);
                    if (selfGroupPink.getStatus() == 2) {
                        throw new Exception("拼团已完成，无法参与！");
                    }
                }
            }
            log.info("支付金额，传入金额：" + payMoney + "计算金额：" + paySum);
            payMoney = paySum;
            data.put("body", body);
            //生成商户订单号，不可重复
            data.put("out_trade_no", payNum);
            data.put("total_fee", payMoney.multiply(new BigDecimal("100")).intValue() + ""); //1块等于微信支付传入100);
            data.put("notify_url", url); //异步通知地址（请注意必须是外网）
            if (type == 2) {
                data.put("openid", userJpaRepository.getOne(userId).getOpenId()); //公众号支付需要openId
            }
            if (type == 1) {
                data.put("trade_type", TRADE_TYPE_APP);
            } else if (type == 2) {
                data.put("trade_type", TRADE_TYPE_H5);
            } else if (type == 3) {
                data.put("trade_type", TRADE_TYPE_H5);
            } else {
                data.put("trade_type", Wap);
                data.put("spbill_create_ip", HttpClientUtil.getIpAddress(request));
            }            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("attach", "");
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);
            for (String key : response.keySet()) {
                log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
            }
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                Map<String, String> param = new HashMap<>();
                if (type == 1) {
                    param.put("appid", config.getAppID());
                    param.put("partnerid", response.get("mch_id"));
                    param.put("prepayid", response.get("prepay_id"));
                    param.put("package", "Sign=WXPay");
                    param.put("noncestr", WXPayUtil.generateNonceStr());
                    param.put("timestamp", System.currentTimeMillis() / 1000 + "");
                    param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));
                    param.put("outtradeno", payNum);
                    param.put("signType", "MD5");
                    return param;
                } else if (type == 2 || type == 3) {
                    param.put("appid", config.getAppID());
                    param.put("partnerid", response.get("mch_id"));
                    param.put("prepayid", response.get("prepay_id"));
                    param.put("noncestr", generateNonceStr);
                    String timestamp = System.currentTimeMillis() / 1000 + "";
                    param.put("timestamp", timestamp);
                    String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + timestamp + "" + "&key=" + config.getKey();
                    String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                    param.put("sign", sign);
                    param.put("outtradeno", payNum);
                    param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                    param.put("mweb_url", response.get("mweb_url"));
                    param.put("trade_type", response.get("trade_type"));
                    param.put("return_msg", response.get("return_msg"));
                    param.put("result_code", response.get("result_code"));
                    param.put("signType", "MD5");
                    return param;
                } else {
                    param.put("mweb_url", response.get("mweb_url"));
                    return param;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("下单失败");
        }
        throw new Exception("下单失败");
    }

    @Override
    public String payBack(String resXml) {
        WXConfigUtil config = null;
        String h5Url = commonRepository.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/" + h5Url + "/service/apiclient_cert.p12";
        try {
            config = new WXConfigUtil(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setAppId(commonRepository.findOne(5).getValue());
        config.setKey(commonRepository.findOne(75).getValue());
        config.setMchId(commonRepository.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        //支付成功业务
                        this.wxPayBack(out_trade_no, 2); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    @Override
    public String payBacks(String resXml) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setAppId(commonRepository.findOne(74).getValue());
        config.setKey(commonRepository.findOne(75).getValue());
        config.setMchId(commonRepository.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        //支付成功业务
                        this.wxPayBack(out_trade_no, 1); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }

    @Override
    public boolean refund(Long ordersId) {
        Orders orders = ordersJpaRepository.findById(ordersId).orElse(null);
        PayDetails payDetails = new PayDetails();
        String orderNum = null;
        if (orders.getOrderType() == 5) {
            payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getReOrderNum())
                    .eq("classify", orders.getPayWay()));
            if (payDetails != null) {
                if (payDetails.getState() == 1) {
                    orderNum = payDetails.getOrderId();
                }
            } else {
                payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNum())
                        .eq("classify", orders.getPayWay()));
                if (payDetails != null) {
                    if (payDetails.getState() == 1) {
                        orderNum = payDetails.getOrderId();
                    }
                }
            }
        } else {
            payDetails = payDetailsDao.selectByOrderId(orders.getBuyOrderNum());
            if (payDetails != null) {
                if (payDetails.getState() == 1) {
                    orderNum = payDetails.getOrderId();
                }
            }
        }
        WXConfigUtil config = null;
        String h5Url = commonRepository.findOne(19).getValue().split("://")[1];
//        String filePath = "/www/wwwroot/" + h5Url + "/service/apiclient_cert.p12";
        String filePath = "D:\\tuikuanzhengshu\\省钱兄科技的退款证书\\apiclient_cert.p12";
        try {
            config = new WXConfigUtil(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        Integer payWay = orders.getPayWay(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
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
        }
        config.setAppId(commonRepository.findOne(commInfoId).getValue());
        config.setKey(commonRepository.findOne(75).getValue());
        config.setMchId(commonRepository.findOne(76).getValue());
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
        data.put("out_trade_no", orderNum); //订单号,支付单号一致
        data.put("out_refund_no", getGeneralOrder()); //退款单号，同一笔用不同的退款单号
        double total_fee = 0.00;
        List<Orders> list = ordersService.findByPayNum(orders.getPayNum());
        for (int i = 0; i < list.size(); i++) {
            BigDecimal payMoney = list.get(i).getPayMoney();
            total_fee += payMoney.doubleValue(); //付款金额
        }
        data.put("total_fee", new Double(total_fee * 100).intValue() + ""); //1块等于微信支付传入100);
        data.put("refund_fee", new Double(orders.getPayMoney().doubleValue() * 100).intValue() + ""); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            for (String key : response.keySet()) {
                log.info("微信退款回调说明：keys:" + key + "    value:" + response.get(key).toString());
            }
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("返回");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 小程序支付
     *
     * @return
     */
    @Override
    public Map wxPayMp(String payNum, Long userId, BigDecimal payMoney) throws Exception {
        Map<String, Object> payMap = new HashMap<String, Object>();//返回给小程序端需要的参数
        String url = commonRepository.findOne(19).getValue() + "/tao/pay/notifyMp";
        String body = Config.BASE_NAME.trim();
        List<Orders> list = ordersService.findByPayNum(payNum);
        /*if (list.size() >0){
            body = list.get(0).getTitle();
        }*/
        //支付金额校验
        BigDecimal paySum = new BigDecimal("0.00");
        for (Orders o : list) {
            if (o.getGroupId() != null) {
                SelfGroup selfGroup = selfGroupJpaRepository.findById(o.getGroupId()).orElse(null);
                if (selfGroup == null || selfGroup.getStatus() != 1) {
                    throw new Exception("商品已被下架或移除，请选择其他商品！");
                }
            } else if (o.getSecKillId() != null) {
                SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(o.getSecKillId()).orElse(null);
                if (selfSecKill == null || selfSecKill.getStatus() != 1) {
                    throw new Exception("商品已被下架或移除，请选择其他商品！");
                }
            } else {
                Goods g = goodsJpaRepository.findById(o.getGoodsId()).orElse(null);
                if (g == null || g.getStatus() != 1) {
                    throw new Exception("商品已被下架或移除，请选择其他商品！");
                }
            }
            paySum = paySum.add(o.getPayMoney());
            if (o.getStatus() != 1) {
                throw new Exception("订单已经支付过了！");
            }
            if (o.getGroupPinkId() != null) {
                SelfGroupPink selfGroupPink = selfGroupPinkJpaRepository.findById(o.getGroupPinkId()).orElse(null);
                if (selfGroupPink.getStatus() == 2) {
                    throw new Exception("拼团已完成，无法参与！");
                }
            }
        }
        log.info("支付金额，传入金额：" + payMoney + "计算金额：" + paySum);
        payMoney = paySum;
        String money = payMoney.multiply(new BigDecimal("100")).intValue() + "";
        try {
            String appId = commonRepository.findOne(45).getValue();
            String mchId = commonRepository.findOne(76).getValue();
            String key = commonRepository.findOne(75).getValue();
            String openId = userJpaRepository.getOne(userId).getOpenId();
            //生成的随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<>();
            packageParams.put("appid", appId);
            packageParams.put("mch_id", mchId);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", payNum);//商户订单号,自己的订单ID
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("notify_url", url);//支付成功后的回调地址
            packageParams.put("trade_type", TRADE_TYPE_H5);//支付方式
            packageParams.put("openid", openId);//用户的openID，自己获取
            String prestr = SignUtil.createLinkString(packageParams); //去掉空格：踩坑
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = SignUtil.sign(prestr, key).toUpperCase();
            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + appId + "</appid>"
                    + "<attach>" + "" + "</attach>"
                    + "<body>" + body + " </body>"
                    + "<mch_id>" + mchId + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + url + "</notify_url>"
                    + "<openid>" + openId + "</openid>"
                    + "<out_trade_no>" + payNum + "</out_trade_no>"
                    + "<total_fee>" + money + "</total_fee>"//支付的金额，单位：分
                    + "<trade_type>" + TRADE_TYPE_H5 + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            /**踩坑：body中的内容会拼接空格进去*/
            xml = xml.replaceAll(" ", "");
            //调用统一下单接口，并接受返回的结果
            String result = SignUtil.httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", xml);
            // 将解析结果存储在HashMap中
            Map map = SignUtil.doXMLParse(result);
            String return_code = (String) map.get("return_code");//返回状态码
            String result_code = (String) map.get("result_code");//返回状态码
            String err_code = (String) map.get("err_code");//返回状态码
            String err_code_des = (String) map.get("err_code_des");//返回状态码
            log.error("下单状态：:");
            log.error("return_code:" + return_code);
            log.error("result_code:" + result_code);
            log.error("err_code:" + err_code);
            log.error("err_code_des:" + err_code_des);
            if (return_code.equals("SUCCESS") || return_code.equals(result_code)) {
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                Map<String, String> param = new HashMap<>();
                param.put("nonceStr", nonce_str);
                Long timestamp = System.currentTimeMillis() / 1000;
                param.put("timeStamp", timestamp + "");
                String stringSignTemp = "appId=" + appId + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timestamp + "";
                String sign = SignUtil.sign(stringSignTemp, key).toUpperCase();
                System.err.println(stringSignTemp);
                System.err.println(sign);
                param.put("paySign", sign);
                param.put("package", "prepay_id=" + prepay_id);//给前端返回的值
                param.put("signType", "MD5");
                return param;
            } else {
                return payMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payMap;
    }

    /**
     * 小程序回调
     *
     * @param resXml
     * @return
     */
    @Override
    public String payBackMp(String resXml) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setAppId(commonRepository.findOne(45).getValue());
        config.setKey(commonRepository.findOne(75).getValue());
        config.setMchId(commonRepository.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        this.wxPayBack(out_trade_no, 3); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }

    @Autowired
    private SelfGoodsVirtualService selfGoodsVirtualService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SelfOrdersRemindJpaRepository selfOrdersRemindJpaRepository;

    /**
     * 支付成功业务
     */
    public void wxPayBack(String out_trade_no, Integer payWay) {
        //支付成功，处理业务：处理多个订单
        List<Orders> orderList = ordersService.findByPayNum(out_trade_no);
        for (Orders orders : orderList) {
            if (orders.getPayWay() != null) {
                log.info("订单回调重复：" + orders.toString());
                return;
            }
            SelfOrderRemind r = new SelfOrderRemind();
            r.setOrdersId(orders.getId());
            r.setCreateTime(DateUtils.createTime());
            r.setStatus(1);
            selfOrdersRemindJpaRepository.save(r);


            orders.setStatus(2); //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中 10已评价）
            //2.分销业务
//            ordersService.fenXiao(orders.getId());
            //3.公众号消息通知
            userService.orderPay(orders.getUserId(), orders.getOrderNum(), orders.getPayMoney().toString(), orders.getCreateAt());
            //4.订单业务
            if (orders.getOrderType() == 1) { //普通订单
                //4.1虚拟商品处理
                if (orders.getIsExpress() == 2) { //是否需要发货(1需要发货 2无需发货)
                    //虚拟商品发货，发货成功为确认收货，否则不做变化
                    SelfGoodsVirtual v = selfGoodsVirtualService.sendGoods(orders.getGoodsId());
                    if (v != null) {
                        orders.setVirtualId(v.getId()); //虚拟商品id
                        orders.setExpressTime(DateUtils.createTime()); //发货时间
                        orders.setFinishTime(DateUtils.createTime()); //收货时间
                        orders.setStatus(4); //确认收货
                    }
                } else if (orders.getIsExpress() == 3) { //核销订单
                    orders.setStatus(3); //待收货、待使用
                }
                //4.2拼团业务处理
            } else if (orders.getOrderType() == 2) {
                orders.setStatus(9);
                ordersService.pink(orders);
            }
            orders.setPayWay(payWay); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
            orders.setPayTime(DateUtils.createTime()); //设置支付时间
            ordersDao.updateById(orders);
        }
    }

}
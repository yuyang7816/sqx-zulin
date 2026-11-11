package com.sqx.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.constant.AliPayConstants;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.TbCouponUser;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.TbCoupon;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDao;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.pay.AliPayService;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.TbCouponService;
import com.sqx.domain.repository.service.sys.TbCouponUserService;
import com.sqx.domain.repository.service.vipDetails.VipDetailsService;
import com.sqx.domain.repository.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private TbCouponUserService couponUserService;
    @Autowired
    private TbCouponService couponService;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private UserDao userDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Login
    @ApiOperation("充值金额")
    @RequestMapping(value = "/payMoney", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payAppMoney(@RequestAttribute("userId") Long userId, BigDecimal money, Integer classify) {
        return aliPayService.payMoney(userId, money, null, classify,1);
    }


    /**
     *
     * @param userId
     * @param orderId
     * @param classify
     * @param orderType
     * @return
     */
    @Login
    @ApiOperation("支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrder(@RequestAttribute("userId") Long userId, Long orderId,BigDecimal money, Integer classify,Integer orderType) {
        return aliPayService.payOrder(userId, orderId, money,classify, orderType);
    }

    @Login
    @ApiOperation("支付宝补缴租金")
    @RequestMapping(value = "/payRent", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payRent(@RequestAttribute("userId") Long userId, Long orderId,BigDecimal supplementMoney, Integer classify) {
        return aliPayService.payRent(userId, orderId,supplementMoney, classify);
    }

    @Login
    @ApiOperation("退款")
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result refund(String orderId,BigDecimal money) {
        if (aliPayService.aliRefund(orderId, money)){
            return Result.success();
        }
        return Result.failed();
    }

    @Login
    @ApiOperation("支付继续租赁订单")
    @RequestMapping(value = "/continuingLease", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result continuingLease(@RequestAttribute("userId") Long userId, Orders entity,Integer classify) throws Exception {
        return aliPayService.continuingLease(userId, entity, classify);
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改(买断租赁的商品)")
    @Transactional(rollbackFor = Exception.class)
    public Result updateBody(@RequestBody Orders entity,@RequestAttribute Long userId,Integer classify) {
        return aliPayService.updateBody(entity,userId,classify);
    }


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    log.error(outTradeNo);
                    //支付宝支付单号
                    String tradeNo = params.get("trade_no");
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if (payDetails != null && payDetails.getState() == 0) {
                        payDetails.setState(1);
                        payDetails.setPayTime(sdf.format(new Date()));
                        payDetailsDao.updateById(payDetails);
                        if (payDetails.getType() == 1) {
                            //余额充值
                            userMoneyDao.updateMayMoney(1, payDetails.getUserId(), payDetails.getMoney());
                            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                            userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                            userMoneyDetails.setUserId(payDetails.getUserId());
                            userMoneyDetails.setContent("支付宝充值：" + payDetails.getMoney());
                            userMoneyDetails.setTitle("账户充值");
                            userMoneyDetails.setType(1);
                            userMoneyDetails.setClassify(0);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                            userMoneyDetailsService.save(userMoneyDetails);
                            UserEntity user = userDao.selectById(payDetails.getUserId());
                            if (user.getOpenId() != null){
                            String commonInfo = commonInfoService.findOne(523).getValue();
                            List<String> msgList = new ArrayList<>();
                            msgList.add("充值订单:" + payDetails.getOrderId() + "已充值成功");
                            msgList.add("充值金额：" + payDetails.getMoney());
                            msgList.add("支付时间：" + new Date());
                            SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 1);
                            }
                        }else if (payDetails.getType() == 3){
                            vipDetailsService.callbackBusiness(payDetails.getOrderId());
                        } else if (payDetails.getType() == 7){
                            couponUserService.businessCallback(3, payDetails.getUserId(), payDetails.getRelationId(), payDetails.getBuyNum(), BigDecimal.valueOf(payDetails.getMoney()));
                        } else if (payDetails.getType() == 2){
                            //订单支付
                            Orders tbOrders = ordersService.findByOrderNum(payDetails.getOrderId());
                            ordersService.payOrderBack(tbOrders.getId(), tbOrders.getPayMoney(), payDetails.getClassify());
                        }else if (payDetails.getType() == 4){
                            //补缴租金
                            Orders tbOrders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("supplement_num",payDetails.getOrderId()));
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
                    writeResponse(response, "success");
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Login
    @ApiOperation("支付宝购买优惠券")
    @RequestMapping(value = "/payCoupon", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payCoupon(@RequestAttribute("userId") Long userId, Long couponId, Integer buyNum, Integer payType) {
        TbCoupon tbCoupon = couponService.getById(couponId);
        if (tbCoupon == null || tbCoupon.getIsEnable() == 0 || tbCoupon.getDeleteFlag() == 1) {
            return Result.failed("优惠券暂未出售或不存在");
        }
//        if (tbCoupon.getCouponType() != 2) {
//            return Result.error("当前优惠券不支持购买");
//        }
        //查看当前用户已购买或领取数量
        Long num = couponUserService.count(new QueryWrapper<TbCouponUser>().eq("user_id", userId).eq("coupon_id", couponId));
        if (tbCoupon.getMaxReceive() != 0) {
            if ((tbCoupon.getMaxReceive() - num) <= buyNum) {
                return Result.failed("当前可购买或领取的数量已到达上限");
            }

        }
//        BigDecimal totalPrice = tbCoupon.getBuyMoney().multiply(new BigDecimal(buyNum));
        PayDetails payDetails = new PayDetails();
        payDetails.setOrderId(getGeneralOrder());
        payDetails.setState(0);
        payDetails.setBuyNum(buyNum);
        payDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        payDetails.setUserId(userId);
//        payDetails.setMoney(totalPrice.doubleValue());
        payDetails.setMoney(BigDecimal.ZERO.doubleValue());
        payDetails.setClassify(4);
        payDetails.setType(7);
        payDetails.setRelationId(couponId);
        payDetailsDao.insert(payDetails);

        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue();
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "场馆预约" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);

        if (payType == 1) {
            return payApp(name, payDetails.getOrderId(), payDetails.getMoney());
        }
        return payH5(name, payDetails.getOrderId(), payDetails.getMoney(), returnUrl);
    }

    public Result payApp(String name, String generalOrder, Double money) {
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String result = "";
        CommonInfo payWay = commonInfoService.findOne(509);
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(510);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(money + "");
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.failed("获取订单失败！");
                }
                return Result.success(result);
            } else {
                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(String.valueOf(money));
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.failed("获取订单失败！");
                }
                return Result.success(result);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.failed(-100, "获取订单失败！");
    }

    public Result payH5(String name, String generalOrder, Double money, String returnUrl) {
        CommonInfo payWay = commonInfoService.findOne(509);
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(510);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //金额
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success(result);
            } else {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money); //金额
                alipayRequest.setBizContent(order.toString());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(url);
                //通知页面地址
                alipayRequest.setReturnUrl(returnUrl);
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success(form);
            }
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.failed("获取订单信息错误！");
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }
}

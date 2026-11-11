package com.sqx.system.selfAliPay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.constant.AliPayConstants;
import com.sqx.common.core.constant.AliPayNotifyParamConstants;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.AliPayOrderUtil;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGroup.SelfGroupJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupPink.SelfGroupPinkJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.mapper.selfOrderRemind.SelfOrdersRemindJpaRepository;
import com.sqx.domain.repository.mapper.selfSeckill.SelfSeckillJpaRepository;
import com.sqx.domain.repository.service.selfGoodsVirtual.SelfGoodsVirtualService;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Api(value="自营商城支付宝支付",tags={"自营商城支付宝支付"})
@Slf4j
@RestController
@RequestMapping("/aliPay")
public class SelfAliPayController {

    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;
    @Autowired
    private SelfGoodsVirtualService selfGoodsVirtualService;
    @Autowired
    private IUserService userService;
    @Autowired
    private SelfOrdersRemindJpaRepository selfOrdersRemindJpaRepository;
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
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    /**
     * 创建线程池处理业务逻辑
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(30, 100,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 支付宝网页支付
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/payment")
    public void aliPay(HttpServletResponse response,String payNum, Long userId, BigDecimal payMoney) throws IOException {
        String one2 = commonRepository.findOne(19).getValue();
        String url=one2+"/tao/aliPay/notifyH5";
        String returl = commonRepository.findOne(298).getValue();
        CommonInfo one1 = commonRepository.findOne(12);
        String body = one1.getValue();
        List<Orders> list=new ArrayList<>();
        for(String pay:payNum.split(",")){
            List<Orders> byPayNum = ordersService.findByPayNum(pay);
            list.addAll(byPayNum);
        }
        if (list.size() >0){
            body = list.get(0).getTitle();
        }
        //支付金额校验
        BigDecimal paySum = new BigDecimal("0.00");
        for (Orders o : list) {
            paySum = paySum.add(o.getPayMoney());
        }
        log.info("支付金额，传入金额："+payMoney+"计算金额："+paySum);
        payMoney = paySum;
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConstants.REQUEST_URL,
                commonRepository.findOne(63).getValue(), commonRepository.findOne(65).getValue(), AliPayConstants.FORMAT,
                AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), AliPayConstants.SIGNTYPE);


        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(url);
        request.setReturnUrl(returl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payNum);
        bizContent.put("total_amount", String.valueOf(payMoney));
        bizContent.put("subject", body);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradePagePayResponse response1 = alipayClient.pageExecute(request);
            String body1 = response1.getBody();
            response.setContentType("text/html;charset=" + AliPayConstants.CHARSET);
            response.getWriter().write(body1);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("支付宝支付异常，异常信息为:" + e.getErrMsg(),e);
        }

    }

    /**
     * 支付宝异步通知地址
     * @param request
     * @return
     */
    @RequestMapping("notify")
    @Transactional(rollbackFor = Exception.class)
    public void returnUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = AliPayOrderUtil.convertRequestParamsToMap(request);
        String result = "";
        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, commonRepository.findOne(64).getValue(),
                    AliPayConstants.CHARSET, AliPayConstants.SIGNTYPE);
        } catch (AlipayApiException e) {
            log.info("支付宝回调验签异常：" + e.getMessage());
            e.printStackTrace();
        }
        if (signVerified) {
            this.check(params);
            singleThreadPool.execute(() -> {
                AliPayNotifyParamConstants param = AliPayOrderUtil.buildAliPayNotifyParam(params);
                String tradeStatus = param.getTradeStatus();
                //支付成功
                if (tradeStatus.equalsIgnoreCase(AliPayConstants.TRADE_SUCCESS)) {
                    // TODO 处理业务逻辑
                    String outTradeNo = params.get("out_trade_no");
                    //支付成功，处理业务
                    Orders orders = ordersService.findByOrderNum(outTradeNo);
                    if (orders.getStatus() == 1){
                        orders.setPayWay(2); //支付方式为2支付宝
                        orders.setStatus(2); //2已付款
                        orders.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //设置支付时间
                        ordersDao.updateById(orders);
                    }
                }
            });
            result = "success";
        } else {
            result = "failure";
        }
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.info("支付宝返回异常，异常信息为：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 校验支付宝支付返回的订单信息是否正确
     *
     * @param params
     */
    private void check(Map<String, String> params) {
        for (String key : params.keySet()){
            log.info("key="+key+";  value="+params.get(key));
        }
        // TODO 判断支付订单号是否是同一个
        String outTradeNo = params.get("out_trade_no");
        // 订单支付金额是否正确
        BigDecimal totalAmount = new BigDecimal(params.get("total_amount"));
//        Assert.isTrue(!totalAmount.equals(new BigDecimal(0.2)), "支付金额错误");
        // 判断支付的商户信息是否一致
        Assert.isTrue(params.get("app_id").equals(commonRepository.findOne(63).getValue()), "支付的商户信息不正确");
    }

    @IgnoreSecurity
    @RequestMapping("/notifyApp")
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
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
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonRepository.findOne(64).getValue(), AliPayConstants.CHARSET,"RSA2");
            log.info(flag+"回调验证信息");
            if(flag){
                String tradeStatus = params.get("trade_status");
                if("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)){
                    String outTradeNo = params.get("out_trade_no");
                    this.payBack(outTradeNo, 4); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱
                }

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }

    @IgnoreSecurity
    @RequestMapping("/notifyH5")
    @Transactional(rollbackFor = Exception.class)
    public void notifyH5(HttpServletRequest request, HttpServletResponse response){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
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
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonRepository.findOne(64).getValue(), AliPayConstants.CHARSET,"RSA2");
            log.info(flag+"回调验证信息");
            if(flag){
                String tradeStatus = params.get("trade_status");
                if("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)){
                    String outTradeNo = params.get("out_trade_no");
                    this.payBack(outTradeNo, 5); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱
                }

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }

    /**
     * 支付回调业务
     * @param payNum
     * @param payWay
     */
    private void payBack(String payNum, Integer payWay){
        //支付成功，处理业务
        List<Orders> list = ordersService.findByPayNum(payNum);
        for (Orders orders : list) {
            if (orders.getPayWay() != null){
                log.info("订单回调重复："+orders.toString());
                return;
            }

            SelfOrderRemind r = new SelfOrderRemind();
            r.setOrdersId(orders.getId());
            r.setCreateTime(DateUtils.createTime());
            r.setStatus(1);
            selfOrdersRemindJpaRepository.save(r);


            orders.setPayWay(payWay); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
            orders.setStatus(2); //已付款
            orders.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //设置支付时间
            //4.订单业务
            if (orders.getOrderType() == 1){ //普通订单
                //4.1虚拟商品处理
                if (orders.getIsExpress() == 2){ //是否需要发货(1需要发货 2无需发货)
                    //虚拟商品发货，发货成功为确认收货，否则不做变化
                    SelfGoodsVirtual v = selfGoodsVirtualService.sendGoods(orders.getId());
                    if (v != null){
                        orders.setVirtualId(v.getId()); //虚拟商品id
                        orders.setExpressTime(DateUtils.createTime()); //发货时间
                        orders.setFinishTime(DateUtils.createTime()); //收货时间
                        orders.setStatus(4); //确认收货
                    }
                }else if (orders.getIsExpress() == 3){ //核销订单
                    orders.setStatus(3); //待收货、待使用
                }
                //4.2拼团业务处理
            }else if (orders.getOrderType() == 2){
                orders.setStatus(9);
                ordersService.pink(orders);
            }
            ordersDao.updateById(orders);
            //订单分销
//            ordersService.fenXiao(orders.getId());
            //公众号消息通知
            userService.orderPay(orders.getUserId(), orders.getOrderNum(),orders.getPayMoney().toString(),orders.getCreateAt());
        }
    }


    @IgnoreSecurity
    @ApiOperation("app支付")
    @GetMapping("/payApp")
    @Transactional(rollbackFor = Exception.class)
    public Result payApp(String payNum, Long userId, BigDecimal payMoney){
        log.info("开始下单了！！");
        String one2 = commonRepository.findOne(19).getValue();
        String url=one2+"/tao/aliPay/notifyApp";
        //url="http://2o9t550523.qicp.vip/tao/aliPay/notifyApp"; //本地测试地址
        String returl=commonRepository.findOne(297).getValue();
        log.info("回调地址:"+url);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonRepository.findOne(63).getValue(), commonRepository.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String body = Config.BASE_NAME;
        List<Orders> list = ordersService.findByPayNum(payNum);
        if (list.size() >0){
            body = list.get(0).getTitle();
        }
        //支付金额校验
        BigDecimal paySum = new BigDecimal("0.00");
        for (Orders o : list) {
            if(o.getGroupId()!=null){
                SelfGroup selfGroup = selfGroupJpaRepository.findById(o.getGroupId()).orElse(null);
                if(selfGroup==null || selfGroup.getStatus()!=1){
                    return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                }
            }else if(o.getSecKillId()!=null){
                SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(o.getSecKillId()).orElse(null);
                if(selfSecKill==null || selfSecKill.getStatus()!=1){
                    return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                }
            }else{
                Goods g = goodsJpaRepository.findById(o.getGoodsId()).orElse(null);
                if(g==null || g.getStatus()!=1){
                    return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                }
            }
            paySum = paySum.add(o.getPayMoney());
            if (o.getStatus() != 1) {
                return ResultUtil.error(-200, "订单已经支付过了！");
            }
            if (o.getGroupPinkId() != null) {
                SelfGroupPink selfGroupPink = selfGroupPinkJpaRepository.findById(o.getGroupPinkId()).orElse(null);
                if (selfGroupPink.getStatus() == 2) {
                    return ResultUtil.error(-200, "拼团已完成，无法参与！");
                }
            }
        }
        log.info("支付金额，传入金额："+payMoney+"计算金额："+paySum);
        payMoney = paySum;
        model.setBody(body);
        model.setSubject(body);
        model.setOutTradeNo(payNum);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(payMoney+""); //金额
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(url);
        request.setReturnUrl(returl);
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if(response.isSuccess()){
                return ResultUtil.success(response.getBody());
            } else {
                log.info("调用失败");
                ResultUtil.error(-100,"获取订单失败！");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultUtil.error(-100,"获取订单失败！");

    }

    @IgnoreSecurity
    @ApiOperation("h5支付")
    @GetMapping(value = "/payH5")
    @Transactional(rollbackFor = Exception.class)
    public Result payH5(String payNum, Long userId, BigDecimal payMoney) {
        String one2 = commonRepository.findOne(19).getValue();
        String url=one2+"/tao/aliPay/notifyH5";
        String returl = commonRepository.findOne(297).getValue();
        log.info("回调地址:"+url);
        try {
            CommonInfo one1 = commonRepository.findOne(12);
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonRepository.findOne(63).getValue(), commonRepository.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), "RSA2");
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            JSONObject order = new JSONObject();
            order.put("out_trade_no",payNum); //订单号
            String body = one1.getValue();
            List<Orders> list = ordersService.findByPayNum(payNum);
            if (list.size() >0){
                body = list.get(0).getTitle();
            }
            //支付金额校验
            BigDecimal paySum = new BigDecimal("0.00");
            for (Orders o : list) {
                if(o.getGroupId()!=null){
                    SelfGroup selfGroup = selfGroupJpaRepository.findById(o.getGroupId()).orElse(null);
                    if(selfGroup==null || selfGroup.getStatus()!=1){
                        return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                    }
                }else if(o.getSecKillId()!=null){
                    SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(o.getSecKillId()).orElse(null);
                    if(selfSecKill==null || selfSecKill.getStatus()!=1){
                        return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                    }
                }else{
                    Goods g = goodsJpaRepository.findById(o.getGoodsId()).orElse(null);
                    if(g==null || g.getStatus()!=1){
                        return ResultUtil.error(-200, "商品已被下架或移除，请选择其他商品！");
                    }
                }
                paySum = paySum.add(o.getPayMoney());
                if (o.getStatus() != 1) {
                    return ResultUtil.error(-200, "订单已经支付过了！");
                }
                if (o.getGroupPinkId() != null) {
                    SelfGroupPink selfGroupPink = selfGroupPinkJpaRepository.findById(o.getGroupPinkId()).orElse(null);
                    if (selfGroupPink.getStatus() == 2) {
                        return ResultUtil.error(-200, "拼团已完成，无法参与！");
                    }
                }
            }
            log.info("支付金额，传入金额："+payMoney+"计算金额："+paySum);
            payMoney = paySum;
            order.put("subject", Config.BASE_NAME); //商品标题
            order.put("product_code", "QUICK_WAP_WAY");
            order.put("body", body);//商品名称
            order.put("total_amount", payMoney+""); //金额
            alipayRequest.setBizContent(order.toString());
            alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
            alipayRequest.setReturnUrl(returl); //线上通知页面地址
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            return ResultUtil.success(form);
        }catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultUtil.error(-100,"获取订单失败！");
    }
}

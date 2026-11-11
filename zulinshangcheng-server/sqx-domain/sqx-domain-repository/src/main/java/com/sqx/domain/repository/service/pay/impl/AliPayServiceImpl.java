package com.sqx.domain.repository.service.pay.impl;


import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.core.constant.AliPayConstants;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.app.Address;
import com.sqx.domain.repository.entity.app.ContinuingOrder;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.app.AddressDao;
import com.sqx.domain.repository.mapper.app.UserDao;
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
import com.sqx.domain.repository.service.pay.AliPayService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.TbOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.sqx.common.core.utils.wx.WxPayUtils.getGeneralOrder;


@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private TbOrdersService ordersService;
    @Autowired
    private OrdersDao ordersDao;
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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify, Integer type) {
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "余额充值" : one3.getValue();
        String generalOrder = getGeneralOrder();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String returnUrl = one.getValue() + "/pages/task/recharge";
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(type);
        if (parentId != null) {
            payDetails.setParentId(parentId);
        }
        payDetailsDao.insert(payDetails);
        if (classify == 4) {
            return payApp(generalOrder, name, money, url);
        }
        return payH5(generalOrder, name, money, url, returnUrl);
    }

    @Override
    public Result payOrder(Long userId, Long orderId,BigDecimal money, Integer classify, Integer orderType) {
        Orders orders = ordersDao.selectById(orderId);
        if (orders == null) {
            return Result.failed("订单不存在");
        }
//        if (orders.getStatus() != 0) {
//            return Result.failed("订单状态已变更,请刷新后重试");
//        }
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "支付订单" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String returnUrl = one.getValue() + "/pages/task/recharge";
        PayDetails payDetails = null;
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
                    if (classify == 4) {
                        return payApp(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url);
                    }
                    return payH5(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url, returnUrl);
                // 继续租赁订单
            }else if (orders.getStatus() == 4){
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
                    if (classify == 4) {
                        return payApp(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url);
                    }
                    return payH5(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url, returnUrl);
                }
            // 买断订单
        }else if (orderType == 6){
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
            if (classify == 4) {
                return payApp(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url);
            }
            return payH5(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url, returnUrl);
        }
        return Result.failed("订单状态已变更,请刷新后重试");
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
//        if (classify == 4) {
//            return payApp(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url);
//        }
//        return payH5(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url, returnUrl);
    }

    @Override
    public Result payRent(Long userId, Long orderId, BigDecimal supplementMoney, Integer classify) {
        Orders orders = ordersDao.selectById(orderId);
        if (orders == null) {
            return Result.failed("订单不存在");
        }
        if (orders.getSupplementStatus() != null && orders.getSupplementStatus() != 2) {
            return Result.failed("订单状态已变更,请刷新后重试");
        }
        orders.setSupplementStatus(2);
        orders.setSupplementMoney(supplementMoney);
        orders.setSupplementNum(getGeneralOrder());
        ordersDao.updateById(orders);
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "支付订单" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String returnUrl = one.getValue() + "/pages/task/recharge";
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
        if (classify == 4) {
            return payApp(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url);
        }
        return payH5(payDetails.getOrderId(), name, BigDecimal.valueOf(payDetails.getMoney()), url, returnUrl);
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
            CommonInfo one = commonInfoService.findOne(19);
            CommonInfo one3 = commonInfoService.findOne(12);
            String name = one3 == null ? "继续租赁" : one3.getValue();
            String generalOrder = getGeneralOrder();
            String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
            String returnUrl = one.getValue() + "/pages/task/recharge";
            if (classify == 4) {
                payApp(entityOrders.getOrderNum(), name, entityOrders.getTotalContinueMoney(), url);
            }
            payH5(entityOrders.getOrderNum(), name, entityOrders.getTotalContinueMoney(), url, returnUrl);
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
                CommonInfo one = commonInfoService.findOne(19);
                CommonInfo one3 = commonInfoService.findOne(12);
                String name = one3 == null ? "继续租赁" : one3.getValue();
                String generalOrder = getGeneralOrder();
                String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
                String returnUrl = one.getValue() + "/pages/task/recharge";
                if (classify == 4) {
                    payApp(entity.getOrderNum(), name, entity.getPrice(), url);
                }
                payH5(entity.getOrderNum(), name, entity.getPrice(), url, returnUrl);
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


    public Result payApp(String generalOrder, String name, BigDecimal money, String url) {
        try {
            String pay = commonInfoService.findOne(97).getValue();
            if ("1".equals(pay)) {
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
                    return Result.success(response.getBody());
                } else {
                    return Result.failed("获取订单失败！");
                }
            } else {
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
                CommonInfo urls = commonInfoService.findOne(200);
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
                    return Result.success(response.getBody());
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.failed(-100, "获取订单失败！");
    }

    public Result payH5(String generalOrder, String name, BigDecimal money, String url, String returnUrl) {
        String pay = commonInfoService.findOne(97).getValue();
        try {
            if ("1".equals(pay)) {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        commonInfoService.findOne(63).getValue(),
                        commonInfoService.findOne(65).getValue(),
                        "json", AliPayConstants.CHARSET,
                        commonInfoService.findOne(64).getValue(), "RSA2");
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
            } else {
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
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                com.alibaba.fastjson.JSONObject order = new com.alibaba.fastjson.JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //金额
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success(form);
            }
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.failed("获取订单信息错误！");
    }

    @Override
    public boolean aliRefund(String orderId, BigDecimal refundMoney) {
        PayDetails payDetails = new PayDetails();
        String pay = commonInfoService.findOne(97).getValue();
        try {
            if ("1".equals(pay)) {
                payDetails = payDetailsDao.selectByOrderId(orderId);
                if (refundMoney == null) {
                    refundMoney = BigDecimal.valueOf(payDetails.getMoney());
                }
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        commonInfoService.findOne(63).getValue(),
                        commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET,
                        commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(payDetails.getOrderId()); //订单号
                model.setRefundAmount(refundMoney + ""); //退款金额
                model.setOutRequestNo(payDetails.getOrderId()); //退款单号
                request.setBizModel(model);
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                if (response.isSuccess()) {
                    return true;
                } else {
                    return false;
                }
            } else {
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
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(payDetails.getOrderId()); //订单号
                model.setRefundAmount(refundMoney + ""); //退款金额
                model.setOutRequestNo(payDetails.getOrderId()); //退款单号
                request.setBizModel(model);
                AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
                if (response.isSuccess()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝退款失败！，订单号" + payDetails.getOrderId() + "，失败原因：" + e.getMessage(), e);
        }
        return false;
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

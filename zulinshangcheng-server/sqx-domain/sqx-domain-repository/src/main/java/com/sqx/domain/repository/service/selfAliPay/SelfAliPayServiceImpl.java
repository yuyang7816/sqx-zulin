package com.sqx.domain.repository.service.selfAliPay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.core.constant.AliPayConstants;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SelfAliPayServiceImpl implements SelfAliPayService {
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;

    @Override
    public boolean refund(Long ordersId) {
        PayDetails payDetails = new PayDetails();
        String orderNum = null;
        try {
            Orders orders = ordersJpaRepository.findById(ordersId).orElse(null);
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
//            payDetails = payDetailsDao.selectByOrderId(orders.getOrderNum());
            BigDecimal refundMoney = BigDecimal.valueOf(payDetails.getMoney());
            String pay = commonRepository.findOne(97).getValue();
            if ("1".equals(pay)) {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        commonRepository.findOne(63).getValue(),
                        commonRepository.findOne(65).getValue(),
                        "json", AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), "RSA2");
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(orderNum); //订单号
                model.setRefundAmount(payDetails.getMoney() + ""); //退款金额
                model.setOutRequestNo(orders.getOrderNum()); //退款单号
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
        }
        return false;
    }
}

package com.sqx.system.selfWXPay;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.service.selfWXPay.SelfWXService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;

@Api(value="自营商城微信支付",tags={"自营商城微信支付"})
@RestController
@RequestMapping("/pay")
public class SelfWXPayController {
    private final Logger log = LoggerFactory.getLogger(SelfWXPayController.class);
    @Autowired
    private SelfWXService service;

    @IgnoreSecurity
    @ApiOperation("app支付")
    @PostMapping("/wxPayApp")
    public Map wxPayApp(String payNum, Long userId, BigDecimal payMoney,HttpServletRequest request) throws Exception {
        return service.doUnifiedOrder(payNum,1, userId, payMoney,request);
    }

    @IgnoreSecurity
    @ApiOperation("公众号JSAPI支付")
    @PostMapping("/wxPayWeb")
    public Map wxPayWeb(String payNum, Long userId, BigDecimal payMoney,HttpServletRequest request) throws Exception {
        return service.doUnifiedOrder(payNum,2, userId, payMoney,request);
    }

    @IgnoreSecurity
    @ApiOperation("小程序支付")
    @PostMapping("/wxPay")
    public Map wxPay(String payNum, Long userId, BigDecimal payMoney) throws Exception {
        return service.wxPayMp(payNum, userId, payMoney);
    }

    @IgnoreSecurity
    @ApiOperation("小程序支付")
    @PostMapping("/wxH5Pay")
    public Map wxH5Pay(String payNum, Long userId, BigDecimal payMoney,HttpServletRequest request) throws Exception {
        return service.doUnifiedOrder(payNum,4, userId, payMoney,request);
    }


    @IgnoreSecurity
    @PostMapping("/notify")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = service.payBack(resXml);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @IgnoreSecurity
    @PostMapping("/notifys")
    public String wxPayNotifys(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = service.payBacks(resXml);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @IgnoreSecurity
    @PostMapping("/notifyMp")
    public String wxPayNotifyMp(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = service.payBackMp(resXml);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @IgnoreSecurity
    @ApiOperation("微信退款")
    @PostMapping("/refund")
    public Result refund(Long ordersId){
        return ResultUtil.success(service.refund(ordersId));
    }

}

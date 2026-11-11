package com.sqx.pay;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.service.pay.WxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fang
 * @date 2020/2/26
 */
@RestController
@Api(value = "微信支付", tags = {"微信支付"})
@RequestMapping("/app/wxPay")
@Slf4j
public class ApiWeiXinPayController {

    @Autowired
    private WxService wxService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    @Login
    @ApiOperation("充值金额")
    @RequestMapping(value = "/payMoney", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payAppMoney(@RequestAttribute("userId") Long userId, BigDecimal money, Integer classify) throws Exception {
        return wxService.payMoney(userId, money, null, classify,1);
    }


    @Login
    @ApiOperation("支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrder(@RequestAttribute("userId") Long userId, Long orderId,BigDecimal money, Integer classify,Integer orderType) throws Exception {
        return wxService.payOrder(userId, orderId,money,classify,orderType);
    }

    @Login
    @ApiOperation("支付继续租赁订单")
    @RequestMapping(value = "/continuingLease", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result continuingLease(@RequestAttribute("userId") Long userId, Orders entity,Integer classify) throws Exception {
        return wxService.continuingLease(userId, entity, classify);
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改(买断租赁的商品)")
    @Transactional(rollbackFor = Exception.class)
    public Result updateBody(@RequestBody Orders entity,@RequestAttribute Long userId,Integer classify) {
        return wxService.updateBody(entity,userId,classify);
    }


    @Login
    @ApiOperation("补缴租金")
    @RequestMapping(value = "/paySupplement", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result paySupplement(@RequestAttribute("userId") Long userId, Long orderId,BigDecimal supplementMoney, Integer classify) throws Exception {
        return wxService.paySupplement(userId, orderId,supplementMoney,classify);
    }


/*    @Login
    @ApiOperation("微信购买优惠券")
    @PostMapping("/payCoupon")
    public Result payCoupon(@RequestAttribute("userId") Long userId, Long couponId, Integer buyNum, Integer payType, HttpServletRequest request) throws Exception {
        return wxService.payCoupon(userId, couponId, buyNum, payType, request);
    }*/

    @PostMapping("/notify")
    @ApiOperation("微信回调")
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
            String result = wxService.payBack(resXml, 1);
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

    @PostMapping("/notifyJsApi")
    @ApiOperation("微信回调")
    public String notifyJsApi(HttpServletRequest request) {
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
            String result = wxService.payBack(resXml, 3);
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

    @PostMapping("/notifyMp")
    @ApiOperation("微信回调")
    public String notifyMp(HttpServletRequest request) {
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
            String result = wxService.payBack(resXml, 2);
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

}

package com.sqx.pay;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@Api(value = "订单中心", tags = {"订单中心"})
@RequestMapping(value = "/admin/payDetails/")
public class PayDetailsController {
    @Autowired
    private PayDetailsService payDetailsService;

    @PostMapping("refundPayDetails")
    @ApiOperation("退款")
    public Result refundPayDetails(Long orderId, BigDecimal money,String remark){
        return payDetailsService.refundPayDetails(orderId,money,remark);
    }
}

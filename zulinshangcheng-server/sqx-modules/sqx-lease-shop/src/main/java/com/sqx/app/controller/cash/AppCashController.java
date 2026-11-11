package com.sqx.app.controller.cash;
;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.cash.ICashService;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/5/15
 */
@Slf4j
@RestController
@Api(value = "提现", tags = {"提现"})
@RequestMapping(value = "/app/cash")
public class AppCashController {

    /**
     * 提现记录
     */
    @Autowired
    private ICashService cashOutService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;


    @Login
    @GetMapping(value = "/cashMoney")
    @ApiOperation("发起提现")
    public Result cashMoney(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                            @ApiParam("金额") BigDecimal money,
                            @ApiParam("1支付宝 2微信小程序  3微信公众号") Integer classify) {
        return cashOutService.cashMoney(userId, money, classify);
    }

    @Login
    @RequestMapping(value = "/selectUserRechargeByUserId", method = RequestMethod.GET)
    @ApiOperation("查询某个用户充值信息列表")
    @ResponseBody
    public Result selectUserRechargeByUserId(@ApiParam("页码")int page,
                                             @ApiParam("条数")int limit,
                                             @ApiParam("开始时间")String startTime,
                                             @ApiParam("结束时间")String endTime,
                                             @ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                             @ApiParam("0待支付 1支付成功 2失败") Integer state) {
        return Result.success(payDetailsService.selectPayDetails(page, limit, startTime, endTime, userId, state));
    }

    @Login
    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.GET)
    @ApiOperation("查询提现记录列表")
    @ResponseBody
    public Result selectHelpProfit(@ApiParam("页码")int page,
                                   @ApiParam("条数")int limit,
                                   @ApiParam("用户id")@RequestAttribute("userId") Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", limit);
        map.put("userId", userId);
        PageUtils pageUtils = cashOutService.selectCashOutList(map);
        return Result.success(pageUtils);
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(@ApiParam("页码")Integer page,
                                        @ApiParam("条数")Integer limit,
                                        @ApiParam("用户id")@RequestAttribute("userId") Long userId,
                                        @ApiParam("用途 0充值余额 1支付订单 3下级分成 4退款 5信用分 6金币") Integer classify,
                                        @ApiParam("类型 1增加 2减少") Integer type) {
        return userMoneyDetailsService.queryUserDetails(page, limit, userId, classify, type);
    }

//    @Login
//    @PostMapping("refundPayDetails")
//    @ApiOperation("退款")
//    public Result refundPayDetails( @ApiParam("用户id")@RequestAttribute("userId") Long userId,
//                                    @ApiParam(value = "退款id")Long payId){
//        return payDetailsService.refundPayDetailsByUserId(userId,payId);
//    }

}
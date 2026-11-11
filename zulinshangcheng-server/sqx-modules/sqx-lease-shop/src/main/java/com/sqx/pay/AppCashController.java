//package com.sqx.pay;
//
//import com.sqx.auth.app.annotation.Login;
//import com.sqx.common.core.domain.Result;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.domain.repository.service.pay.CashOutService;
//import com.sqx.domain.repository.service.pay.PayDetailsService;
//import com.sqx.domain.repository.service.sys.UserMoneyDetailsService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author fang
// * @date 2020/5/15
// */
//@Slf4j
//@RestController
//@Api(value = "提现", tags = {"提现"})
//@RequestMapping(value = "/app/cash")
//public class AppCashController {
//
//    /** 提现记录 */
//    @Autowired
//    private CashOutService cashOutService;
//    @Autowired
//    private PayDetailsService payDetailsService;
//    @Autowired
//    private UserMoneyDetailsService userMoneyDetailsService;
//
//
//    @Login
//    @GetMapping(value = "/cashMoney")
//    @ApiOperation("发起提现")
//    public Result cashMoney(@RequestAttribute("userId") Long userId, BigDecimal money, Integer classify)
//    {
//        return cashOutService.cashMoney(userId,money,classify);
//    }
//
//    @Login
//    @RequestMapping(value = "/selectUserRechargeByUserId", method = RequestMethod.GET)
//    @ApiOperation("查询某个用户充值信息列表")
//    @ResponseBody
//    public Result selectUserRechargeByUserId(int page, int limit, String startTime, String endTime, @RequestAttribute("userId") Long userId, Integer state){
//        return Result.success(payDetailsService.selectPayDetails(page,limit,startTime,endTime,userId,state));
//    }
//
//    @Login
//    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.GET)
//    @ApiOperation("查询提现记录列表")
//    @ResponseBody
//    public Result selectHelpProfit(int page, int limit, @RequestAttribute("userId") Long userId){
//        Map<String,Object> map=new HashMap<>();
//        map.put("page",page);
//        map.put("limit",limit);
//        map.put("userId",userId);
//        PageUtils pageUtils = cashOutService.selectCashOutList(map);
//        return Result.success(pageUtils);
//    }
//
//    @Login
//    @ApiOperation("钱包明细")
//    @GetMapping("/queryUserMoneyDetails")
//    public Result queryUserMoneyDetails(Integer page, Integer limit, @RequestAttribute("userId") Long userId, Integer classify, Integer type) {
//        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
//    }
//
//
//}
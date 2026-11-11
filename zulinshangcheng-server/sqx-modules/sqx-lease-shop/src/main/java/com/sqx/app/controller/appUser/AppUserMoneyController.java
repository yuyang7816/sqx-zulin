package com.sqx.app.controller.appUser;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.service.app.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@RestController
@RequestMapping("/app/userMoney")
@Api(value = "用户钱包-管理端", tags = {"用户钱包-管理端"})
public class AppUserMoneyController {
    @Autowired
    private UserMoneyService userMoneyService;

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @GetMapping("/selectMyMoney")
    @Login
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@ApiParam(value = "用户id")@RequestAttribute Long userId) {

        return Result.success(userMoneyService.selectUserMoneyByUserId(userId));
    }

    /**
     * 我的收益
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的收益")
    public Result selectMyProfit(@ApiParam(value = "用户id")@RequestAttribute Long userId) {
        return Result.success(userMoneyService.selectMyProfit(userId));
    }

    /**
     * 余额明细
     */
    @GetMapping("/balanceDetailed")
    @Login
    @ApiOperation("余额明细")
    public Result balanceDetailed(@ApiParam(value = "用户id")@RequestAttribute Long userId,
                                  @ApiParam(value = "页码")Long page,
                                  @ApiParam(value = "条数")Long limit,
                                  @ApiParam(value = "钱包详情") UserMoneyDetails userMoneyDetails) {
        userMoneyDetails.setUserId(userId);
        return Result.success(userMoneyService.balanceDetailed(page,limit, userMoneyDetails));
    }



    @Login
    @PostMapping("/rechargeMoney")
    @ApiOperation("充值余额")
    public Result RechargeMoney(@ApiParam(value = "用户id")@RequestAttribute Long userId,
                                @ApiParam(value = "余额") BigDecimal money,
                                @ApiParam(value = "支付方式：123微信  45支付宝") Integer classify) throws Exception {
        return userMoneyService.rechargeMoney(userId, money, classify);
    }

    @Login
    @PostMapping(value = "moneyPayIndent")
    @ApiOperation("余额支付订单")
    public Result moneyPayIndent(@ApiParam(value = "用户id")@RequestAttribute Long userId,
                                 @ApiParam(value = "订单id")Long orderId) {
        return userMoneyService.moneyPayIndent(userId, orderId);
    }

}

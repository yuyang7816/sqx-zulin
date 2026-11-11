package com.sqx.system.userMoneyDetails;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Details")
@AllArgsConstructor
@Api("钱包明细")
public class UserMoneyDetailsController {
    private UserMoneyDetailsService userMoneyDetailsService;

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(@ApiParam(value = "页码") Integer page,
                                        @ApiParam(value = "条数") Integer limit,
                                        @ApiParam(value = "用户id") Long userId,
                                        @ApiParam(value = "用途 0充值余额 1支付订单 3下级分成 4退款 5信用分 6金币") Integer classify,
                                        @ApiParam(value = "类型 1增加 2减少") Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId, classify, type);
    }

    @ApiOperation("信用分明细")
    @GetMapping("/queryCreditScore")
    public Result queryCreditScore(@ApiParam(value = "页码") Integer page,
                                   @ApiParam(value = "条数") Integer limit,
                                   @ApiParam(value = "用户id") Long userId,
                                   @ApiParam(value = "用途 0充值余额 1支付订单 3下级分成 4退款 5信用分 6金币") Integer classify,
                                   @ApiParam(value = "类型 1增加 2减少") Integer type) {
        return userMoneyDetailsService.queryCreditScore(page, limit, userId, 5, type);
    }

    @ApiOperation("金币明细")
    @GetMapping("/queryGoldNum")
    public Result queryGoldNum(@ApiParam(value = "页码") Integer page,
                               @ApiParam(value = "条数") Integer limit,
                               @ApiParam(value = "用户id") Long userId,
                               @ApiParam(value = "用途 0充值余额 1支付订单 3下级分成 4退款 5信用分 6金币") Integer classify,
                               @ApiParam(value = "类型 1增加 2减少") Integer type) {
        return userMoneyDetailsService.queryGoldNum(page, limit, userId, 6, type);
    }


}

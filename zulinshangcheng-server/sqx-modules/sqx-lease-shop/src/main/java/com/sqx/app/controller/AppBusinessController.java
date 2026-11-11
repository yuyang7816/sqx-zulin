package com.sqx.app.controller;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Business;
import com.sqx.domain.repository.service.sys.BusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/19 13:56
 */
@RestController
@RequestMapping("/app/business/")
@Api(value = "商家入驻-用户端", tags = {"商家入驻-用户端"})
public class AppBusinessController {

    @Autowired
    private BusinessService businessService;

    @Login
    @PostMapping("addBusiness")
    @ApiOperation("商家申请入驻信息")
    public Result addBusiness(Business business,@RequestAttribute Long userId){
        business.setUserId(userId);
        return businessService.addBusiness(business);
    }
    @Login
    @PostMapping("repeatedBusiness")
    @ApiOperation("重新提交商家申请入驻信息")
    public Result repeatedBusiness(Business business,@RequestAttribute Long userId){
        business.setUserId(userId);
        return businessService.repeatedBusiness(business);
    }
    @Login
    @GetMapping("selectBusinessByUserId")
    @ApiOperation("根据userId查询商家入驻信息")
    public Result selectBusinessByUserId(@RequestAttribute Long userId){
        return businessService.selectBusinessByUserId(userId);
    }
}

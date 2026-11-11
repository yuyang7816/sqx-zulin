package com.sqx.app.controller.advert;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfAdvert.AdvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 18:10
 */
@RestController
@Api(value="自营商城广告位",tags={"自营商城广告位管理"})
@RequestMapping(value = "/app/advert")
public class AppAdvertController {

    @Autowired
    private AdvertService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer type) {
        return service.findAll(type);
    }


}

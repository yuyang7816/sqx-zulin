package com.sqx.system.selfBanner;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfBanner;
import com.sqx.domain.repository.service.selfBanner.SlefBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城Banner位",tags={"自营商城Banner位"})
@RequestMapping(value = "/selfBanner")
public class SelfBannerController {
    @Autowired
    private SlefBannerService service;

    @IgnoreSecurity
    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer type) {
        return service.findAll(type);
    }

    @IgnoreSecurity
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfBanner entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfBanner entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

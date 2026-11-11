package com.sqx.system.selfGroup;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfGroupPink.SelfGroupPinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="自营商城拼团购买",tags={"自营商城拼团购买"})
@RequestMapping(value = "/selfGroupPink")
public class SelfGroupPinkController {
    @Autowired
    private SelfGroupPinkService service;

    @GetMapping("/list")
    @ApiOperation("后台管理列表")
    public Result findAll(Integer page, Integer size, Integer status) {
        return service.findAll(page, size, status);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @IgnoreSecurity
    @PostMapping("/findByGroupId")
    @ApiOperation("可拼的团")
    public Result findByGroupId(Long groupId) {
        return service.findByGroupId(groupId);
    }
}

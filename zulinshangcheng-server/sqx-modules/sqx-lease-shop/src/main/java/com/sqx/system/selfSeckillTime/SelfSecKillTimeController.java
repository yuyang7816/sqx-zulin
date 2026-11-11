package com.sqx.system.selfSeckillTime;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfSecKillTime;
import com.sqx.domain.repository.service.selfSeckillTime.SelfSeckillTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城秒杀时段管理",tags={"自营商城秒杀时段管理"})
@RequestMapping(value = "/selfSecKillTime")
public class SelfSecKillTimeController {
    @Autowired
    private SelfSeckillTimeService service;

    @GetMapping("/list")
    @ApiOperation("后台管理列表")
    public Result findAll(Integer page, Integer size) {
        return service.findAll(page, size);
    }

    @IgnoreSecurity
    @GetMapping("/allList")
    @ApiOperation("用户端列表")
    public Result allList() {
        return service.allList();
    }


    @IgnoreSecurity
    @GetMapping("/result")
    @ApiOperation("所有时段封装")
    public Result result() {
        return service.result();
    }

    @IgnoreSecurity
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfSecKillTime entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfSecKillTime entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

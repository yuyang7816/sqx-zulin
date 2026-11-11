package com.sqx.system.selfGroup;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGroup;
import com.sqx.domain.repository.service.selfGroup.SelfGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城拼团商品管理",tags={"自营商城拼团商品管理"})
@RequestMapping(value = "/selfGroup")
public class SelfGroupController {
    @Autowired
    private SelfGroupService service;

    @GetMapping("/list")
    @ApiOperation("后台管理列表")
    public Result findAll(Integer page, Integer size, String title) {
        return service.findAll(page, size, title);
    }

    @IgnoreSecurity
    @GetMapping("/userList")
    @ApiOperation("用户端列表")
    public Result userList(Integer page, Integer size, @ApiParam("排序字段 综合createTime 拼团销量groupNumber 佣金比例commissionPrice 超低价price")@RequestParam(required = false) String sort) {
        return service.userList(page, size, sort);
    }

    @IgnoreSecurity
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfGroup entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfGroup entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @GetMapping("/updateStatus")
    @ApiOperation("上下架")
    public Result updateStatus(Long id) {
        return service.updateStatus(id);
    }

    @ApiOperation(value = "多规格生成sku")
    @GetMapping(value = "/isFormatSku")
    public Result isFormatSku(Long goodsId){
        return service.isFormatSku(goodsId);
    }

    @ApiOperation(value = "单规格生成sku")
    @GetMapping(value = "/onlyFormatSku")
    public Result onlyFormatSku(Long goodsId){
        return service.onlyFormatSku(goodsId);
    }

    @ApiOperation(value = "回显拼团sku")
    @GetMapping(value = "/showSku")
    public Result showSku(Long groupId){
        return service.showSku(groupId);
    }

}

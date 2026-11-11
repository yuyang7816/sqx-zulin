package com.sqx.system.selfSecKill;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfSecKill;
import com.sqx.domain.repository.service.selfSeckill.SelfSeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城秒杀",tags={"自营商城秒杀商品"})
@RequestMapping(value = "/selfSecKill")
public class SelfSecKillController {
    @Autowired
    private SelfSeckillService service;

    @GetMapping("/list")
    @ApiOperation("后台管理列表")
    public Result findAll(Integer page, Integer size, String title) {
        return service.findAll(page, size, title);
    }

    @GetMapping("/userList")
    @ApiOperation("用户端列表")
    public Result userList(Integer page, Integer size, Long secKillTimeId) {
        return service.userList(page, size, secKillTimeId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfSecKill entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfSecKill entity) {
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
    public Result showSku(Long secKillId){
        return service.showSku(secKillId);
    }

}

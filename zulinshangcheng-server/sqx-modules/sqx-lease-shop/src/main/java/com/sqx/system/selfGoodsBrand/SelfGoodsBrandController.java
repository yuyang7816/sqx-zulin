package com.sqx.system.selfGoodsBrand;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsBrand;
import com.sqx.domain.repository.service.selfGoodsBrand.SelfGoodsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城商品品牌",tags={"自营商城商品品牌"})
@RequestMapping(value = "/selfGoodsBrand")
public class SelfGoodsBrandController {
    @Autowired
    private SelfGoodsBrandService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size) {
        return service.findAll(page, size);
    }

    @GetMapping("/result")
    @ApiOperation("品牌封装列表")
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
    public Result saveBody(@RequestBody SelfGoodsBrand entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfGoodsBrand entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

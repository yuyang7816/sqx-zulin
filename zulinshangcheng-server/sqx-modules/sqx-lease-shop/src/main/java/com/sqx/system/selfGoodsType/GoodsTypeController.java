package com.sqx.system.selfGoodsType;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsType;
import com.sqx.domain.repository.service.selfGoodsType.GoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城分类",tags={"自营商城分类管理"})
@RequestMapping(value = "/goodsType")
public class GoodsTypeController {
    @Autowired
    private GoodsTypeService service;


    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(String name) {
        return service.findAll(name);
    }


    @GetMapping("/result")
    @ApiOperation("商品分类数据封装")
    public Result findAllResult() {
        return service.findAllResult();
    }


    @GetMapping("/result1")
    @ApiOperation("一级分类数据封装")
    public Result findAllResult1() {
        return service.findAllResult1();
    }



    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody GoodsType entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody GoodsType entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}

package com.sqx.app.controller.goodsType;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfGoodsType.GoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 18:00
 */
@RestController
@Api(value="商城分类---用户端",tags={"商城分类管理---用户端"})
@RequestMapping(value = "/app/goodsType")
public class AppGoodsTypeController {

    @Autowired
    private GoodsTypeService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(String name) {
        return service.findAll(name);
    }

    @GetMapping("/result1")
    @ApiOperation("一级分类数据封装")
    public Result findAllResult1() {
        return service.findAllResult1();
    }

}

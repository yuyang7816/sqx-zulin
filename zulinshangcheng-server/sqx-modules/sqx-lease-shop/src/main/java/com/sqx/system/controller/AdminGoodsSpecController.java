package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsSpecifications;
import com.sqx.domain.repository.service.sys.GoodsSpecificationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/20 18:06
 */
@RestController
@RequestMapping("/admin/goodsSpec/")
@Api(value = "商品规格管理-管理端", tags = {"商品规格管理-管理端"})
public class AdminGoodsSpecController {

    @Autowired
    private GoodsSpecificationsService goodsSpecificationsService;

    @PostMapping("addGoodsSpecifications")
    @ApiOperation("添加或修改商品规格")
    public Result addGoodsSpecifications(GoodsSpecifications goodsSpecifications) {
        return goodsSpecificationsService.addGoodsSpecifications(goodsSpecifications);
    }

    @PostMapping("deleteGoodsSpecifications")
    @ApiOperation("删除商品规格")
    public Result deleteGoodsSpecifications(Long id) {
        return goodsSpecificationsService.deleteGoodsSpecifications(id);
    }

    @GetMapping("getGoodsSpecificationsList")
    @ApiOperation("获取商品规格列表")
    public Result getGoodsSpecificationsList(Integer page, Integer limit, GoodsSpecifications goodsSpecifications) {
        return Result.success(goodsSpecificationsService.getGoodsSpecificationsList(page, limit, goodsSpecifications));
    }

    @GetMapping("selectById")
    @ApiOperation("根据id查询商品规格")
    public Result selectById(Long id) {
        return goodsSpecificationsService.selectBySpecId(id);
    }

}

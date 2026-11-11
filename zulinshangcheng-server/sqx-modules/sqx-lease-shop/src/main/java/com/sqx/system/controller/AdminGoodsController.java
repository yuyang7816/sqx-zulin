//package com.sqx.system.controller;
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.sys.Goods;
//import com.sqx.domain.repository.service.sys.GoodsService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @description：TODO
// * @author： qiao
// * @create： 2024/9/19 14:48
// */
//@RestController
//@RequestMapping("/admin/goods/")
//@Api(value = "商品管理-管理端",tags = {"商品管理-管理端"})
//public class AdminGoodsController {
//
//    @Autowired
//    private GoodsService goodsService;
//
//    @PostMapping("addGoods")
//    @ApiOperation("添加或修改商品信息")
//    public Result addGoods(Goods goods){
//        return goodsService.addGoodsByAdmin(goods);
//    }
//
//    @PostMapping("deleteGoods")
//    @ApiOperation("删除商品信息")
//    public Result deleteGoods(Long id){
//        return goodsService.deleteGoods(id);
//    }
//    @GetMapping("getGoodsList")
//    @ApiOperation("查询商品列表")
//    public Result getGoodsList(Integer page, Integer limit, Goods goods){
//        return Result.success(goodsService.getGoodsList(page, limit, goods));
//    }
//
//    @PostMapping("examineGoods")
//    @ApiOperation("商品审核")
//    public Result examineGoods(Long id, Integer status){
//        return goodsService.examineGoods(id, status);
//    }
//}

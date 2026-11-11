package com.sqx.system.goodsAccessory;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsAccessory;
import com.sqx.domain.repository.service.sys.GoodsAccessoryService;
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
 * @create： 2024/12/24 15:16
 */
@RestController
@RequestMapping("/admin/goodsAccessory/")
@Api(value = "商品配件相关---管理端", tags = {"商品配件相关---管理端"})
public class GoodsAccessoryController {

    @Autowired
    private GoodsAccessoryService goodsAccessoryService;

    @ApiOperation(value = "添加或修改商品配件")
    @PostMapping("addGoodsAccessory")
    public Result addGoodsAccessory(GoodsAccessory goodsAccessory) {
        return goodsAccessoryService.addGoodsAccessory(goodsAccessory);
    }

    @ApiOperation(value = "删除商品配件")
    @PostMapping("deleteGoodsAccessory")
    public Result deleteGoodsAccessory(Long id) {
        return goodsAccessoryService.deleteGoodsAccessory(id);
    }

    @ApiOperation(value = "查询商品配件")
    @GetMapping("selectGoodsAccessory")
    public Result selectGoodsAccessory(Long id) {
        return goodsAccessoryService.selectGoodsAccessory(id);
    }

    @IgnoreSecurity
    @ApiOperation(value = "查询商品配件列表")
    @GetMapping("selectGoodsAccessoryList")
    public Result selectGoodsAccessoryList(Integer page, Integer limit, GoodsAccessory goodsAccessory) {
        return goodsAccessoryService.selectGoodsAccessoryList(page, limit, goodsAccessory);
    }

    @ApiOperation(value = "根据typeId查询商品配件列表")
    @GetMapping("selectListByTypeId")
    public Result selectListByTypeId(Long typeId) {
        return goodsAccessoryService.selectListByTypeId(typeId);
    }
}

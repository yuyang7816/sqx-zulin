package com.sqx.system.goodsGift;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsGift;
import com.sqx.domain.repository.service.sys.GoodsGiftService;
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
 * @create： 2024/12/24 15:20
 */
@RestController
@RequestMapping("/admin/goodsGift/")
@Api(value = "商品赠品相关---管理端", tags = {"商品赠品相关---管理端"})
public class GoodsGiftController {

    @Autowired
    private GoodsGiftService goodsGiftService;

    @ApiOperation(value = "添加或修改商品赠品")
    @PostMapping("addGoodsGift")
    public Result addGoodsGift(GoodsGift goodsGift) {
        return goodsGiftService.addGift(goodsGift);
    }

    @ApiOperation(value = "删除商品赠品")
    @PostMapping("deleteGoodsGift")
    public Result deleteGoodsGift(Long id) {
        return goodsGiftService.deleteGift(id);
    }

    @ApiOperation(value = "查询商品赠品")
    @GetMapping("selectGoodsGift")
    public Result selectGoodsGift(Long id) {
        return goodsGiftService.selectGift(id);
    }

    @ApiOperation(value = "查询商品赠品列表")
    @GetMapping("selectGoodsGiftList")
    public Result selectGoodsGiftList(Integer page, Integer limit, GoodsGift goodsGift) {
        return goodsGiftService.selectGiftList(page, limit, goodsGift);
    }

    @ApiOperation(value = "根据typeId查询商品赠品列表")
    @GetMapping("selectListByTypeId")
    public Result selectListByTypeId(Long typeId) {
        return goodsGiftService.selectListByTypeId(typeId);
    }
}

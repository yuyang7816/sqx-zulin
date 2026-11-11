package com.sqx.app.controller.goods;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfGoods.GoodsService;
import com.sqx.domain.repository.service.selfGoodsRule.SelfGoodsRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 14:55
 */
@RestController
@Api(value = "自营商城商品相关", tags = {"自营商城商品相关"})
@RequestMapping(value = "/app/goods")
public class AppGoodsController {

    @Autowired
    private GoodsService service;
    @Autowired
    private SelfGoodsRuleService ruleService;



    /**
     * 用户端
     * 首页查询分类专区的品牌下的商品列表
     *
     * @param page
     * @param size
     * @param typeId
     * @param brandId
     * @return
     */
    @ApiOperation(value = "首页查询分类专区的品牌下的商品列表")
    @GetMapping(value = "/goodsInfoByBrand")
    public Result goodsInfoByBrand(@ApiParam("页码") Integer page,
                                   @ApiParam("条数") Integer size,
                                   @ApiParam("分类") Long typeId,
                                   @ApiParam("品牌id") Long brandId,
                                   @ApiParam("1:价格正序 2:价格倒序") Integer type,
                                   @ApiParam("商品搜索") String title) {
        return service.goodsInfoByBrand(page, size, typeId, brandId, type, title);
    }

    /**
     * 用户端
     * 查询标签分类
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "查询标签分类")
    @GetMapping(value = "/selectByLabelType")
    public Result selectByLabelType(@ApiParam("页码") Integer page,
                                    @ApiParam("条数") Integer size) {
        return service.selectByLabelType(page, size);
    }

    /**
     * 用户端
     * 根据标签id查询商品列表
     *
     * @param page
     * @param size
     * @param labelId
     * @return
     */
    @ApiOperation(value = "根据标签id查询商品列表")
    @GetMapping(value = "/goodListByLabelId")
    public Result goodListByLabelId(@ApiParam("页码") Integer page,
                                    @ApiParam("条数") Integer size,
                                    @ApiParam("标签id") Long labelId) {
        return service.goodListByLabelId(page, size, labelId);
    }

    /**
     * 根据品牌id查询商品列表
     * @param page
     * @param size
     * @param title
     * @param type
     * @param threeTypeId
     * @return
     */
    @ApiOperation(value = "根据品牌id来查询商品列表")
    @GetMapping(value = "/goodsListByBrandId")
    public Result goodsListByBrandId(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer size,
                                     @ApiParam("title")String title,
                                     @ApiParam("品牌id")Long brandId,
                                     @ApiParam("排序方式")Integer type,
                                     @ApiParam("三级类型id") Long threeTypeId) {
        return service.goodsListByBrandId(page, size,title,brandId,type, threeTypeId);
    }

    @GetMapping("/selectedGood")
    @ApiOperation("首页热爆租品")
    public Result selectedGood(Integer page, Integer size, Long labelId) {
        return service.selectedGood(page, size, labelId);
    }

    @GetMapping("/selectedRecommendations")
    @ApiOperation("首页精选推荐")
    public Result selectedRecommendations(Integer page, Integer size,
                                          @ApiParam("一级类型id") Long typeId,
                                          @ApiParam("1:价格正序 2:价格倒序") Integer type,
                                          @ApiParam("搜索商品") String title) {
        return service.selectedRecommendations(page, size, typeId, type, title);
    }

    @RequestMapping(value = "findById", method = RequestMethod.GET)
    @ApiOperation("查询商品详情，如果登录前端传userId，未登录就不传")
    public Result findById(Long id,Long userId) {
        return service.findById(id,userId);
    }


}

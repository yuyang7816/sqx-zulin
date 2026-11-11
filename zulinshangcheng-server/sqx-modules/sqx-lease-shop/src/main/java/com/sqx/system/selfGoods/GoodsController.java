package com.sqx.system.selfGoods;

import com.sqx.auth.app.annotation.Login;
import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.SelfGoodsAttr;
import com.sqx.domain.repository.service.selfGoods.GoodsService;
import com.sqx.domain.repository.service.selfGoodsRule.SelfGoodsRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "自营商城商品相关", tags = {"自营商城商品相关"})
@RequestMapping(value = "/goods")
public class GoodsController {
    @Autowired
    private GoodsService service;
    @Autowired
    private SelfGoodsRuleService ruleService;

    @IgnoreSecurity
    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size,
                          @ApiParam("商品标题：可搜索") @RequestParam(required = false) String title,
                          @ApiParam("商品类型：类型名称") @RequestParam(required = false) String type,
                          Long twoTypeId,Long threeTypeId,
                          @ApiParam("商品状态：0全部 1上架 2下架") @RequestParam(required = false) String status,
                          String activityId, String notActivityId,
                          @ApiParam("是否积分商品：1积分商城商品") @RequestParam(required = false) String isJiFenGoods) {
        return service.findAll(page, size, title, type,twoTypeId,threeTypeId, status, isJiFenGoods, activityId, notActivityId);
    }

    @IgnoreSecurity
    @GetMapping("/goodsList")
    @ApiOperation("其他商品列表")
    public Result goodsVirtualList(Integer page, Integer size,
                                   @ApiParam("商品标题：可搜索") @RequestParam(required = false) String title,
                                   @ApiParam("商品类型：类型名称") @RequestParam(required = false) String type,
                                   @ApiParam("商品状态：0全部 1上架 2下架") @RequestParam(required = false) Integer status,
                                   @ApiParam("虚拟类型：0全部 1普通商品 2虚拟商品 3核销商品") @RequestParam(required = false) Integer isExpress,
                                   @ApiParam("是否积分商品：1积分商城商品") @RequestParam(required = false) Integer isJiFenGoods) {
        return service.goodsVirtualList(page, size, title, type, status, isExpress, isJiFenGoods);
    }

    @IgnoreSecurity
    @GetMapping("/userList")
    @ApiOperation("用户端商品列表")
    public Result userList(Integer page, Integer size,
                           @ApiParam("商品类型：类型名称") @RequestParam(required = false) String type,
                           @ApiParam("品牌id") @RequestParam(required = false) String brandId,
                           @ApiParam("商品标题：可搜索") @RequestParam(required = false) String title,
                           @ApiParam("排序字段 综合createAt 销量sales 佣金比例commissionPrice 超低价price") @RequestParam(required = false) String sort,
                           @ApiParam("是否积分商品：0虚拟商品列表固定传0 1积分商城商品") @RequestParam(required = false) Integer isJiFenGoods) {
        return service.findAllByUser(page, size, type, brandId, title, sort, isJiFenGoods);
    }

    @PostMapping("/updateGoodsActivity")
    @ApiOperation("修改商品活动")
    public Result updateGoodsActivity(Integer type, Long goodsId, String activityId) {
        return service.updateGoodsActivity(type, goodsId, activityId);
    }

    @IgnoreSecurity
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

//    @Login
    @RequestMapping(value = "findById", method = RequestMethod.GET)
    @ApiOperation("查询商品详情，如果登录前端传userId，未登录就不传")
    public Result findById(Long id,Long userId) {
        return service.findById(id,userId);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody Goods entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody Goods entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @IgnoreSecurity
    @GetMapping("/brandList")
    @ApiOperation("用户端品牌列表")
    public Result brandList(@ApiParam("搜索内容") @RequestParam(required = false) String title) {
        return service.searchBrandList(title);
    }

    @IgnoreSecurity
    @GetMapping("/selectGoods")
    @ApiOperation("精选好物")
    public Result selectGoods(Integer page, Integer size,
                              @ApiParam("排序字段 综合createAt 销量sales 佣金比例commissionPrice 超低价price") @RequestParam(required = false) String sort) {
        return service.selectGoods(page, size, sort);
    }

    @IgnoreSecurity
    @GetMapping("/selectedGood")
    @ApiOperation("首页热爆租品")
    public Result selectedGood(Integer page, Integer size, Long labelId) {
        return service.selectedGood(page, size, labelId);
    }

    @IgnoreSecurity
    @GetMapping("/selectedRecommendations")
    @ApiOperation("首页精选推荐")
    public Result selectedRecommendations(Integer page, Integer size,
                                          @ApiParam("一级类型id") Long typeId,
                                          @ApiParam("1:价格正序 2:价格倒序") Integer type,
                                          @ApiParam("搜索商品") String title) {
        return service.selectedRecommendations(page, size, typeId, type, title);
    }

    @IgnoreSecurity
    @GetMapping("/selling")
    @ApiOperation("热卖榜单")
    public Result selling(Integer page, Integer size,
                          @ApiParam("排序字段 综合createAt 销量sales 佣金比例commissionPrice 超低价price") @RequestParam(required = false) String sort) {
        return service.selling(page, size, sort);
    }

    @IgnoreSecurity
    @GetMapping("/homeGoods")
    @ApiOperation("首页商品")
    public Result homeGoods(Integer page, Integer size) {
        return service.homeGoods(page, size);
    }

    @IgnoreSecurity
    @GetMapping("/news")
    @ApiOperation("每日上新")
    public Result news(Integer page, Integer size,
                       @ApiParam("排序字段 综合createAt 销量sales 佣金比例commissionPrice 超低价price") @RequestParam(required = false) String sort) {
        return service.news(page, size, sort);
    }

    @IgnoreSecurity
    @GetMapping("/recommend")
    @ApiOperation("热爆租品")
    public Result recommend(Integer page, Integer size,
                            @ApiParam("排序字段 综合createAt 销量sales 佣金比例commissionPrice 超低价price") @RequestParam(required = false) String sort) {
        return service.recommend(page, size, sort);
    }

    @GetMapping("/addRecommend")
    @ApiOperation("后台管理添加每日推荐")
    public Result addRecommend(Long id) {
        return service.addRecommend(id);
    }

    @GetMapping("/deleteRecommend")
    @ApiOperation("后台管理删除每日推荐")
    public Result deleteRecommend(String ids) {
        return service.deleteRecommend(ids);
    }

    @GetMapping("/addSelectGoods")
    @ApiOperation("后台管理添加精选好物")
    public Result addSelectGoods(Long id) {
        return service.addSelectGoods(id);
    }

    @GetMapping("/deleteSelectGoods")
    @ApiOperation("后台管理删除精选好物")
    public Result addSelectGoods(String ids) {
        return service.deleteSelectGoods(ids);
    }

    @GetMapping("/info")
    @ApiOperation("商品规格选项")
    public Result info() {
        return ruleService.info();
    }

    @ApiOperation(value = "多规格生成sku")
    @PostMapping(value = "/isFormatAttr")
    public Result isFormatSku(@RequestBody SelfGoodsAttr attr,
                              @ApiParam("商品图片") @RequestParam(required = false) String coverImg,
                              @ApiParam("原价") @RequestParam(required = false) String originalPrice,
                              @ApiParam("售价") @RequestParam(required = false) String price,
                              @ApiParam("会员价") @RequestParam(required = false) String memberPrice,
                              @ApiParam("日租价")@RequestParam(required = false) String dailyRent,
                              @ApiParam("会员日租价")@RequestParam(required = false)String memberRent,
                              @ApiParam("库存")@RequestParam(required = false)Integer stock,
                              @ApiParam("新旧程度")@RequestParam(required = false)String oldDegree) {
        return service.isFormatAttr(attr, coverImg, originalPrice, price, memberPrice,dailyRent,memberRent,stock,oldDegree);
    }

    @ApiOperation(value = "单规格生成sku")
    @GetMapping(value = "/onlyFormatAttr")
    public Result onlyFormatSku(@ApiParam("商品图片") @RequestParam(required = false) String coverImg,
                                @ApiParam("原价") @RequestParam(required = false) String originalPrice,
                                @ApiParam("售价") @RequestParam(required = false) String price,
                                @ApiParam("会员价") @RequestParam(required = false) String memberPrice,
                                @ApiParam("日租价")@RequestParam(required = false) String dailyRent,
                                @ApiParam("会员日租价")@RequestParam(required = false)String memberRent,
                                @ApiParam("库存")@RequestParam(required = false)Integer stock,
                                @ApiParam("新旧程度")@RequestParam(required = false)String oldDegree) {
        return service.onlyFormatAttr(coverImg, originalPrice, price, memberPrice,dailyRent,memberRent,stock,oldDegree);
    }

    @ApiOperation(value = "回显属性")
    @GetMapping(value = "/formatAttr")
    public Result formatAttr(Long goodsId) {
        return service.formatAttr(goodsId);
    }

    @ApiOperation(value = "回显规格")
    @GetMapping(value = "/findAttrValue")
    public Result findAttrValue(Long goodsId) {
        return service.findAttrValue(goodsId);
    }

    @ApiOperation(value = "商品上下架")
    @GetMapping(value = "/updateStatus")
    public Result updateStatus(Long goodsId) {
        return service.updateStatus(goodsId);
    }

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
     * @param threeTypeId
     * @return
     */
    @ApiOperation(value = "根据品牌id来查询商品列表")
    @GetMapping(value = "/goodsListByBrandId")
    public Result goodsListByBrandId(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer size,
                                     @ApiParam("标题") String title,
                                     Long brandId,
                                     Integer type,
                                     @ApiParam("三级类型id") Long threeTypeId) {
        return service.goodsListByBrandId(page, size,title,brandId,type, threeTypeId);
    }


}

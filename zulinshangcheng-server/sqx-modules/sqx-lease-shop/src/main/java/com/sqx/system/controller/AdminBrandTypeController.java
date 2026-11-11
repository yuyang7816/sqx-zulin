package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.BrandType;
import com.sqx.domain.repository.service.sys.BrandTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/14 17:29
 */
@RestController
@RequestMapping("/admin/brandType/")
@Api(value = "品牌分类-管理端",tags = {"品牌分类-管理端"})
public class AdminBrandTypeController {

    @Autowired
    private BrandTypeService brandTypeService;

    @PostMapping("addBrandType")
    @ApiOperation("添加或修改品牌分类")
    public Result addBrandType(BrandType brandType){
        return brandTypeService.addBrandType(brandType);
    }

    @PostMapping("deleteBrandType")
    @ApiOperation("删除品牌分类")
    public Result deleteBrandType(Long id){
        return brandTypeService.deleteBrandType(id);
    }

    @GetMapping("getBrandTypeList")
    @ApiOperation("获取品牌分类列表")
    public Result getBrandTypeList(Integer page,Integer limit, BrandType brandType){
        return Result.success(brandTypeService.getBrandTypeList(page,limit,brandType));
    }

    @GetMapping("selectById")
    @ApiOperation("根据id查询品牌分类")
    public Result selectById(Long id){
        return brandTypeService.selectById(id);
    }

    @GetMapping("selectBySectionId")
    @ApiOperation("根据专区id查询品牌分类")
    public Result selectBySectionId(Long sectionId){
        return brandTypeService.selectBySectionId(sectionId);
    }
}

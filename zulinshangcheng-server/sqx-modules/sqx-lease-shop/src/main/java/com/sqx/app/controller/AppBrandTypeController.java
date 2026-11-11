package com.sqx.app.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.sys.BrandTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/19 14:19
 */
@RestController
@RequestMapping("/app/brandType/")
@Api(value = "品牌分类-用户端",tags = {"品牌分类-用户端"})
public class AppBrandTypeController {

    @Autowired
    private BrandTypeService brandTypeService;

    @GetMapping("selectBySectionId")
    @ApiOperation("根据专区id查询品牌分类")
    public Result selectBySectionId(Long sectionId){
        return brandTypeService.selectBySectionId(sectionId);
    }
}

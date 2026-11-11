package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Section;
import com.sqx.domain.repository.service.sys.SectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/19 13:39
 */
@RestController
@RequestMapping("/admin/section/")
@Api(value = "专区分类-管理端", tags = {"专区分类-管理端"})
public class AdminSectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping("addSection")
    @ApiOperation("修改或添加专区分类")
    public Result addSection(Section section){
        return sectionService.addSection(section);
    }

    @PostMapping("deleteSection")
    @ApiOperation("删除专区分类")
    public Result deleteSection(Long id){
        return sectionService.deleteSection(id);
    }

    @GetMapping("getSectionList")
    @ApiOperation("获取专区分类列表")
    public Result getSectionList(Integer page,Integer limit, Section section){
        return Result.success(sectionService.getSectionList(page,limit,section));
    }

    @GetMapping("selectSection")
    @ApiOperation("根据id查询专区分类")
    public Result selectSection(Long id){
        return sectionService.selectSection(id);
    }

}

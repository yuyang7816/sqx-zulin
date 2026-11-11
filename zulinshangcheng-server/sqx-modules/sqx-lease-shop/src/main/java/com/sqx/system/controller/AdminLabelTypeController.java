package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.LabelType;
import com.sqx.domain.repository.service.sys.LabelTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/19 15:57
 */
@RestController
@RequestMapping("/admin/labelType/")
@Api(value = "标签相关-管理端",tags = {"标签相关-管理端"})
public class AdminLabelTypeController {

    @Autowired
    private LabelTypeService labelTypeService;

    @PostMapping("addLabelType")
    @ApiOperation("修改或添加标签信息")
    public Result addLabelType(LabelType labelType) {
        return labelTypeService.addLabelType(labelType);
    }

    @PostMapping("deleteLabelType")
    @ApiOperation("删除标签信息")
    public Result deleteLabelType(Long id) {
        return labelTypeService.deleteLabelType(id);
    }

    @RequestMapping("getLabelTypeList")
    @ApiOperation("获取标签列表")
    public Result getLabelTypeList(Integer page, Integer limit, LabelType labelType) {
        return Result.success(labelTypeService.getLabelTypeList(page, limit, labelType));
    }

    @RequestMapping("selectByLabelTypeId")
    @ApiOperation("根据id查询标签信息")
    public Result selectByLabelTypeId(Long id) {
        return labelTypeService.selectByLabelTypeId(id);
    }
}

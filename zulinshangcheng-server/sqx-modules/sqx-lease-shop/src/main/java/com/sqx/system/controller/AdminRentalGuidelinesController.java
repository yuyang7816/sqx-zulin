package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.RentalGuidelines;
import com.sqx.domain.repository.service.sys.RentalGuidelinesService;
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
 * @create： 2024/9/19 16:34
 */
@RestController
@RequestMapping("/admin/rentalGuidelines/")
@Api(value = "租赁须知相关-管理端", tags = {"租赁须知相关-管理端"})
public class AdminRentalGuidelinesController {

    @Autowired
    private RentalGuidelinesService rentalGuidelinesService;

    @PostMapping("addRentalGuidelines")
    @ApiOperation("修改或新增租赁须知")
    public Result addRentalGuidelines(RentalGuidelines rentalGuidelines) {
        return rentalGuidelinesService.addRentalGuidelines(rentalGuidelines);
    }

    @PostMapping("deleteRentalGuidelines")
    @ApiOperation("删除租赁须知")
    public Result deleteRentalGuidelines(Long id) {
        return rentalGuidelinesService.deleteRentalGuidelines(id);
    }

    @GetMapping("getRentalGuidelinesList")
    @ApiOperation("获取租赁须知列表")
    public Result getRentalGuidelinesList(Integer page, Integer limit, RentalGuidelines rentalGuidelines) {
        return Result.success(rentalGuidelinesService.getRentalGuidelinesList(page, limit, rentalGuidelines));
    }

    @GetMapping("selectByRentalGuidelinesId")
    @ApiOperation("根据id查询租赁须知")
    public Result selectByRentalGuidelinesId(Long id) {
        return rentalGuidelinesService.selectByRentalGuidelinesId(id);
    }
}

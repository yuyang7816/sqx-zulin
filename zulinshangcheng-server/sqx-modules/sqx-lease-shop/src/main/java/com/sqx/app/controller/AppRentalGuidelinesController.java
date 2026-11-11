package com.sqx.app.controller;

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
@RequestMapping("/app/rentalGuidelines/")
@Api(value = "租赁须知相关-用户端", tags = {"租赁须知相关-用户端"})
public class AppRentalGuidelinesController {

    @Autowired
    private RentalGuidelinesService rentalGuidelinesService;

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

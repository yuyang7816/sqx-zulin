package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Business;
import com.sqx.domain.repository.service.sys.BusinessService;
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
 * @create： 2024/9/19 14:11
 */
@RestController
@RequestMapping("/admin/business/")
@Api(value = "商家入驻管理-管理端", tags = {"商家入驻管理-管理端"})
public class AdminBusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("updateBusiness")
    @ApiOperation("修改商家入驻信息")
    public Result updateBusiness(Business business){
        return businessService.updateBusiness(business);
    }

    @PostMapping("deleteBusiness")
    @ApiOperation("删除商家入驻信息")
    public Result deleteBusiness(Long id){
        return businessService.deleteBusiness(id);
    }

    @GetMapping("getBusinessList")
    @ApiOperation("获取商家入驻列表")
    public Result getBusinessList(Integer page, Integer limit, Business business){
        return Result.success(businessService.getBusinessList(page,limit,business));
    }

    @GetMapping("selectByBusinessId")
    @ApiOperation("根据id查询商家入驻信息")
    public Result selectByBusinessId(Long id){
        return businessService.selectByBusinessId(id);
    }

    @PostMapping("examineByBusinessId")
    @ApiOperation("商家入驻审核")
    public Result examineByBusinessId(Business business){
        return businessService.examineByBusinessId(business);
    }


}

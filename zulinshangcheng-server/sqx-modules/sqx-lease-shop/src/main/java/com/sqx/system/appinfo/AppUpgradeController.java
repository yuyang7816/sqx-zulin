package com.sqx.system.appinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.App;
import com.sqx.domain.repository.service.app.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/appinfo")
@Api(value = "APP升级管理", tags = {"APP升级管理"})
public class AppUpgradeController {

    @Autowired
    private AppService iAppService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("管理平台升级详情")
    @ResponseBody
    public Result list(@ApiParam(value = "页码")Integer page,
                       @ApiParam(value = "条数")Integer limit) {
        IPage<App> pages =new Page<>(page,limit);
        return Result.success(iAppService.page(pages));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台升级详情")
    @ResponseBody
    public Result getBanner(@ApiParam(value = "app主键id")@PathVariable Long id) {
        return Result.success(iAppService.selectAppById(id));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation("管理平台添加升级信息")
    @ResponseBody
    public Result addBanner(@RequestBody App app) {
        if(app.getId()!=null){
            iAppService.updateAppById(app);
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            app.setCreateAt(sdf.format(new Date()));
            iAppService.insertApp(app);
        }
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除升级信息")
    public Result deleteBanner(@ApiParam(value = "app主键id")@PathVariable Long id) {
        iAppService.deleteAppById(id);
        return Result.success();
    }





}

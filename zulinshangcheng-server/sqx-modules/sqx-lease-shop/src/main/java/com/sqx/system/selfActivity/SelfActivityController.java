package com.sqx.system.selfActivity;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfActivity;
import com.sqx.domain.repository.mapper.selfActivity.SelfActivityJpaRepository;
import com.sqx.domain.repository.mapper.selfActivity.SelfActivityRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Api(value="自营商城首页菜单和活动管理",tags={"自营商城首页菜单和活动管理"})
@RequestMapping(value = "/selfActivity")
public class SelfActivityController {
    @Autowired
    private SelfActivityJpaRepository activityJpaRepository;

    @Autowired
    private SelfActivityRepository activityRepository;


    @GetMapping(value = "/id")
    @ApiOperation("管理平台详情")
    public Result getBanner(Integer id) {
        return ResultUtil.success(activityJpaRepository.findById(Long.valueOf(id)).orElse(null));
    }

    @GetMapping(value = "/state")
    @ApiOperation("根据状态查询菜单列表")
    @IgnoreSecurity
    public Result getBannerState(String state) {
        return ResultUtil.success(activityRepository.findOne(state));
    }

    @PostMapping(value = "/update")
    @ApiOperation("管理平台修改")
    public Result addBanner(@RequestBody SelfActivity info) {
        SelfActivity activity = new SelfActivity();
        activity.setId(Long.valueOf(info.getId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        activity.setImage_url(info.getImage_url());
        activity.setTitle(info.getTitle());
        activity.setCreateAt(sdf.format(now));
        activity.setState(info.getState());
        activity.setUrl(info.getUrl());
        activity.setContent(info.getContent());
        return ResultUtil.success(activityJpaRepository.save(activity));
    }

    @PostMapping(value = "/save")
    @ApiOperation("管理平台添加")
    public Result saveBanner(@RequestBody SelfActivity info) {
        info.setCreateAt(DateUtils.createTime());
        return ResultUtil.success(activityJpaRepository.save(info));
    }

    @GetMapping(value = "/delete")
    @ApiOperation("管理平台删除")
    public Result deleteBanner( Long id) {
        activityJpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @IgnoreSecurity
    @GetMapping(value = "/list")
    @ApiOperation("管理平台获取全部广告位")
    public Result getBannerList() {
        return ResultUtil.success(activityJpaRepository.findAll());
    }


}

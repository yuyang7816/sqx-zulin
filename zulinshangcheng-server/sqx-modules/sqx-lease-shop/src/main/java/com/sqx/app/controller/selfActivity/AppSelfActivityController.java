package com.sqx.app.controller.selfActivity;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.mapper.selfActivity.SelfActivityJpaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 18:07
 */
@RestController
@Api(value="商城首页菜单和活动管理---用户端",tags={"商城首页菜单和活动管理---用户端"})
@RequestMapping(value = "/app/selfActivity")
public class AppSelfActivityController {

    @Autowired
    private SelfActivityJpaRepository activityJpaRepository;

    @IgnoreSecurity
    @GetMapping(value = "/list")
    @ApiOperation("管理平台获取全部广告位")
    public Result getBannerList() {
        return ResultUtil.success(activityJpaRepository.findAll());
    }
}

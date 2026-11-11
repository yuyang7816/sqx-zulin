package com.sqx.app.controller;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.ScFollow;
import com.sqx.domain.repository.service.sys.ScFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/scFollow/")
@Api(value = "收藏|关注-用户端", tags = {"收藏|关注-用户端"})
public class AppScFollowController {
    @Autowired
    private ScFollowService scFollowService;


    @Login
    @ApiOperation("收藏-取消收藏|想要-取消想要")
    @PostMapping("updateFollow")
    public Result updateFollow(@RequestAttribute("userId") Long userId, ScFollow follow) {
        follow.setUserId(userId);
        return scFollowService.updateFollow(follow);
    }
    @Login
    @ApiOperation("获取我的收藏|想要列表")
    @PostMapping("getFollowList")
    public Result getFollowList(@RequestAttribute Long userId,Integer page,Integer limit,ScFollow follow){
        return Result.success(scFollowService.getFollowGoodsList(userId,page,limit,follow));
    }

    @Login
    @ApiOperation("查看我的足迹列表")
    @GetMapping("selectMyFootprintList")
    public Result selectMyFootprintList(@RequestAttribute Long userId,Integer pageNum,Integer pageSize){
        return scFollowService.selectMyFootprintList(userId,pageNum,pageSize);
    }

    @Login
    @ApiOperation("查看我关注的用户列表")
    @GetMapping("getFollowList")
    public Result getFollowList(@RequestAttribute Long userId,Integer pageNum,Integer pageSize){
        return scFollowService.getFollowList(userId,pageNum,pageSize);
    }

    @Login
    @ApiOperation("查看我的粉丝列表,is_mutual_follow为0是单向关注(即回关)，1是互相关注")
    @GetMapping("getFanList")
    public Result getFanList(@RequestAttribute Long userId,Integer pageNum,Integer pageSize){
        return scFollowService.getFanList(userId,pageNum,pageSize);
    }
}

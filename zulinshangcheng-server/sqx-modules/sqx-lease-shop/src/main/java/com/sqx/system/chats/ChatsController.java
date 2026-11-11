package com.sqx.system.chats;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.chats.Chats;
import com.sqx.domain.repository.service.chats.ChatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="聊天会话",tags={"聊天会话"})
@RequestMapping(value = "/chat")
public class ChatsController {
    @Autowired
    private ChatsService service;

    @GetMapping("/count")
    @ApiOperation("商家端未读消息")
    public Result count(@ApiParam("店铺id(总后台商户传0)") @RequestParam Long storeId) {
        return service.count(storeId);
    }

    @GetMapping("/userCount")
    @ApiOperation("用户端未读消息")
    public Result userCount(@ApiParam("用户id") @RequestParam Long userId) {
        return service.userCount(userId);
    }

    @GetMapping("/list")
    @ApiOperation("商家端会话列表")
    public Result findAll(@ApiParam("店铺id(总后台商户传0)") @RequestParam Long storeId,
                          @ApiParam("用户昵称") @RequestParam(required = false) String userName) {
        return service.findAll(storeId, userName);
    }

    @GetMapping("/userList")
    @ApiOperation("用户端会话列表")
    public Result userList(@ApiParam("用户id") Long userId) {
        return service.userList(userId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(@ApiParam("会话id") Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("用户端发起聊天")
    public Result saveBody(@RequestBody Chats entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody Chats entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除聊天会话")
    public Result delete(@ApiParam("会话id")Long id) {
        return service.delete(id);
    }

}

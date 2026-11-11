package com.sqx.app.controller.chats;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.chats.Chats;
import com.sqx.domain.repository.service.chats.ChatsContentService;
import com.sqx.domain.repository.service.chats.ChatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="聊天会话",tags={"聊天会话"})
@RequestMapping(value = "/app/chats")
public class AppChatsController {

    @Autowired
    private ChatsService service;
    @Autowired
    private ChatsContentService chatsContentService;

    @PostMapping("/save")
    @ApiOperation("用户端发起聊天")
    public Result saveBody(@RequestBody Chats entity) {
        return service.saveBody(entity);
    }

    @GetMapping("/list")
    @ApiOperation("用户端聊天内容列表")
    public Result findAll(@ApiParam("会话id") Long chatId) {
        return chatsContentService.findAll(chatId);
    }
    
    @GetMapping("/userCount")
    @ApiOperation("用户端未读消息")
    public Result userCount(@ApiParam("用户id") @RequestParam Long userId) {
        return service.userCount(userId);
    }


}

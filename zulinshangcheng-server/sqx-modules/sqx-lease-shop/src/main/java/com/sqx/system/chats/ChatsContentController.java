package com.sqx.system.chats;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.chats.ChatsContent;
import com.sqx.domain.repository.service.chats.ChatsContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="聊天会话内容",tags={"聊天会话内容"})
@RequestMapping(value = "/chatsContent")
public class ChatsContentController {
    @Autowired
    private ChatsContentService service;


    @GetMapping("/list")
    @ApiOperation("用户端聊天内容列表")
    public Result findAll(@ApiParam("会话id")Long chatId) {
        return service.findAll(chatId);
    }

    @GetMapping("/storeList")
    @ApiOperation("商户端聊天内容列表")
    public Result storeList(@ApiParam("会话id")Long chatId) {
        return service.storeList(chatId);
    }


    @ApiOperation("查询")
    public Result findOne(@ApiParam(value = "会话内容id") Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("发送消息")
    public Result saveBody(@RequestBody ChatsContent entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody ChatsContent entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除聊天会话内容")
    public Result delete(@ApiParam(value = "多个会话内容id") String ids) {
        return service.delete(ids);
    }

}

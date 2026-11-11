package com.sqx.system.chat;

import com.sqx.common.core.annotation.SysLog;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.chat.ChatContentService;
import com.sqx.domain.repository.service.chat.ChatConversationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "聊天", tags = {"聊天"})
@RequestMapping(value = "/chat")
public class ChatController {

    @Autowired
    private ChatContentService chatContentService;
    @Autowired
    private ChatConversationService chatConversationService;

    @GetMapping("/selectChatConversationPage")
    @ApiOperation("获取聊天会话")
    public Result selectChatConversationPage(@ApiParam(value = "页码")Integer page,
                                             @ApiParam(value = "条数")Integer limit,
                                             @ApiParam(value = "用户id")Long userId,
                                             @ApiParam(value = "昵称")String nickName,
                                             @ApiParam(value = "手机号")String phone,
                                             @ApiParam(value = "搜索聊天内容")String content,
                                             @ApiParam(value = "开始时间")String startTime,
                                             @ApiParam(value = "结束时间") String endTime){
        return Result.success(chatConversationService.selectChatConversationPage(page, limit, userId,nickName,
                content,null,null,phone,startTime,endTime));
    }


    @GetMapping("/selectChatContent")
    @ApiOperation("获取聊天记录")
    public Result selectChatContent(@ApiParam(value = "页码") Integer page,
                                    @ApiParam(value = "条数") Integer limit,
                                    @ApiParam(value = "聊天会话id")Long chatConversationId,
                                    @ApiParam(value = "搜索聊天内容")String content,
                                    @ApiParam(value = "用户昵称")String userName,
                                    @ApiParam(value = "手机号")String phone,
                                    @ApiParam(value = "开始时间")String startTime,
                                    @ApiParam(value = "结束时间") String endTime){
        return Result.success(chatContentService.selectChatContentPage(page, limit, null,chatConversationId,content,null,phone,startTime,endTime,userName));
    }


    @PostMapping("/deleteChatContentById")
    @ApiOperation("删除某一句聊天记录")
    @SysLog(value = "管理端删除聊天记录")
    public Result deleteChatContentById(@ApiParam(value = "聊天内容id")Long chatContentId){
        chatContentService.removeById(chatContentId);
        return Result.success();
    }





}

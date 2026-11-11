//package com.sqx.system.controller;
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.service.sys.ChatContentService;
//import com.sqx.domain.repository.service.sys.ChatConversationService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Api(value = "聊天", tags = {"聊天"})
//@RequestMapping(value = "/chat")
//public class ChatController {
//
//    @Autowired
//    private ChatContentService chatContentService;
//    @Autowired
//    private ChatConversationService chatConversationService;
//
//    @GetMapping("/selectChatConversationPage")
//    @ApiOperation("获取聊天会话")
//    public Result selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName){
//        return Result.success(chatConversationService.selectChatConversationPage(page, limit, userId,nickName));
//    }
//
//
//    @GetMapping("/selectChatContent")
//    @ApiOperation("获取聊天记录")
//    public Result selectChatContent(Integer page, Integer limit, Long chatConversationId, String content){
//        return Result.success(chatContentService.selectChatContentPage(page, limit, chatConversationId,content));
//    }
//
//
//    @PostMapping("/deleteChatContentById")
//    @ApiOperation("删除某一句聊天记录")
//    public Result deleteChatContentById(Long chatContentId){
//        chatContentService.removeById(chatContentId);
//        return Result.success();
//    }
//
//
//
//
//
//}

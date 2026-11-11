package com.sqx.system.chat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.annotation.SysLog;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.ChatConversation;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.chats.ChatRepository;
import com.sqx.domain.repository.service.chat.ChatContentService;
import com.sqx.domain.repository.service.chat.ChatConversationService;
import com.sqx.domain.repository.service.chats.ChatsService;
import com.sqx.domain.repository.service.sys.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "聊天", tags = {"聊天"})
@RequestMapping(value = "/app/chat")
public class AppChatController {

    @Autowired
    private ChatContentService chatContentService;
    @Autowired
    private ChatConversationService chatConversationService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatRepository jpaRepository;

    @Login
    @GetMapping("/selectChatConversationPage")
    @ApiOperation("获取聊天会话")
    public Result selectChatConversationPage(@ApiParam(name ="页码")Integer page,
                                             @ApiParam(name ="条数")Integer limit,
                                             @ApiParam(name ="用户id")@RequestAttribute("userId") Long userId,
                                             @ApiParam(value = "接收人id")Long focusedUserId,
                                             @ApiParam(name ="昵称")String nickName){
        return Result.success(chatConversationService.selectChatConversationPage(page, limit, userId,nickName,null,focusedUserId,1,null,null,null));
    }

    @Login
    @PostMapping("/insertChatConversation")
    @ApiOperation("发起聊天")
    public Result insertChatConversation(@RequestBody ChatConversation chatConversation){
        chatConversation.setGoodId(chatConversation.getGoodId());
        return chatConversationService.insertChatConversations(chatConversation);
    }

    @Login
    @GetMapping("/selectChatContent")
    @ApiOperation("获取聊天记录")
    public Result selectChatContent(@ApiParam(name ="页码")Integer page,
                                    @ApiParam(name ="条数")Integer limit,
                                    @ApiParam(name ="聊天会话id")Long chatConversationId,
                                    @ApiParam(name ="用户id")@RequestAttribute("userId") Long userId,
                                    @ApiParam(name ="搜索聊天内容")String content,
                                    @ApiParam(name ="商品id")String goodId){
        //清空未读消息
        chatContentService.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId,goodId);
        ChatConversation byId = chatConversationService.getById(chatConversationId);
        byId.setChatConversationId(chatConversationId);
        byId.setGoodId(goodId);
        chatConversationService.updateByGoodId(byId);
        if(byId!=null){
            boolean flag=false;
            if(String.valueOf(byId.getIsWxMsg()).equals(String.valueOf(userId))){
                byId.setIsWxMsg(0);
                byId.setGoodId(goodId);
                flag=true;
            }
            if(String.valueOf(byId.getIsSendMsg()).equals(String.valueOf(userId))){
                byId.setIsSendMsg(0);
                byId.setGoodId(goodId);
                flag=true;
            }
            if(flag){
                byId.setGoodId(goodId);
                chatConversationService.updateById(byId);
            }
        }
        return Result.success(chatContentService.selectChatContentPageByData(page, limit, userId,chatConversationId,content,1));
    }

    @Login
    @GetMapping("/selectChatCount")
    @ApiOperation("获取未读消息数量")
    public Result selectChatCount(@ApiParam(name ="用户id")@RequestAttribute("userId") Long userId){
//        int chatCount = chatContentService.selectChatCount(userId);
        Integer count = jpaRepository.userCounts(userId);
        Long messageCount = messageService.count(new QueryWrapper<MessageInfo>()
                .eq("is_see", 0)
                .eq("user_id", userId));
        Map<String,Object> result=new HashMap<>();
        result.put("chatCount",count == null ? 0 : count);
        result.put("messageCount",messageCount);
        return Result.success(result);
    }

    @Login
    @PostMapping("/removeChatContentAll")
    @SysLog(value = "删除所有聊天会话")
    @ApiOperation("删除所有聊天会话")
    public Result removeChatContentAll(Long chatConversationId){
        ChatConversation chatConversation = chatConversationService.getById(chatConversationId);
        chatConversation.setIsDelete(1);
        chatConversationService.updateById(chatConversation);
        chatContentService.updateChatContentStatusByUserIdAndChatIds(chatConversationId);
        return Result.success();
    }
}
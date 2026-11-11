//package com.sqx.domain.repository.service.sys;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.sqx.common.core.domain.Result;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.domain.repository.entity.app.ChatConversation;
//
//
//public interface ChatConversationService extends IService<ChatConversation> {
//
//    PageUtils selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName);
//
//    int insertChatConversation(ChatConversation chatConversation);
//
//    ChatConversation selectChatConversation(Long userId, Long focusedUserId);
//
//    Result insertChatConversations(ChatConversation chatConversation);
//
//}
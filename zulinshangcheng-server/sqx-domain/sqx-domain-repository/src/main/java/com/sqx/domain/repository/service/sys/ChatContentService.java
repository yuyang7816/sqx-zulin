//package com.sqx.domain.repository.service.sys;
//
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.domain.repository.entity.app.ChatContent;
//
//public interface ChatContentService  extends IService<ChatContent> {
//
//    PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId, String content);
//
//    int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId);
//
//    int selectChatCount(Long userId);
//
//}
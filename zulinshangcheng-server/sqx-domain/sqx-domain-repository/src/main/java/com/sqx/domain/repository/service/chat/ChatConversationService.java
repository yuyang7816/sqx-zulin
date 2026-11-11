package com.sqx.domain.repository.service.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.ChatConversation;

import java.util.Map;


public interface ChatConversationService extends IService<ChatConversation> {

    IPage<Map<String,Object>> selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName,
                                                         String content,Long focusedUserId,Integer isDelete,String phone,
                                                         String startTime,String endTime);

    int insertChatConversation(ChatConversation chatConversation);

    ChatConversation selectChatConversation(Long userId,Long focusedUserId);

    Result insertChatConversations(ChatConversation chatConversation);

    void updateByGoodId(ChatConversation byId);
}
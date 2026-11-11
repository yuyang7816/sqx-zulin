package com.sqx.domain.repository.service.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.domain.repository.entity.app.ChatContent;


import java.util.List;
import java.util.Map;

public interface ChatContentService  extends IService<ChatContent> {

    IPage<Map<String,Object>> selectChatContentPage(Integer page, Integer limit,Long userId, Long chatConversationId,
                                                    String content,Integer isDelete,String phone,String startTime,String endTime,String userName);

    int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId,String goodId);

    int selectChatCount(Long userId);

    List<ChatContent> selectChatByChatId(Long chatConversationId);

    int updateChatContentStatusByUserIdAndChatIds(Long chatConversationId);

    IPage<Map<String,Object>> selectChatContentPageByData(Integer page, Integer limit, Long userId, Long chatConversationId, String content,Integer isDelete);
}
package com.sqx.domain.repository.service.chats;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.chats.ChatsContent;

public interface ChatsContentService {

    /**
     * 用户聊天内容列表
     * @param chatId
     * @return
     */
    Result findAll(Long chatId);

    /**
     * 店铺聊天内容列表
     * @param chatId
     * @return
     */
    Result storeList(Long chatId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(String ids);

    /**
     * 发送消息
     * @param entity
     * @return
     */
    Result saveBody(ChatsContent entity);

    //修改
    Result updateBody(ChatsContent entity);

}

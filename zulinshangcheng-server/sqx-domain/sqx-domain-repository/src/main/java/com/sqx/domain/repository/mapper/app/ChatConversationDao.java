package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.ChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


@Mapper
public interface ChatConversationDao extends BaseMapper<ChatConversation> {

    IPage<Map<String,Object>> selectChatConversationPage(Page<Map<String,Object>> page,
                                                         @Param("userId")Long userId,
                                                         @Param("nickName")String nickName,
                                                         @Param("content")String content,
                                                         @Param("focusedUserId")Long focusedUserId,
                                                         @Param("isDelete")Integer isDelete,
                                                         @Param("phone")String phone,
                                                         @Param("startTime")String startTime,
                                                         @Param("endTime")String endTime);

    int insertChatConversation(ChatConversation chatConversation);

    void updateByGoodId(ChatConversation byId);
}
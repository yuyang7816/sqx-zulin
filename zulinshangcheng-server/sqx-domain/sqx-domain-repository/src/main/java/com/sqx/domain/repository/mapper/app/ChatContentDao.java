package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.ChatContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ChatContentDao extends BaseMapper<ChatContent> {

    IPage<Map<String,Object>> selectChatContentPage(@Param("pages") Page<Map<String,Object>> pages,
                                                    @Param("userId")Long userId,
                                                    @Param("chatConversationId") Long chatConversationId,
                                                    @Param("content") String content,
                                                    @Param("isDelete") Integer isDelete,
                                                    @Param("phone")String phone,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("userName") String userName);

    int updateChatContentStatusByUserIdAndChatId(@Param("userId") Long userId,@Param("chatConversationId") Long chatConversationId,@Param("goodId")String goodId);

    int selectChatCount(@Param("userId") Long userId);

    List<Map<String,Object>> selectChatContentCountByWx(@Param("minute") Integer minute,@Param("counts") Integer counts);

    List<Map<String,Object>> selectChatContentCountByDx(@Param("minute") Integer minute,@Param("counts") Integer counts);

    List<ChatContent> selectChatByChatId(Long chatConversationId);

    int updateChatContentStatusByUserIdAndChatIds(@Param("chatConversationId") Long chatConversationId);

    IPage<Map<String, Object>> selectChatContentPageByData(@Param("pages") Page<Map<String,Object>> pages,
                                                           @Param("userId")Long userId,
                                                           @Param("chatConversationId") Long chatConversationId,
                                                           @Param("content") String content,
                                                           @Param("isDelete") Integer isDelete);
}
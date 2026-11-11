package com.sqx.domain.repository.service.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.repository.entity.app.ChatContent;
import com.sqx.domain.repository.mapper.app.ChatContentDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ChatContentServiceImpl extends ServiceImpl<ChatContentDao, ChatContent> implements ChatContentService {


    @Override
    public IPage<Map<String, Object>> selectChatContentPage(Integer page, Integer limit, Long userId, Long chatConversationId,
                                                            String content, Integer isDelete, String phone,
                                                            String startTime, String endTime,String userName) {
        Page<Map<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return baseMapper.selectChatContentPage(pages, userId, chatConversationId, content, isDelete, phone, startTime, endTime,userName);
    }

    @Override
    public int updateChatContentStatusByUserIdAndChatId(Long userId, Long chatConversationId, String goodId) {
        return baseMapper.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId, goodId);
    }

    @Override
    public int selectChatCount(Long userId) {
        return baseMapper.selectChatCount(userId);
    }

    @Override
    public List<ChatContent> selectChatByChatId(Long chatConversationId) {
        List<ChatContent> chatContents = baseMapper.selectChatByChatId(chatConversationId);
        return chatContents;
    }

    @Override
    public int updateChatContentStatusByUserIdAndChatIds(Long chatConversationId) {
        return baseMapper.updateChatContentStatusByUserIdAndChatIds(chatConversationId);
    }

    @Override
    public IPage<Map<String, Object>> selectChatContentPageByData(Integer page, Integer limit, Long userId, Long chatConversationId, String content, Integer isDelete) {
        Page<Map<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectChatContentPageByData(pages, userId, chatConversationId, content, isDelete);
    }

}
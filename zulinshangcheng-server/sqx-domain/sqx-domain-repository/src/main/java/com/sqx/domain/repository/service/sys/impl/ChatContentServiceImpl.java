//package com.sqx.domain.repository.service.sys.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.domain.repository.entity.app.ChatContent;
//import com.sqx.domain.repository.mapper.app.ChatContentDao;
//import com.sqx.domain.repository.service.sys.ChatContentService;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class ChatContentServiceImpl extends ServiceImpl<ChatContentDao, ChatContent> implements ChatContentService {
//
//
//    @Override
//    public PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId, String content) {
//        return new PageUtils(baseMapper.selectChatContentPage(new Page<>(page,limit),chatConversationId,content));
//    }
//
//    @Override
//    public int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId){
//        return baseMapper.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId);
//    }
//
//    @Override
//    public int selectChatCount(Long userId) {
//        return baseMapper.selectChatCount(userId);
//    }
//
//}
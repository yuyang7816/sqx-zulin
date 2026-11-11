//package com.sqx.domain.repository.service.sys.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sqx.common.core.domain.Result;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.domain.repository.entity.app.ChatContent;
//import com.sqx.domain.repository.entity.app.ChatConversation;
//import com.sqx.domain.repository.entity.app.UserEntity;
//import com.sqx.domain.repository.mapper.app.ChatConversationDao;
//import com.sqx.domain.repository.service.sys.ChatContentService;
//import com.sqx.domain.repository.service.sys.ChatConversationService;
//import com.sqx.domain.repository.service.sys.UserService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
//
//@Service
//public class ChatConversationServiceImpl extends ServiceImpl<ChatConversationDao, ChatConversation> implements ChatConversationService {
//
//    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock();
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ChatContentService chatContentService;
//
//    @Override
//    public PageUtils selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName){
//        return new PageUtils(baseMapper.selectChatConversationPage(new Page<>(page,limit),userId,nickName));
//    }
//
//
//    @Override
//    public int insertChatConversation(ChatConversation chatConversation) {
//        return baseMapper.insertChatConversation(chatConversation);
//    }
//
//    @Override
//    public ChatConversation selectChatConversation(Long userId, Long focusedUserId){
//        ChatConversation chatConversation = baseMapper.selectOne(new QueryWrapper<ChatConversation>().eq("user_id", userId).eq("focused_user_id", focusedUserId));
//        if(chatConversation!=null){
//            return chatConversation;
//        }
//        return baseMapper.selectOne(new QueryWrapper<ChatConversation>().eq("focused_user_id", userId).eq("user_id", focusedUserId));
//    }
//
//
//    @Override
//    public Result insertChatConversations(ChatConversation chatConversation){
//        reentrantReadWriteLock.writeLock().lock();
//        try{
//            ChatConversation chatConversation1 = selectChatConversation(chatConversation.getUserId(), chatConversation.getFocusedUserId());
//            if(chatConversation1==null){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String format = sdf.format(new Date());
//                chatConversation.setCreateTime(format);
//                chatConversation.setUpdateTime(format);
//                baseMapper.insertChatConversation(chatConversation);
//                UserEntity byId = userService.getById(chatConversation.getFocusedUserId());
//                if(byId!=null && StringUtils.isNotEmpty(byId.getMsg())){
//                    ChatContent chatContent=new ChatContent();
//                    chatContent.setStatus(1);
//                    chatContent.setContent(byId.getMsg());
//                    chatContent.setCreateTime(format);
//                    chatContent.setMessageType("1");
//                    chatContent.setUserId(byId.getUserId());
//                    chatContent.setChatConversationId(chatConversation.getChatConversationId());
//                    chatContentService.save(chatContent);
//                }
//                return Result.success(chatConversation);
//            }
//            return Result.success(chatConversation1);
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("发起聊天出错"+e.getMessage(),e);
//        }finally {
//            reentrantReadWriteLock.writeLock().unlock();
//        }
//        return Result.failed("40011");
//    }
//
//
//
//}
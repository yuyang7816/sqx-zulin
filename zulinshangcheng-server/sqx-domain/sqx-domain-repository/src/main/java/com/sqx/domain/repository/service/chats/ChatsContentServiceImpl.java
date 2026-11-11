package com.sqx.domain.repository.service.chats;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.chats.Chats;
import com.sqx.domain.repository.entity.chats.ChatsContent;
import com.sqx.domain.repository.mapper.app.ChatsContentDao;
import com.sqx.domain.repository.mapper.chats.ChatContentRepository;
import com.sqx.domain.repository.mapper.chats.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatsContentServiceImpl implements ChatsContentService {
    @Autowired
    private ChatContentRepository jpaRepository;
    @Resource
    private ChatRepository chatRepository;
    @Autowired
    private ChatsContentDao charsContentDao;

    /**
     * 用户聊天内容列表
     * @param chatId 会话id
     * @return
     */
    @Override
    public Result findAll(Long chatId) {
        //构造自定义查询条件
//        Specification<ChatsContent> queryCondition = new Specification<ChatsContent>() {
//            @Override
//            public Predicate toPredicate(Root<ChatsContent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicateList = new ArrayList<>();
//                predicateList.add(criteriaBuilder.equal(root.get("chatId"), chatId));
//                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
//            }
//        };
        List<ChatsContent> list = charsContentDao.selectList(new QueryWrapper<ChatsContent>().eq("chat_id",chatId));
        /*
        用户调取消息内容列表
        1.用户的未读数为0
        2.店铺发送的消息为已读
         */
        chatRepository.userCount(chatId); //用户未读为0
        jpaRepository.updateStatusByChantIdAndSendTye2(chatId); //用户调取列表，设置店铺发送的消息为已读
        //聊天会话信息
        List<Chats> allChat = chatRepository.findAll();
        for (ChatsContent cc : list) {
            for (Chats c : allChat) {
                if (c.getChatId().equals( cc.getChatId())){
                    cc.setChat(c);
                }
            }
        }
        return ResultUtil.success(list);
    }

    /**
     * 店铺聊天内容列表
     * @param chatId
     * @return
     */
    @Override
    public Result storeList(Long chatId) {
        //构造自定义查询条件
        Specification<ChatsContent> queryCondition = new Specification<ChatsContent>() {
            @Override
            public Predicate toPredicate(Root<ChatsContent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("chatId"), chatId));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<ChatsContent> list = jpaRepository.findAll(queryCondition);
        /*
        店铺调取消息内容列表
        1.店铺的未读数为0
        2.用户发送的消息为已读
         */
        chatRepository.storeCount(chatId); //用户未读为0
        jpaRepository.updateStatusByChantIdAndSendTye1(chatId); //店铺调取列表，设置用户发送的消息为已读
        //聊天会话信息
        List<Chats> allChat = chatRepository.findAll();
        for (ChatsContent cc : list) {
            for (Chats c : allChat) {
                if (c.getChatId().equals(cc.getChatId())){
                    cc.setChat(c);
                }
            }
        }
        return ResultUtil.success(list);
    }

    /**
     * 发送消息
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(ChatsContent entity) {
        //发送消息保存消息
        entity.setCreateTime(DateUtils.createTime());
        entity.setStatus(1); //未读
        ChatsContent save = jpaRepository.save(entity);
        //会话列表未读加1
        if (save.getSendType() == 1){
            chatRepository.addStoreCount(save.getChatId(), DateUtils.createTime()); //用户发送消息，店铺未读+1
        }else {
            chatRepository.addUserCount(save.getChatId(), DateUtils.createTime()); //店铺发送消息，用户未读+1
        }
        return ResultUtil.success(save);
    }

    @Override
    public Result updateBody(ChatsContent entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            jpaRepository.deleteById(Long.valueOf(id));
        }
        return ResultUtil.success();
    }

}

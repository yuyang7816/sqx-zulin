package com.sqx.domain.repository.service.chats;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.chats.Chats;
import com.sqx.domain.repository.entity.chats.ChatsContent;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.chats.ChatContentRepository;
import com.sqx.domain.repository.mapper.chats.ChatRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatsServiceImpl implements ChatsService {
    @Autowired
    private ChatRepository jpaRepository;
    @Autowired
    private ChatContentRepository chatContentRepository;
    @Autowired
    private UserDao userDao;

    @Override
    public Result count(Long storeId) {
        Integer count = jpaRepository.count(storeId);
        return ResultUtil.success(count==null?0:count);
    }

    @Override
    public Result userCount(Long userId) {
        Integer count = jpaRepository.userCounts(userId);
        return ResultUtil.success(count==null?0:count);
    }

    /**
     * 商家端会话列表
     * @param storeId
     * @return
     */
    @Override
    public Result findAll(Long storeId, String userName) {
        //构造自定义查询条件
        Specification<Chats> queryCondition = new Specification<Chats>() {
            @Override
            public Predicate toPredicate(Root<Chats> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("storeId"), storeId));
                if (StringUtils.isNotEmpty(userName)){
                    predicateList.add(criteriaBuilder.like(root.get("userName"), "%"+userName+"%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<Chats> list = jpaRepository.findAll(queryCondition, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        //最新一条消息展示
        List<ChatsContent> allContent = chatContentRepository.findAll();
        for (Chats c : list) {
            for (ChatsContent cc : allContent) {
                if (c.getChatId().equals(cc.getChatId())){
                    c.setContent(cc.getContent());
                    c.setContentTime(cc.getCreateTime());
                    c.setStoreId(Long.valueOf(0));
                    UserEntity user = userDao.selectById(c.getUserId());
                    if (user != null){
                    c.setUserName(user.getUserName());
                    }
                }
            }
        }
        return ResultUtil.success(list);
    }

    /**
     * 用户端会话列表
     * @param userId
     * @return
     */
    @Override
    public Result userList(Long userId) {
        //构造自定义查询条件
        Specification<Chats> queryCondition = new Specification<Chats>() {
            @Override
            public Predicate toPredicate(Root<Chats> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<Chats> list = jpaRepository.findAll(queryCondition, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        return ResultUtil.success(list);
    }

    /**
     * 用户端发起聊天
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(Chats entity) {
        //判断是否存在聊天
//        List<Chats> chatList = jpaRepository.findByUserIdAndStoreId(entity.getUserId(), entity.getStoreId());
        List<Chats> chatList = jpaRepository.findByUserId(entity.getUserId());
        if (chatList.size() > 0){
            Chats chat = chatList.get(0);
            return ResultUtil.success(chat);
        }else {
            //不存在会话，创建会话
            entity.setCreateTime(DateUtils.createTime());
            Chats save = jpaRepository.save(entity);
            return ResultUtil.success(save);
        }
    }

    @Override
    public Result updateBody(Chats entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }


    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id); //删除会话
        chatContentRepository.deleteAllByChatId(id); //删除会话聊天内容
        return ResultUtil.success();
    }

}

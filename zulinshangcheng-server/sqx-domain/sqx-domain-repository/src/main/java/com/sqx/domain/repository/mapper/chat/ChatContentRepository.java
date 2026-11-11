//package com.sqx.domain.repository.mapper.chat;
//
//import com.sqx.domain.repository.entity.chat.ChatContent;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public interface ChatContentRepository extends JpaRepository<ChatContent, Long> {
//
//    //条件查询带分页
//    Page<ChatContent> findAll(Specification<ChatContent> specification, Pageable pageable);
//
//    //条件查询
//    List<ChatContent> findAll(Specification<ChatContent> specification);
//
//    //根据会话id删除聊天内容
//    void deleteAllByChatId(Long chatId);
//
//    //用户消息设置为已读
//    @Modifying
//    @Transactional
//    @Query(value = "update ChatContent s set s.status=2 where s.chatId=:chatId and s.sendType=1")
//    Integer updateStatusByChantIdAndSendTye1(@Param("chatId") Long chatId);
//
//    //店铺消息设置为已读
//    @Modifying
//    @Transactional
//    @Query(value = "update ChatContent s set s.status=2 where s.chatId=:chatId and s.sendType=2")
//    Integer updateStatusByChantIdAndSendTye2(@Param("chatId") Long chatId);
//
//}

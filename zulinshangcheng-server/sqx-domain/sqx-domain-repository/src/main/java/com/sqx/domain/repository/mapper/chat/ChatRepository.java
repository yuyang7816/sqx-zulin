//package com.sqx.domain.repository.mapper.chat;
//
//import com.sqx.domain.repository.entity.chat.Chat;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public interface ChatRepository extends JpaRepository<Chat, Long> {
//
//    //条件查询带分页
//    Page<Chat> findAll(Specification<Chat> specification, Pageable pageable);
//
//    //条件查询
//    List<Chat> findAll(Specification<Chat> specification, Sort sort);
//
//    //商家端未读消息统计
//    @Query(value = "select sum(s.storeCount) from Chat s where s.storeId=:storeId")
//    Integer count(@Param("storeId") Long storeId);
//
//    //根据用户id和店铺id查询会话
//    @Query(value = "from Chat s where s.userId=:userId and s.storeId=:storeId")
//    List<Chat> findByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);
//
//    //商家端会话列表
//    @Query(value = "select c.chat_id as chatId,c.store_count as storeCount,c.user_head as userHead,c.user_name as userName, cc.content, cc.create_time as contentContent from chat c  left join chat_content cc on c.chat_id = cc.chat_id  where c.store_id=:storeId order by cc.create_time desc",
//            nativeQuery=true)
//    List<Map<String,Object>> findAllByStoreId(@Param("storeId") Long storeId);
//
//    //商家端会话列表
//    @Query(value = "select c.chat_id as chatId,c.store_count as storeCount,c.user_head as userHead,c.user_name as userName, cc.content, cc.create_time as contentContent from chat c  left join chat_content cc on c.chat_id = cc.chat_id  where c.store_id=:storeId and c.user_name like concat('%',:userName,'%')  order by cc.create_time desc",
//            nativeQuery=true)
//    List<Map<String,Object>> findAllByStoreIdAndUserName(@Param("storeId") Long storeId, @Param("userName") String userName);
//
//    //用户端会话列表
//    @Query(value = "select c.chat_id as chatId,c.store_head as storeHead,c.store_name as storeName,c.user_count as userCount, cc.content, cc.create_time as contentContent from chat c  left join chat_content cc on c.chat_id = cc.chat_id  where c.user_id=:userId order by cc.create_time desc limit 0,1",
//            nativeQuery=true)
//    List<Map<String,Object>> findAllByUserId(@Param("userId") Long userId);
//
//    //店铺未读+1
//    @Modifying
//    @Transactional
//    @Query(value = "update Chat s set s.storeCount=s.storeCount+1, s.createTime=:createTime where s.chatId=:chatId")
//    Integer addStoreCount(@Param("chatId") Long chatId, @Param("createTime") String createTime);
//
//    //店铺未读清空
//    @Modifying
//    @Transactional
//    @Query(value = "update Chat s set s.storeCount=0 where s.chatId=:chatId")
//    Integer storeCount(@Param("chatId") Long chatId);
//
//    //用户未读+1
//    @Modifying
//    @Transactional
//    @Query(value = "update Chat s set s.userCount=s.userCount+1, s.createTime=:createTime where s.chatId=:chatId")
//    Integer addUserCount(@Param("chatId") Long chatId, @Param("createTime") String createTime);
//
//    //用户未读清空
//    @Modifying
//    @Transactional
//    @Query(value = "update Chat s set s.userCount=0 where s.chatId=:chatId")
//    Integer userCount(@Param("chatId") Long chatId);
//}

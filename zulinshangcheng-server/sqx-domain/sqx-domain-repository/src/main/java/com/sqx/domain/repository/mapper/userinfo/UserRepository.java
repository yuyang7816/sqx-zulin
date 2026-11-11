package com.sqx.domain.repository.mapper.userinfo;

import com.sqx.domain.repository.entity.app.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends Repository<UserEntity, Long> {

    @Query(value = "from UserEntity s where s.phone=:phone order by userId desc")
    Page<UserEntity> find(@Param("phone") String phone, Pageable pageable);

    @Query(value = "select s.createTime,s.avatar,s.userName,s.invitationCode,s.balance from UserEntity s where s.inviterCode=:inviterCode order by userId desc")
    Page<UserEntity> getUsersByInvitation(@Param("inviterCode") String inviterCode, Pageable pageable);

    //直属用户id列表
    @Query(value = "select s.userId from UserEntity s where s.inviterCode=:invitationCode order by userId desc")
    List<Long> getUsersByRelationId(@Param("invitationCode") String invitationCode);

    //非直属用户id列表
    @Query(value = "select s.user_id from tb_user s where inviter_code in (select invitation_code from tb_user u where u.inviter_code=:invitationCode)",nativeQuery = true)
    List<Long> getUsersByRelationIds(@Param("invitationCode") String invitationCode);

    @Query(value = "from UserEntity s where s.inviterCode=:inviterCode and s.balance=0")
    List<UserEntity> getInvitation(@Param("inviterCode") String inviterCode);

    @Query(value = "from UserEntity s where s.inviterCode=:inviterCode")
    Page<UserEntity> getInvitations(@Param("inviterCode") String inviterCode,Pageable pageable);

    @Query(value = "select * from tb_user where inviter_code in (select invitation_code from tb_user where inviter_code=:inviterCode and length(relation_id)>0 and invitation_code is not null)",nativeQuery = true)
    Page<UserEntity> getInvitationss(@Param("inviterCode") String inviterCode,Pageable pageable);

    @Query(value = "select count(t.userId) from UserEntity t where t.invitationCode=:invitationCode")
    String getCount(@Param("invitationCode") String invitationCode);


    @Query(value = "select count(t.userId) from UserEntity t where t.inviterCode=:inviterCode and t.balance=0")
    String getCounts(@Param("inviterCode") String inviterCode);

    @Query(value = "select count(t.userId) from UserEntity t")
    String getCountNumber();

    @Query(value = "select count(t.userId) from UserEntity t where t.isInvitation=:isInvitation")
    String getCountNumberMember(@Param("isInvitation") int isInvitation);

    @Query(value = "select count(s.userId) from UserEntity s where s.isInvitation=:isInvitation and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTime(@Param("time") String time, @Param("isInvitation") int isInvitation);

    @Query(value = "select count(s.userId) from UserEntity s where s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTime(@Param("time") String time);

    @Query(value = "select count(s.userId) from UserEntity s where s.invitationCode is not null and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeByRelationId(@Param("time") String time);

    @Query(value = "select count(s.userId) from UserEntity s where s.invitationCode is null and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeRAndRelationIdIsNull(@Param("time") String time);

    @Query(value = "select count(s.userId) from UserEntity s where s.phone is not null and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeByPhone(@Param("time") String time);

    @Query(value = "select count(s.userId) from UserEntity s where s.openId is not null and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeByWx(@Param("time") String time);

    @Query(value = "select count(s.userId) from UserEntity s where s.openId is not null and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeByMp(@Param("time") String time);


    @Query(value = "select count(s.userId) from UserEntity s where s.platform=:platform and s.createTime like concat('%',:time,'%') ")
    String getCountNumberByTimeAndPlatform(@Param("time") String time, @Param("platform") String platform);

    @Query(value = "select count(s.userId) from UserEntity s where s.platform=:platform and s.createTime like concat('%',:createAt,'%')")
    String getCountUser(@Param("platform") String platform, @Param("createAt") String createAt);

    @Query(value = "from UserEntity s where s.phone=:userName")
    UserEntity login(@Param("userName") String userName);

    @Query(value = "from UserEntity s where s.userId=:userId")
    UserEntity getUserById(@Param("userId") Long userId);

    @Query(value = "from UserEntity s where s.phone=:userName")
    UserEntity getUserByPhone(@Param("userName") String userName);

//    @Query(value = "from UserEntity s where s.pdd=:pdd")
//    UserEntity getUserByPdd(@Param("pdd") String pdd);
//
//    @Query(value = "from UserEntity s where s.jd=:jd")
//    UserEntity getUserByJd(@Param("jd") String jd);

    @Query(value = "from UserEntity s where s.openId=:openId")
    UserEntity getUserByWxId(@Param("openId") String openId);

    @Query(value = "from UserEntity s where s.wxOpenId=:wxOpenId")
    UserEntity getUserByUnionid(@Param("wxOpenId") String wxOpenId);

    @Query(value = "from UserEntity s where s.accountOpenId=:accountOpenId")
    UserEntity getUserByOpen_uid(@Param("accountOpenId") String accountOpenId);

//    @Query(value = "from UserEntity s where s.tb_openid=:tb_openid")
//    UserEntity getUserByTbOpenid(@Param("tb_openid") String tb_openid);
//
//    @Query(value = "from UserEntity s where s.mpId=:mpId")
//    UserEntity getUserByMpId(@Param("mpId") String mpId);


    @Query(value = "from UserEntity s where s.invitationCode=:invitationCode")
    UserEntity getUserByRelationId(@Param("invitationCode") String invitationCode);

    @Query(value = "from UserEntity s where s.invitationCode=:invitationCode")
    UserEntity getUserByInvitationCode(@Param("invitationCode") String invitationCode);

    @Query(value = "from UserEntity s where s.invitationCode<>null")
    List<UserEntity> findByRelationIdNotNull();

    @Query(value = "select s.inviterCode from UserEntity s group by s.inviterCode")
    List<String> findRelationList();

    @Modifying
    @Query(value = "update UserEntity s set s.status=:status where s.userId=:userId")
    Integer updateState(@Param("status") String status, @Param("userId") long userId);

    @Modifying
    @Query(value = "update UserEntity s set s.password=:password where s.userId=:userId")
    Integer updatePwd(@Param("password") String password, @Param("userId") String userId);

    @Query(value = "select s.inviterCode from UserEntity s where s.createTime like concat('%',:time,'%') group by s.inviterCode")
    List<Long> findInvitationList(@Param("time") String time);

    @Query(value = "select count(t.userId) from UserEntity t where t.inviterCode=:inviterCode and t.createTime like concat('%',:time,'%')")
    Long getCountByTime(@Param("inviterCode") String inviterCode,@Param("time") String time);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.balance=s.balance+:balance where s.userId=:userId")
    Integer updateJiFenAdd(@Param("balance") String balance,@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.balance=s.balance-:balance where s.userId=:userId")
    Integer updateJiFenDelete(@Param("balance") String balance,@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.orderJifen=:orderJifen where s.userId=:userId")
    Integer updateOrderJifen(@Param("orderJifen") String orderJifen,@Param("userId") Long userId);

    @Query(value = "from UserEntity")
    List<UserEntity> selectUserList();

    @Query(value = "select count(*) from tb_user where create_time like concat('%',:time,'%') and platform=:platform",nativeQuery = true)
    int getUserPlatformByTime(@Param("time") String time,@Param("platform") String platform);

    //积分抽奖：加分
    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.orderJifen=s.orderJifen+:orderJifen where s.userId=:userId")
    Integer addOrderJifen(@Param("orderJifen") String orderJifen,@Param("userId") Long userId);
    //积分抽奖：减分
    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.orderJifen=s.orderJifen-:orderJifen where s.userId=:userId")
    Integer lessOrderJifen(@Param("orderJifen") String orderJifen,@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.userName=:userName where s.userId=:userId")
    Integer updateNickName(@Param("userName") String userName,@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity s set s.avatar=:avatar where s.userId=:userId")
    Integer updateImgUrl(@Param("avatar") String avatar,@Param("userId") Long userId);


}

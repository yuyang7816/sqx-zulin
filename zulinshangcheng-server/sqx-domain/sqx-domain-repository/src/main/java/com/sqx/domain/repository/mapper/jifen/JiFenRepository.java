package com.sqx.domain.repository.mapper.jifen;

import com.sqx.domain.repository.entity.sys.JiFen;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JiFenRepository extends Repository<JiFen, Long> {

    @Query(value = "select s.userId from JiFen s where s.createAt like concat('%',:time,'%') group by s.userId")
    List<String> findJiFenList(@Param("time") String time);

    @Query(value = "select count(s.id) from JiFen s where s.userId=:userId  and  s.createAt like concat('%',:time,'%')")
    Long countJiFen(@Param("time") String time, @Param("userId") String userId);


    @Query(value = "select count(s.id) from JiFen s where s.userId=:userId and s.content='每日登录' and  s.createAt like concat('%',:time,'%')")
    Long countJiFenLogin(@Param("time") String time, @Param("userId") String userId);

    @Query(value = "select s.id from JiFen s where s.userId=:userId and s.content='每日登录' and  s.createAt like concat('%',:time,'%')")
    Long countJiFenByTime(@Param("time") String time, @Param("userId") String userId);

    @Query(value = "select sum(s.number) from JiFen s where s.userId=:userId  and  s.createAt like concat('%',:time,'%')")
    //@Query(value = "select sum(s.number) from JiFen s where s.userId=:userId  and  s.createAt like concat('%',:time,'%') and s.des like '%+%'") //只统计增加的积分
    String sumJiFen(@Param("time") String time, @Param("userId") String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from ji_fen where user_id=:userId",nativeQuery = true)
    int deleteJiFenByUserId(@Param("userId") Long userId);

}
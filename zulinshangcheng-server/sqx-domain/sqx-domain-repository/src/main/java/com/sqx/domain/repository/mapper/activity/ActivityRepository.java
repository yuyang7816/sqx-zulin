package com.sqx.domain.repository.mapper.activity;

import com.sqx.domain.repository.entity.sys.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends Repository<Activity, Long> {
    @Query(value = "from Activity s where s.state=:state  " +
            "and (s.type=:type or s.type like concat('%,',:type,'%') or s.type like concat('%,',:type,',%') or s.type like concat('%',:type,',%') ) " +
            "order by s.sort,id desc")
    List<Activity> findOne(@Param("state") String state,@Param("type") Integer type);

    @Query(value = "from Activity s where s.state=:state  order by s.sort,id desc ")
    List<Activity> findOne(@Param("state") String state);

    @Query(value = "from Activity s where s.state=:state " +
            "and (s.type=:type or s.type like concat('%,',:type,'%') or s.type like concat('%,',:type,',%') or s.type like concat('%',:type,',%') ) " +
            "order by s.sort,id desc ")
    Page<Activity> findPage(Pageable pageable, @Param("state") String state,@Param("type") Integer type);

    @Query(value = "from Activity s where s.state=:state order by s.sort,id desc ")
    Page<Activity> findPage(Pageable pageable, @Param("state") String state);

}
package com.sqx.domain.repository.mapper.message;

import com.sqx.domain.repository.entity.sys.MessageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends Repository<MessageInfo, Long> {
    @Query(value = "from MessageInfo s where s.state=:state order by id desc")
    Page<MessageInfo> find(@Param("state") String state, Pageable pageable);

    @Query(value = "from MessageInfo s where s.type=:type order by id desc")
    Page<MessageInfo> findType(@Param("type") Integer type, Pageable pageable);

    @Query(value = "from MessageInfo s where s.state=:type and s.userId=:userId order by id desc")
    Page<MessageInfo> findTypeByUserId(@Param("type") String type, @Param("userId") String userId, Pageable pageable);

    @Modifying
    @Query(value = "update MessageInfo s set s.state=:state where s.id=:id")
    Integer updateState(@Param("state") String state, @Param("id") long id);

    @Modifying
    @Query(value = "update MessageInfo s set s.sendState=:sendState where s.id=:id")
    Integer updateSendState(@Param("sendState") String sendState, @Param("id") long id);

}
package com.sqx.domain.repository.mapper.token;

import com.sqx.domain.repository.entity.sys.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenRepository extends Repository<Token, Long> {
    @Query(value = "from Token s where s.uuid=:uuid")
    Token findOne(@Param("uuid") String uuid);
    @Query(value = "from Token s where s.userId=:userId")
    Token findByUserId(@Param("userId") String userId);
    @Query(value = "from Token s where s.adminId=:adminId")
    List<Token> findByAdminId(@Param("adminId") String adminId);
}
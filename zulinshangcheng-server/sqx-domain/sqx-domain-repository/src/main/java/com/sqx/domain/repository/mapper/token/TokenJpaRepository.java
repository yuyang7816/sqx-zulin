package com.sqx.domain.repository.mapper.token;

import com.sqx.domain.repository.entity.sys.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenJpaRepository extends JpaRepository<Token, Long> {



}

package com.sqx.domain.repository.mapper.cash;

import com.sqx.domain.repository.entity.sys.CashOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashJpaRepository extends JpaRepository<CashOut, Long> {



}

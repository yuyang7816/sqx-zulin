package com.sqx.domain.repository.mapper.money;

import com.sqx.domain.repository.entity.sys.MoneyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyDetailsJpaRepository extends JpaRepository<MoneyDetails, Long> {



}

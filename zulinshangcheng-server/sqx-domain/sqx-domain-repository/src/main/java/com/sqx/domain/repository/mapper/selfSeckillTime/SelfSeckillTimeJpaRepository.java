package com.sqx.domain.repository.mapper.selfSeckillTime;

import com.sqx.domain.repository.entity.sys.SelfSecKillTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfSeckillTimeJpaRepository extends JpaRepository<SelfSecKillTime, Long> {

    Page<SelfSecKillTime> findAll(Specification<SelfSecKillTime> specification, Pageable pageable);

}

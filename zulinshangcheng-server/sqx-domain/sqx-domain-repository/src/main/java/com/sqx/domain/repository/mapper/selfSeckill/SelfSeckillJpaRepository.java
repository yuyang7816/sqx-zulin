package com.sqx.domain.repository.mapper.selfSeckill;

import com.sqx.domain.repository.entity.sys.SelfSecKill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfSeckillJpaRepository extends JpaRepository<SelfSecKill, Long> {

    Page<SelfSecKill> findAll(Specification<SelfSecKill> specification, Pageable pageable);

    Page<SelfSecKill> findAll(Pageable pageable);
}

package com.sqx.domain.repository.mapper.selfGroup;

import com.sqx.domain.repository.entity.sys.SelfGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfGroupJpaRepository extends JpaRepository<SelfGroup, Long> {

    Page<SelfGroup> findAll(Specification<SelfGroup> specification, Pageable pageable);


    Page<SelfGroup> findAll(Pageable pageable);


}

package com.sqx.domain.repository.mapper.selfOrderRelation;

import com.sqx.domain.repository.entity.sys.SelfOrderRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRelationJpaRepository extends JpaRepository<SelfOrderRelation, Long> {

    //条件查询带分页
    Page<SelfOrderRelation> findAll(Specification<SelfOrderRelation> specification, Pageable pageable);

    //条件查询
    List<SelfOrderRelation> findAll(Specification<SelfOrderRelation> specification);

}

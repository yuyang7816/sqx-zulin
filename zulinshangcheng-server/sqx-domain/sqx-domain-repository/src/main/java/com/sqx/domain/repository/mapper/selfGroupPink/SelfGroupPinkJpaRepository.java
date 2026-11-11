package com.sqx.domain.repository.mapper.selfGroupPink;

import com.sqx.domain.repository.entity.sys.SelfGroupPink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGroupPinkJpaRepository extends JpaRepository<SelfGroupPink, Long> {

    Page<SelfGroupPink> findAll(Specification<SelfGroupPink> specification, Pageable pageable);

    Page<SelfGroupPink> findAll(Pageable pageable);

    //查询当前商品还在拼团的用户:status=1
    List<SelfGroupPink> findAllByGroupIdAndStatus(Long groupId, Integer status);

}

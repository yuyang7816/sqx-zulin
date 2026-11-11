package com.sqx.domain.repository.mapper.selfGoodsRule;

import com.sqx.domain.repository.entity.sys.SelfGoodsRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfGoodsRuleJpaRepository extends JpaRepository<SelfGoodsRule, Long> {

    //分页查询
    Page<SelfGoodsRule> findAll(Pageable pageable);

}

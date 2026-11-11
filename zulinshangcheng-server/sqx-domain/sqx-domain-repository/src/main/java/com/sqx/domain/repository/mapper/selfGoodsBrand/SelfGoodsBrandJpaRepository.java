package com.sqx.domain.repository.mapper.selfGoodsBrand;

import com.sqx.domain.repository.entity.sys.SelfGoodsBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGoodsBrandJpaRepository extends JpaRepository<SelfGoodsBrand, Long> {

    //分页查询
    Page<SelfGoodsBrand> findAll(Pageable pageable);

    //条件查询
    List<SelfGoodsBrand> findAll(Specification<SelfGoodsBrand> specification);

}

package com.sqx.domain.repository.mapper.selfGoods;

import com.sqx.domain.repository.entity.sys.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsJpaRepository extends JpaRepository<Goods, Long> {

    //条件查询带分页
    Page<Goods> findAll(Specification<Goods> specification, Pageable pageable);

    //不带分页
    List<Goods> findAll(Specification<Goods> specification);

//    List<Goods> findByTypeIdAndBrandId(Specification<Goods> specification, Pageable pageable);
}

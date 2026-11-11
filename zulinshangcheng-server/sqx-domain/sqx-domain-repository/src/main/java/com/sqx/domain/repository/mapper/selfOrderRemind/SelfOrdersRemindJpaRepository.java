package com.sqx.domain.repository.mapper.selfOrderRemind;

import com.sqx.domain.repository.entity.sys.SelfOrderRemind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfOrdersRemindJpaRepository extends JpaRepository<SelfOrderRemind, Long> {

    //条件查询带分页
    Page<SelfOrderRemind> findAll(Specification<SelfOrderRemind> specification, Pageable pageable);

    //条件查询
    List<SelfOrderRemind> findAll(Specification<SelfOrderRemind> specification);

    //根据订单id查询
    SelfOrderRemind findByOrdersId(Long ordersId);

}

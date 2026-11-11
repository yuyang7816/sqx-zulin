package com.sqx.domain.repository.mapper.selfOrder;

import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.sys.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {

    //条件查询带分页
    Page<Orders> findAll(Specification<Orders> specification, Pageable pageable);

    //条件查询
    List<Orders> findAll(Specification<Orders> specification);

    //根据订单号查询
    Orders findByOrderNum(String orderNum);

    //根据订单号查询
    List<Orders> findByPayNum(String payNum);

    //根据虚拟id查询
    Orders findByVirtualId(Long virtualId);

}

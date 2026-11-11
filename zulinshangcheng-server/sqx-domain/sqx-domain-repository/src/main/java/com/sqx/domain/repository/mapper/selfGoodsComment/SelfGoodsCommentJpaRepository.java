package com.sqx.domain.repository.mapper.selfGoodsComment;

import com.sqx.domain.repository.entity.sys.SelfGoodsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGoodsCommentJpaRepository extends JpaRepository<SelfGoodsComment, Long> {

    //分页查询
    Page<SelfGoodsComment> findAll(Specification<SelfGoodsComment> specification, Pageable pageable);

    //条件查询
    List<SelfGoodsComment> findAll(Specification<SelfGoodsComment> specification);

    //根据订单查询评论
    SelfGoodsComment findByOrderId(Long orderId);

    //根据商品id
    List<SelfGoodsComment> findAllByGoodsId(Long goodsId);

}

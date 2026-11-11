package com.sqx.domain.repository.mapper.selfGoodsAttr;

import com.sqx.domain.repository.entity.sys.SelfGoodsAttr;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGoodsAttrJpaRepository extends JpaRepository<SelfGoodsAttr, Long> {

    List<SelfGoodsAttr> findAll(Specification<SelfGoodsAttr> specification);

    /**
     * 根据商品id查询
     */
    @Query(value = "from SelfGoodsAttr s where s.goodsId=:goodsId")
    List<SelfGoodsAttr> findAllByGoodsId(@Param("goodsId") Long goodsId);

}

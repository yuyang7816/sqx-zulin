package com.sqx.domain.repository.mapper.selfGoodsAttrValue;

import com.sqx.domain.repository.entity.sys.SelfGoodsAttrValue;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGoodsAttrValueJpaRepository extends JpaRepository<SelfGoodsAttrValue, Long> {

    List<SelfGoodsAttrValue> findAll(Specification<SelfGoodsAttrValue> specification);

    /**
     * 根据规格id查询
     */
    @Query(value = "from SelfGoodsAttrValue s where s.attrId=:attrId")
    List<SelfGoodsAttrValue> findAllByAttrId(@Param("attrId") Long attrId);
    /**
     * 根据商品id查询
     */
    @Query(value = "from SelfGoodsAttrValue s where s.goodsId=:goodsId")
    List<SelfGoodsAttrValue> findAllByGoodsId(@Param("goodsId") Long goodsId);
}

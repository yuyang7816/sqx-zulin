package com.sqx.domain.repository.mapper.selfGoodsSku;

import com.sqx.domain.repository.entity.sys.SelfGoodsSku;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SelfGoodsSkuJpaRepository extends JpaRepository<SelfGoodsSku, Long> {

    List<SelfGoodsSku> findAll(Specification<SelfGoodsSku> specification);

    /**
     * 根据商品id查询
     */
    @Query(value = "from SelfGoodsSku s where s.goodsId=:goodsId")
    List<SelfGoodsSku> findAllByGoodsId(@Param("goodsId") Long goodsId);

    //sku库存减少
    @Modifying
    @Transactional
    @Query(value = "update SelfGoodsSku s set s.stock=s.stock-:number where s.id=:id")
    Integer lessStock(@Param("id") Long id, @Param("number") Integer number);

    //sku库存增加
    @Modifying
    @Transactional
    @Query(value = "update SelfGoodsSku s set s.stock=s.stock+:number where s.id=:id")
    Integer addStock(@Param("id") Long id, @Param("number") Integer number);

    //sku销量添加
    @Modifying
    @Transactional
    @Query(value = "update SelfGoodsSku s set s.sales=s.sales+:number where s.id=:id")
    Integer addSales(@Param("id") Long id, @Param("number") Integer number);


}

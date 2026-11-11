package com.sqx.domain.repository.mapper.selfGroupSku;

import com.sqx.domain.repository.entity.sys.SelfGroupSku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SelfGroupSkuJpaRepository extends JpaRepository<SelfGroupSku, Long> {

    Page<SelfGroupSku> findAll(Specification<SelfGroupSku> specification, Pageable pageable);


    Page<SelfGroupSku> findAll(Pageable pageable);

    //根据商品id查询
    List<SelfGroupSku> findAllByGoodsId(Long goodsId);

    //拼团sku
    List<SelfGroupSku> findAllByGroupId(Long groupId);

    //秒杀sku
    List<SelfGroupSku> findAllBySecKillId(Long groupId);

    //拼团sku库存减少
    @Modifying
    @Transactional
    @Query(value = "update SelfGroupSku s set s.stock=s.stock-:number,s.pinkStock=s.pinkStock-:number where s.id=:id")
    Integer lessPinkStock(@Param("id") Long id, @Param("number") Integer number);

    //秒杀sku库存减少
    @Modifying
    @Transactional
    @Query(value = "update SelfGroupSku s set s.stock=s.stock-:number,s.secKillStock=s.secKillStock-:number where s.id=:id")
    Integer lessSecKillStock(@Param("id") Long id, @Param("number") Integer number);


}

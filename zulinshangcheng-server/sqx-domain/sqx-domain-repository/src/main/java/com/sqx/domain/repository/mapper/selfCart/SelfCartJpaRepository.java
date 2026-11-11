package com.sqx.domain.repository.mapper.selfCart;

import com.sqx.domain.repository.entity.sys.SelfCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCartJpaRepository extends JpaRepository<SelfCart, Long> {

    //条件查询带分页
    Page<SelfCart> findAll(Specification<SelfCart> specification, Pageable pageable);

    //根据用户id+商品id+skuId查询
    @Query(value = "from SelfCart s where s.userId=:userId and s.goodsId=:goodsId and s.skuId=:skuId")
    SelfCart findByUserIdAndGoodsIdAndSkuId(@Param("userId") Long userId, @Param("goodsId") Long goodsId, @Param("skuId") Long skuId);

}

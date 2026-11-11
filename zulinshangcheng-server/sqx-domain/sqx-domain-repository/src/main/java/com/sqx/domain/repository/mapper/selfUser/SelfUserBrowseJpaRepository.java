package com.sqx.domain.repository.mapper.selfUser;

import com.sqx.domain.repository.entity.sys.SelfUserBrowse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfUserBrowseJpaRepository extends JpaRepository<SelfUserBrowse, Long> {

    //分页查询
    Page<SelfUserBrowse> findAll(Pageable pageable);

    //条件查询
    Page<SelfUserBrowse> findAll(Specification<SelfUserBrowse> specification, Pageable pageable);

    //根据商品id和用户id查询
    @Query(value = "from SelfUserBrowse s where s.goodsId=:goodsId and s.userId=:userId")
    SelfUserBrowse findByGoodsIdAndUserId(@Param("goodsId") Long goodsId, @Param("userId") Long userId);

}

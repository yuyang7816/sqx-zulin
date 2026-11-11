package com.sqx.domain.repository.mapper.selfGoodsType;

import com.sqx.domain.repository.entity.sys.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsTypeJpaRepository extends JpaRepository<GoodsType, Long> {

}

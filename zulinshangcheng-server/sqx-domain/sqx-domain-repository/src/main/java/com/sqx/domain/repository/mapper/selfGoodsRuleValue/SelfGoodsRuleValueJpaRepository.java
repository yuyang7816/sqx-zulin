package com.sqx.domain.repository.mapper.selfGoodsRuleValue;

import com.sqx.domain.repository.entity.sys.SelfGoodsRuleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGoodsRuleValueJpaRepository extends JpaRepository<SelfGoodsRuleValue, Long> {

    /**
     * 根据规格id查询所有规格明细
     */
    @Query(value = "from SelfGoodsRuleValue s where s.ruleId=:ruleId")
    List<SelfGoodsRuleValue> findAllByRuleId(@Param("ruleId") Long ruleId);
}

package com.sqx.domain.repository.mapper.cash;

import com.sqx.domain.repository.entity.sys.CashOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CashRepository extends Repository<CashOut, Long> {
    @Query(value = "from CashOut s where s.relationId=:relationId order by s.id desc")
    List<CashOut> findOne(@Param("relationId") String relationId);

    @Query(value = "from CashOut s where s.zhifubao=:zhifubao order by s.id desc")
    List<CashOut> findByPhone(@Param("zhifubao") String zhifubao);

    @Modifying
    @Query(value = "update CashOut s set s.isOut=:isOut where s.id=:id")
    Integer updateIsOut(@Param("isOut") Boolean isOut, @Param("id") Long id);

    @Query(value = "select sum(s.money) from CashOut s where s.relationId=:relationId and s.state=1")
    String countByRelationId(@Param("relationId") String relationId);

    @Query(value = "select s.relationId from CashOut s group by s.relationId")
    List<Long> findRelationList();

    @Query(value = "select count(s.id) from CashOut s where  s.createAt like concat('%',:time,'%') ")
    Long getCountNumberByTime(@Param("time") String time);

    @Query(value = "select sum(s.money) from CashOut s where  s.createAt like concat('%',:time,'%') ")
    String getSumMoneyByTime(@Param("time") String time);

    @Query(value = "select count(s.id) from CashOut s where s.state=0")
    Long getNotOutMoneyByTime();

    @Query(value = "select * from cash_out where  if(:zhifubaoName!='',zhifubao_name=:zhifubaoName,1=1)" +
            "and if(:relationId!='',relation_id=:relationId,1=1) " +
            "and if(:state!=-2,`state`=:state,1=1)  ",nativeQuery = true)
    Page<CashOut> selectCashList(Pageable pageable, String zhifubaoName, String relationId, Integer state);

    @Query(value = "select sum(money) from cash_out where `state`=:state and create_at like concat('%',:time,'%')",nativeQuery = true)
    BigDecimal selectSumCashOutByState(String time,Integer state);
}
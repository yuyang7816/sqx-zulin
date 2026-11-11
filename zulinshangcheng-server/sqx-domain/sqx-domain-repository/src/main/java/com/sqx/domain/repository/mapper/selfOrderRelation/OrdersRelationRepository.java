package com.sqx.domain.repository.mapper.selfOrderRelation;

import com.sqx.domain.repository.entity.sys.SelfOrderRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRelationRepository extends Repository<SelfOrderRelation, Long> {

    /**
     * 查询所有未入账的
     */
    @Query(value = "from SelfOrderRelation where status = 1")
    List<SelfOrderRelation> findAllByStatus();

    //用户自己本月未到账
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 1 and s.moneyFrom = 2 and s.userId=:userId and s.createAt like concat('%',:month,'%')")
    String sumByUserIdStatus1(@Param("userId") Long userId, @Param("month") String month);

    //用户自己本月已到账
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 2 and s.moneyFrom = 2 and s.userId=:userId and s.createAt like concat('%',:month,'%')")
    String sumByUserIdStatus2(@Param("userId") Long userId, @Param("month") String month);

    //用户团队本月未到账
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 1 and s.moneyFrom = 1 and s.userId=:userId and s.createAt like concat('%',:month,'%')")
    String sumByUserIdStatus1Tuan(@Param("userId") Long userId, @Param("month") String month);

    //用户团队本月已到账
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 2 and s.moneyFrom = 1 and s.userId=:userId and s.createAt like concat('%',:month,'%')")
    String sumByUserIdStatus2Tuan(@Param("userId") Long userId, @Param("month") String month);

    //团队总收益
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 2 and s.moneyFrom in(1,3) and s.userId=:userId")
    String tuanSum(@Param("userId") Long userId);

    //直属团队累计金额
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 2 and s.moneyFrom = 1 and s.lowerUserId=:lowerUserId")
    String sumByLowerUserId(@Param("lowerUserId") Long lowerUserId);

    //非直属团队累计金额
    @Query(value = "select sum(s.commissionPrice) from SelfOrderRelation s where s.status = 2 and s.moneyFrom = 3 and s.lowerUserId=:lowerUserId")
    String sumByLowerUserIds(@Param("lowerUserId") Long lowerUserId);
}
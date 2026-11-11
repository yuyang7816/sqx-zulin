package com.sqx.domain.repository.mapper.selfCoupon;

import com.sqx.domain.repository.entity.sys.SelfCouponUser;
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
public interface SelfCouponUserJpaRepository extends JpaRepository<SelfCouponUser, Long> {

    //分页查询
    Page<SelfCouponUser> findAll(Pageable pageable);

    //条件查询
    Page<SelfCouponUser> findAll(Specification<SelfCouponUser> specification, Pageable pageable);

    List<SelfCouponUser> findAll(Specification<SelfCouponUser> specification);

    List<SelfCouponUser> findByUserIdAndCouponIssueId(Long userId, Long couponIssueId);

    //查询用户的优惠券
    @Query(value = "from SelfCouponUser s where s.userId=:userId")
    List<SelfCouponUser> findAllByUserId(@Param("userId") Long userId);

    //查询用户通用券
    @Query(value = "from SelfCouponUser s where s.userId=:userId and s.status=1 and s.type = 1 ")
    List<SelfCouponUser> findAllByUSerIdAndType1(@Param("userId") Long userId);

    //优惠券被使用
    @Modifying
    @Transactional
    @Query(value = "update SelfCouponUser s set s.status=2 where s.couponUserId=:couponUserId")
    Integer usedCoupon(@Param("couponUserId") Long couponUserId);

    //优惠券退还
    @Modifying
    @Transactional
    @Query(value = "update SelfCouponUser s set s.status=1 where s.couponUserId=:couponUserId")
    Integer backCoupon(@Param("couponUserId") Long couponUserId);


}

package com.sqx.domain.repository.mapper.selfCoupon;


import com.sqx.domain.repository.entity.sys.SelfCouponIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SelfCouponIssueJpaRepository extends JpaRepository<SelfCouponIssue, Long> {

    //分页查询
    Page<SelfCouponIssue> findAll(Pageable pageable);

    //条件查询
    Page<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification, Pageable pageable);

    //领券列表
    List<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification);

    @Query(value = "select * from self_coupon_issue  " +
            "where if(:goodsId!='',goods_ids=:goodsId,1=1) " +
            "and date_format(start_time,'%Y-%m-%d %H:%i:%s')<=date_format(now(),'%Y-%m-%d %H:%i:%s') " +
            "and date_format(end_time,'%Y-%m-%d %H:%i:%s')>=date_format(now(),'%Y-%m-%d %H:%i:%s') " +
            "and status=1 and remain_number>0 " +
            "and ( (coupon_issue_id not in (select coupon_issue_id from self_coupon_user where user_id=:userId) and is_limit=1) or is_limit=2 ) " +
            "order by create_time desc ",
            countQuery = "select count(*) from self_coupon_issue  " +
                    "where if(:goodsId!='',goods_ids=:goodsId,1=1) " +
                    "and date_format(start_time,'%Y-%m-%d %H:%i:%s')<=date_format(now(),'%Y-%m-%d %H:%i:%s') " +
                    "and date_format(end_time,'%Y-%m-%d %H:%i:%s')>=date_format(now(),'%Y-%m-%d %H:%i:%s') " +
                    "and status=1 and remain_number>0 " +
                    "and ( (coupon_issue_id not in (select coupon_issue_id from self_coupon_user where user_id=:userId) and is_limit=1) or is_limit=2 ) " +
                    "order by create_time desc ",nativeQuery = true)
    List<SelfCouponIssue> selectSelfCouponIssueList(String goodsId,Long userId);

    @Modifying
    @Transactional
    @Query(value = "update SelfCouponIssue s set s.couponName=:couponName,s.goodsImages=:goodsImages,s.lessMoney=:lessMoney,s.minMoney=:minMoney where s.couponId=:couponId")
    int updateSelfCouponIssue(String couponName,String goodsImages,String lessMoney,String minMoney,Long couponId);
}

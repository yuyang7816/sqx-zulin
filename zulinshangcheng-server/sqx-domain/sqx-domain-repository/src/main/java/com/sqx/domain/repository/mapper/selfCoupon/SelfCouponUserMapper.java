package com.sqx.domain.repository.mapper.selfCoupon;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【self_coupon_user】的数据库操作Mapper
* @createDate 2024-12-17 18:12:08
* @Entity generator.domain.SelfCouponUser
*/
@Mapper
public interface SelfCouponUserMapper extends BaseMapper<SelfCouponUser> {

    SelfCouponUser selectByCouponUserId(Long couponUserId);

    int updateCouponUserId(SelfCouponUser selfCouponUser);

}





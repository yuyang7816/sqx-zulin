package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.TbCoupon;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
public interface TbCouponService extends IService<TbCoupon> {

    Result addCoupon(TbCoupon tbCoupon);

    IPage<TbCoupon> getCouponPageList(Integer page, Integer limit, TbCoupon tbCoupon);

    Result deleteCoupon(Long couponId);

    Result updateCoupon(TbCoupon tbCoupon);


}

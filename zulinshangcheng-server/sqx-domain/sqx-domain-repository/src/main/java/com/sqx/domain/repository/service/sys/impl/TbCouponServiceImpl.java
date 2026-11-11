package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.TbCoupon;
import com.sqx.domain.repository.mapper.sys.TbCouponDao;
import com.sqx.domain.repository.service.sys.TbCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Service
public class TbCouponServiceImpl extends ServiceImpl<TbCouponDao, TbCoupon> implements TbCouponService {

    @Autowired
    private TbCouponDao tbCouponDao;
    @Override
    public Result addCoupon(TbCoupon tbCoupon) {
        tbCoupon.setDeleteFlag(0);
        tbCouponDao.insert(tbCoupon);
        return Result.success();
    }

    @Override
    public IPage<TbCoupon> getCouponPageList(Integer page, Integer limit, TbCoupon tbCoupon) {
        Page<TbCoupon> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return tbCouponDao.selectPage(pages, new QueryWrapper<>(tbCoupon));
    }

    @Override
    public Result deleteCoupon(Long couponId) {
        tbCouponDao.deleteById(couponId);
        return Result.success();
    }

    @Override
    public Result updateCoupon(TbCoupon tbCoupon) {
        tbCouponDao.updateById(tbCoupon);
        return Result.success();
    }




}

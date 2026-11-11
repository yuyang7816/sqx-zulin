package com.sqx.domain.repository.service.selfCoupon;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfCoupon;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponIssueJpaRepository;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SelfCouponServiceImpl implements SelfCouponService {
    @Autowired
    private SelfCouponJpaRepository jpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "sort")));
        return ResultUtil.success(jpaRepository.findAll(pageable));
    }


    @Override
    public Result saveBody(SelfCoupon entity) {
        entity.setCreateTime(DateUtils.format(new Date()));
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCoupon entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }
}

package com.sqx.domain.repository.service.banner;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.enums.ExceptionEnum;
import com.sqx.domain.repository.entity.sys.Banner;
import com.sqx.domain.repository.mapper.banner.BannerJpaRepository;
import com.sqx.domain.repository.mapper.banner.BannerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 轮播图的service实现类
 */
@Service
public class BannerServiceImpl implements IBannerService {
    @Autowired
    private BannerJpaRepository bannerJpaRepository;
    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Result findAll() {
        return ResultUtil.success(bannerJpaRepository.findAll());
    }
    @Override
    public Result saveBody(String id, String url, String image) {
        Banner banner = new Banner();
        banner.setId(Long.valueOf(id));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setImage_url(image);
        banner.setCreateAt(sdf.format(now));
        banner.setState("true");
        banner.setUrl(url);
        return ResultUtil.success(bannerJpaRepository.save(banner));
    }


    @Override
    public Result findOne(long id) {
        return ResultUtil.success(bannerJpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(long id) {
        bannerJpaRepository.deleteById(id);
        return ResultUtil.success();
    }


    @Override
    @Transactional
    public Result updateBody(Banner banner) {
        Banner foodBanner1 = bannerJpaRepository.findById(banner.getId()).orElse(null);
        assert foodBanner1 != null;
        BeanUtils.copyProperties(banner, foodBanner1);
        return ResultUtil.success();
    }

    @Override
    @Transactional
    public Result updateState(String state, String id) {
        Integer adminInfo = bannerRepository.updateState(state, Long.valueOf(id));
        if (adminInfo == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ExceptionEnum.STATE_PWD_ERROR);
        }
    }


}

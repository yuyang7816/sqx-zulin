package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.BrandType;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.sys.BrandTypeDao;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.service.sys.BrandTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author Admin
 * @description 针对表【brand_type(品牌分类表)】的数据库操作Service实现
 * @createDate 2024-09-14 14:35:38
 */
@Service
public class BrandTypeServiceImpl extends ServiceImpl<BrandTypeDao, BrandType>
        implements BrandTypeService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Result addBrandType(BrandType brandType) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        QueryWrapper<BrandType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_type_name", brandType.getBrandTypeName());
        if (brandType.getId() != null) {
            queryWrapper.ne("id", brandType.getId());
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("品牌类型名称已存在");
            }
            brandType.setCreateTime(new Date());
            brandType.setSectionId(brandType.getSectionId());
            baseMapper.updateById(brandType);
            return Result.success();
        } else {
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("品牌类型名称已存在");
            }
            brandType.setSectionId(brandType.getSectionId());
            brandType.setCreateTime(new Date());
            baseMapper.insert(brandType);
            return Result.success();
        }
    }

    @Override
    public Result deleteBrandType(Long id) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        BrandType brandType = baseMapper.selectById(id);
        Goods goods = goodsDao.selectOne(new QueryWrapper<Goods>().eq("brand_id", brandType.getId()));
        if (goods != null) {
            return Result.failed("该品牌类型下有商品，无法删除");
        }
        if (brandType != null) {
            baseMapper.deleteById(id);
            return Result.success();
        }
        return Result.failed("该品牌类型不存在或已删除");
    }

    @Override
    public IPage<BrandType> getBrandTypeList(Integer page, Integer limit, BrandType brandType) {
        Page<BrandType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<BrandType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(brandType.getBrandTypeName())){
            queryWrapper.like("brand_type_name", "%" + brandType.getBrandTypeName() + "%");
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pages, queryWrapper);
    }

    @Override
    public Result selectById(Long id) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        BrandType brandType = baseMapper.selectById(id);
        if (brandType == null) {
            return Result.failed("该品牌类型不存在");
        }
        return Result.success(brandType);
    }

    @Override
    public Result selectBySectionId(Long sectionId) {
        if (sectionId == null) {
            return Result.failed("参数错误");
        }
        if (baseMapper.selectCount(new QueryWrapper<BrandType>().eq("section_id", sectionId)) == 0) {
            return Result.success(Collections.emptyList());
        }
        return Result.success(baseMapper.selectList(new QueryWrapper<BrandType>().eq("section_id", sectionId)));
    }


}





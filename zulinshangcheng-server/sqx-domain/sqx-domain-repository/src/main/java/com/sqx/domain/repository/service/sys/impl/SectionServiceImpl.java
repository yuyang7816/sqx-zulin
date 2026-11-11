package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.BrandType;
import com.sqx.domain.repository.entity.sys.Section;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.sys.BrandTypeDao;
import com.sqx.domain.repository.mapper.sys.SectionDao;
import com.sqx.domain.repository.service.sys.SectionService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/18 13:22
 */
@Service
public class SectionServiceImpl extends ServiceImpl<SectionDao, Section> implements SectionService {

    @Autowired
    private BrandTypeDao brandTypeDao;
    @Override
    public Result addSection(Section section) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        QueryWrapper<Section> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_name", section.getSectionName());
        if (section.getId() != null) {
            queryWrapper.ne("id", section.getId());
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("专区名称已存在");
            }
            section.setCreateTime(new Date());
            int result = baseMapper.updateById(section);
            if (result <= 0) {
                return Result.failed("修改失败");
            }
            return Result.success();
        } else {
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("专区名称已存在");
            }
            section.setCreateTime(new Date());
            int result = baseMapper.insert(section);
            if (result <= 0){
                return Result.failed("添加失败");
            }
            return Result.success();
        }
    }

    @Override
    public Result deleteSection(Long id) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        Section section = baseMapper.selectById(id);
        BrandType brandType = brandTypeDao.selectOne(new QueryWrapper<BrandType>().eq("section_id", id));
        if (brandType != null){
            return Result.failed("该专区下有品牌，无法删除");
        }
        if (section != null){
            int result = baseMapper.deleteById(id);
            if (result <= 0){
                return Result.failed("删除失败");
            }
            return Result.success();
        }
        return Result.failed("专区不存在");
    }

    @Override
    public IPage<Section> getSectionList(Integer page, Integer limit, Section section) {
        IPage<Section> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(section).orderByDesc("create_time"));
    }

    @Override
    public Result selectSection(Long id) {
        Section section = baseMapper.selectById(id);
        if (section != null) {
            return Result.success(section);
        }
        return Result.failed("专区不存在");
    }
}

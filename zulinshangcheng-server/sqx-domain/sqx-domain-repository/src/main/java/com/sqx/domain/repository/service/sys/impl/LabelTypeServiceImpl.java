package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.LabelType;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.labelType.LabelTypeDao;
import com.sqx.domain.repository.service.sys.LabelTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Admin
 * @description 针对表【label_type(分类标签)】的数据库操作Service实现
 * @createDate 2024-09-14 14:41:55
 */
@Service
public class LabelTypeServiceImpl extends ServiceImpl<LabelTypeDao, LabelType>
        implements LabelTypeService {

    @Override
    public Result addLabelType(LabelType labelType) {
        QueryWrapper<LabelType> query = new QueryWrapper<>();
        query.eq("label", labelType.getLabel());
        if (labelType.getLabelId() != null) {
            query.ne("label_id", labelType.getLabelId());
            Long count = baseMapper.selectCount(query);
            if (count > 0) {
                return Result.failed("该标签名称已存在");
            }
            labelType.setCreateTime(new Date());
            baseMapper.updateById(labelType);
            return Result.success();
        } else {
            Long count = baseMapper.selectCount(query);
            if (count > 0) {
                return Result.failed("该标签名称已存在");
            }
            labelType.setIsEnable(1);
            labelType.setCreateTime(new Date());
            baseMapper.insert(labelType);
            return Result.success();
        }
    }

    @Override
    public Result deleteLabelType(Long id) {
        LabelType labelType = baseMapper.selectById(id);
        if (labelType != null) {
            baseMapper.deleteById(labelType.getLabelId());
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @Override
    public IPage<LabelType> getLabelTypeList(Integer page, Integer limit, LabelType labelType) {
        IPage<LabelType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            // 使用一个非常大的数来模拟全量查询
            pages = new Page<>(1, Integer.MAX_VALUE);
        }
        QueryWrapper<LabelType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(labelType.getLabel())) {
            queryWrapper.like("label", "%" + labelType.getLabel() + "%");
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pages,queryWrapper);
    }

    @Override
    public Result selectByLabelTypeId(Long id) {
        LabelType labelType = baseMapper.selectById(id);
        if (labelType != null){
            return Result.success(labelType);
        }
        return Result.failed("该标签不存在");
    }


}





package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Business;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.sys.BusinessDao;
import com.sqx.domain.repository.service.sys.BusinessService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Admin
 * @description 针对表【business(商家认证表)】的数据库操作Service实现
 * @createDate 2024-09-14 14:37:31
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessDao, Business>
        implements BusinessService {

    @Autowired
    private UserDao userDao;

    @Override
    public Result addBusiness(Business business) {
        UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_id", business.getUserId()));
        if (user == null) {
            return Result.failed("用户未登录");
        }
        Long count = baseMapper.selectCount(new QueryWrapper<Business>()
                .eq("legal_number", business.getLegalNumber())
                .eq("business_name", business.getBusinessName()));
        if (count > 0) {
            return Result.failed("商家信息已存在，请勿重复提交");
        }
        if (business.getLng() == null || business.getLat() == null){
            return Result.failed("经纬度不能为空");
        }
        business.setUserId(user.getUserId());
        business.setStatus(0);
        business.setCreateTime(new Date());
        int result = baseMapper.insert(business);
        if (result > 0) {
            return Result.success();
        }
        return Result.failed();
    }

    @Override
    public Result updateBusiness(Business business) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        if (business.getId() != null) {
            business.setUpdateTime(new Date());
            int result = baseMapper.updateById(business);
            if (result <= 0) {
                return Result.failed("修改失败");
            }
            return Result.success();
        }
        return Result.failed("参数错误");
    }

    @Override
    public Result deleteBusiness(Long id) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return Result.failed("用户未登录");
        }
        Business business = baseMapper.selectById(id);
        if (business != null) {
            baseMapper.deleteById(id);
            return Result.success();
        }
        return Result.failed("该商家认证不存在");
    }

    @Override
    public IPage<Business> getBusinessList(Integer page, Integer limit, Business business) {
        IPage<Business> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Business> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(business.getBusinessName())){
            queryWrapper.like("business_name", "%" + business.getBusinessName() + "%");
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pages, queryWrapper);
    }

    @Override
    public Result selectByBusinessId(Long id) {
        Business business = baseMapper.selectById(id);
        if (business != null) {
            return Result.success(business);
        }
        return Result.failed("该商家认证不存在");
    }

    @Override
    public Result selectBusinessByUserId(Long userId) {
        Business business = baseMapper.selectOne(new QueryWrapper<Business>().eq("user_id",userId));
        if (business != null) {
            return Result.success(business);
        }
        return Result.failed("该商家认证不存在");
    }

    @Override
    public Result examineByBusinessId(Business business) {
        Business business1 = baseMapper.selectById(business.getId());
        if (business1 == null) {
            return Result.failed("该商家认证不存在");
        }
        if (business1.getStatus() != 0) {
            return Result.failed("当前状态不允许操作，请刷新后重试");
        }
        if (business.getStatus() == 1) {
            business1.setStatus(1);
            business1.setAuditTime(new Date());
            int result = baseMapper.updateById(business1);
            if (result <= 0) {
                return Result.failed();
            }
            UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>()
                    .eq("user_id", business1.getUserId()));
            if (user != null) {
                user.setUserType(2);
                userDao.updateById(user);
                return Result.success("操作成功");
            } else {
                return Result.failed("用户不存在");
            }
        } else if (business.getStatus() == 2) {
            business1.setStatus(2);
            business1.setAuditTime(new Date());
            int result = baseMapper.updateById(business1);
            if (result <= 0) {
                return Result.failed();
            }
            return Result.success("操作成功");
        } else {
            return Result.failed("商家认证失败");
        }
    }

    @Override
    public Result repeatedBusiness(Business business) {
        UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_id", business.getUserId()));
        if (user == null) {
            return Result.failed("用户未登录");
        }
        Business business1 = baseMapper.selectOne(new QueryWrapper<Business>()
                .eq("user_id", business.getUserId()));
        if (business1 == null){
            return Result.failed("未找到商家认证信息");
        }
        if (business1.getStatus() != 2){
            return Result.failed("当前状态不允许操作，请刷新后重试");
        }
        business1.setUserId(user.getUserId());
        business1.setStatus(0);
        business1.setUpdateTime(new Date());
        int result = baseMapper.updateById(business1);
        if (result <= 0){
            return Result.failed("提交失败");
        }
        return Result.success();
    }

}





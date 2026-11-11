package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.GoodsRecovery;
import com.sqx.domain.repository.entity.sys.GoodsSpecifications;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.sys.GoodsRecoveryDao;
import com.sqx.domain.repository.mapper.sys.GoodsSpecificationsDao;
import com.sqx.domain.repository.service.sys.GoodsRecoveryService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【goods_recovery(二手物品回收估价表)】的数据库操作Service实现
 * @createDate 2024-06-28 10:50:20
 */
@Service
public class GoodsRecoveryServiceImpl extends ServiceImpl<GoodsRecoveryDao, GoodsRecovery>
        implements GoodsRecoveryService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private GoodsSpecificationsDao goodsSpecificationsDao;


    @Override
    public Result saveGoodsRecovery(GoodsRecovery goodsRecovery) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.failed("该用户不存在或未登录");
        }
        QueryWrapper<GoodsRecovery> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", goodsRecovery.getName())
                .eq("delete_flag", 0);
        if (goodsRecovery.getId() != null) {
            queryWrapper.ne("id", goodsRecovery.getId());
        }
        if (goodsRecovery.getId() != null) {
            goodsRecovery.setUpdateTime(new Date());
            goodsRecovery.setUpdateBy(principal.getUserId());
            return Result.upStatus(baseMapper.updateById(goodsRecovery));
        } else {
            goodsRecovery.setCreateBy(principal.getUserId());
            goodsRecovery.setCreateTime(new Date());
            return Result.upStatus(baseMapper.insert(goodsRecovery));
        }
    }

    @Override
    public Result updateGoodsRecovery(GoodsRecovery goodsRecovery) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.failed("该用户不存在或未登录");
        }
        GoodsRecovery goodsRecovery1 = baseMapper.selectById(goodsRecovery.getId());
        if (ObjectUtils.isEmpty(goodsRecovery1)) {
            return Result.failed("该回收物品不存在");
        }
//        int count = baseMapper.selectCount(new QueryWrapper<GoodsRecovery>()
//                .eq("name", goodsRecovery.getName())
//                .ne("id", goodsRecovery.getId())
//                .eq("delete_flag", 0));
//        if (count > 0) {
//            return Result.error("该回收物品名称已存在");
//        }
        goodsRecovery.setUpdateTime(new Date());
        goodsRecovery.setUpdateBy(principal.getUserId());
        return Result.upStatus(baseMapper.updateById(goodsRecovery));
    }

    @Override
    public Result deleteGoodsRecovery(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.failed("该用户不存在或未登录");
        }
        GoodsRecovery goodsRecovery = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(goodsRecovery)) {
            return Result.failed("该回收物品不存在");
        }
        return Result.upStatus(baseMapper.deleteById(goodsRecovery.getId()));
    }

    @Override
    public Result getGoodsRecoveryList(Integer pageNum, Integer pageSize, String name, Long brandId) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<GoodsRecovery> list = baseMapper.selectListByBrandId(name, brandId);
        return Result.success(new PageInfo<>(list));
    }

    @Override
    public Result getGoodsRecoveryById(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.failed("该用户不存在或未登录");
        }
        GoodsRecovery goodsRecovery = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(goodsRecovery)) {
            return Result.failed("该回收物品不存在");
        }
        return Result.success(goodsRecovery);
    }

    @Override
    public Result estimatedPriceByRecoveryId(Long recoveryId, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("当前用户未登录，请登录");
        }
        if (user.getIsAuth() == 0) {
            return Result.failed("当前用户未实名认证，请实名认证");
        }
        List<GoodsSpecifications> list = goodsSpecificationsDao.selectList(new QueryWrapper<GoodsSpecifications>()
                .eq("recovery_id", recoveryId)
                .eq("delete_flag", 0));
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!ObjectUtils.isEmpty(list)) {
//            for (GoodsSpecifications goodsSpecifications : list) {
//                totalPrice = totalPrice.add(goodsSpecifications.getPrice());
//            }
        }
//        GoodsRecoveryUser goodsRecoveryUser = new GoodsRecoveryUser();
//        goodsRecoveryUser.setUserId(userId);
//        goodsRecoveryUser.setRecoveryId(recoveryId);
//        goodsRecoveryUser.setTotalPrice(totalPrice);
//        int count = goodsRecoveryUserDao.insert(goodsRecoveryUser);
//        if (count <= 0) {
//            return Result.error("该回收物品估价失败");
//        }
        return Result.success(totalPrice);
    }

    @Override
    public Result selectListBySpecifications(Long userId, Long id) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("当前用户未登录，请登录");
        }
        if (user.getIsAuth() == 0) {
            return Result.failed("当前用户未实名认证，请实名认证");
        }
        List<GoodsSpecifications> list = goodsSpecificationsDao.selectList(new QueryWrapper<GoodsSpecifications>()
                .eq("recovery_id", id)
                .eq("delete_flag", 0));
        if (!ObjectUtils.isEmpty(list)) {
            return Result.success(list);
        }
        return Result.success(new ArrayList<>());
    }

    @Override
    public Result selectListByHistory() {
        return null;
    }
}





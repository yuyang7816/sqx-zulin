package com.sqx.domain.repository.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.mapper.app.UserMoneyDetailsDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.notIn("classify", Arrays.asList(5,6));
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success(baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date,userId);
    }

    @Override
    public void setParentMoneyDetails(Long userId, Long parentId, BigDecimal money, Integer userLevel) {
        //添加用户邀请人钱包详情
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(parentId);
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetails.setClassify(3);
        if (userLevel == 1) {
            userMoneyDetails.setTitle("一级用户开通会员分成");
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("一级用户开通会员分成金额:" + money);
            userMoneyDetails.setRelationId(userId);
            baseMapper.insert(userMoneyDetails);
        } else if (userLevel == 2) {
            userMoneyDetails.setTitle("二级用户开通会员分成");
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("二级用户开通会员分成金额:" + money);
            userMoneyDetails.setRelationId(userId);
            baseMapper.insert(userMoneyDetails);
        }

    }

    @Override
    public BigDecimal getOneProfit(Long userId, Integer level) {

        return baseMapper.getOneProfit(userId, level);

    }

    @Override
    public Result queryCreditScore(Integer page, Integer limit, Long userId, Integer classify, Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(classify!=null){
            queryWrapper.eq("classify", 5);
        }
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success(baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Result queryGoldNum(Integer page, Integer limit, Long userId, Integer classify, Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(classify!=null){
            queryWrapper.eq("classify", 6);
        }
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success(baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Result queryUserDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
//        queryWrapper.notIn("classify", Arrays.asList(5,6));
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success(baseMapper.selectPage(page1, queryWrapper));
    }
}

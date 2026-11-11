package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Faqs;
import com.sqx.domain.repository.mapper.sys.FaqsDao;
import com.sqx.domain.repository.service.sys.FaqsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【faqs(常见问题表)】的数据库操作Service实现
* @createDate 2024-09-19 16:01:59
*/
@Service
public class FaqsServiceImpl extends ServiceImpl<FaqsDao, Faqs>
    implements FaqsService {

    @Override
    public Result addFaqs(Faqs faqs) {
        if (faqs.getId() != null){
            faqs.setCreateTime(new Date());
            baseMapper.updateById(faqs);
            return Result.success();
        }else {
            faqs.setCreateTime(new Date());
            baseMapper.insert(faqs);
            return Result.success();
        }
    }

    @Override
    public Result deleteFaqs(Long id) {
        Faqs faqs = baseMapper.selectById(id);
        if (faqs != null){
            baseMapper.deleteById(id);
            return Result.success();
        }
        return Result.failed("删除失败");
    }


    @Override
    public IPage<Faqs> getFaqsList(Integer page, Integer limit, Faqs faqs) {
        IPage<Faqs> pages;
        if (page != null && limit != null){
            pages = new Page<>(page,limit);
        }else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Faqs> queryWrapper = new QueryWrapper<>();
        if (faqs.getQuestion()!=null){
            queryWrapper.like("question", "%" + faqs.getQuestion() + "%");
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pages,queryWrapper);
    }

    @Override
    public Result selectByFaqsId(Long id) {
        Faqs faqs = baseMapper.selectById(id);
        if (faqs != null){
            return Result.success(faqs);
        }
        return Result.failed("查询失败");
    }
}





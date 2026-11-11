package com.sqx.domain.repository.service.selfGoodsRule;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfGoodsRule;
import com.sqx.domain.repository.entity.sys.SelfGoodsRuleValue;
import com.sqx.domain.repository.mapper.selfGoodsRule.SelfGoodsRuleJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsRuleValue.SelfGoodsRuleValueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfGoodsRuleServiceImpl implements SelfGoodsRuleService {
    @Autowired
    private SelfGoodsRuleJpaRepository jpaRepository;
    @Autowired
    private SelfGoodsRuleValueJpaRepository ruleValueJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SelfGoodsRule> pageList = jpaRepository.findAll(pageable);
        List<SelfGoodsRule> all = pageList.getContent();
        for (SelfGoodsRule r : all) {
            r.setRuleValue(ruleValueJpaRepository.findAllByRuleId(r.getId()));
        }
        return ResultUtil.success(pageList);
    }

    @Override
    public Result info() {
        List<SelfGoodsRule> all = jpaRepository.findAll();
        for (SelfGoodsRule r : all) {
            r.setRuleValue(ruleValueJpaRepository.findAllByRuleId(r.getId()));
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result saveBody(SelfGoodsRule entity) {
        entity.setCreateTime(DateUtils.createTime()); //创建时间
        SelfGoodsRule save = jpaRepository.save(entity);
        List<SelfGoodsRuleValue> list = entity.getRuleValue();
        for (SelfGoodsRuleValue v : list) {
            v.setRuleId(save.getId());
            ruleValueJpaRepository.save(v);
        }
        return ResultUtil.success(save);
    }

    @Override
    public Result updateBody(SelfGoodsRule entity) {
        //查询所有值删掉
        List<SelfGoodsRuleValue> allRuleValue = ruleValueJpaRepository.findAllByRuleId(entity.getId());
        ruleValueJpaRepository.deleteAll(allRuleValue);
        //放入新值
        List<SelfGoodsRuleValue> list = entity.getRuleValue();
        for (SelfGoodsRuleValue v : list) {
            v.setRuleId(entity.getId());
            ruleValueJpaRepository.save(v);
        }
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        SelfGoodsRule s = jpaRepository.findById(id).orElse(null);
        s.setRuleValue(ruleValueJpaRepository.findAllByRuleId(id));
        return ResultUtil.success(s);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

}

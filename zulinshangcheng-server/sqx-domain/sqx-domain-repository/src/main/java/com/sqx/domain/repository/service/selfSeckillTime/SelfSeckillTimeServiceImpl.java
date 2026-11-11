package com.sqx.domain.repository.service.selfSeckillTime;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.GoodsTypeResult;
import com.sqx.domain.repository.entity.sys.SelfSecKillTime;
import com.sqx.domain.repository.mapper.selfSeckillTime.SelfSeckillTimeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelfSeckillTimeServiceImpl implements SelfSeckillTimeService {
    @Autowired
    private SelfSeckillTimeJpaRepository jpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.ASC, "startHour")));
        return ResultUtil.success(jpaRepository.findAll(pageable));
    }

    @Override
    public Result allList() {
        List<SelfSecKillTime> all = jpaRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "startHour")));
        for (int i = 0; i < all.size(); i++) {
            SelfSecKillTime s = all.get(i);
            Integer startHour = s.getStartHour();
            Integer nextHour = 24;
            if (i+1 < all.size()){
                nextHour = all.get(i+1).getStartHour();
            }
            Integer status = DateUtils.secKillStartHours(startHour, nextHour);
            s.setStatus(status);
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result result() {
        List<SelfSecKillTime> list = jpaRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "startHour")));
        List<GoodsTypeResult> resultList = new ArrayList<>();
        for (SelfSecKillTime g : list) {
            GoodsTypeResult result = new GoodsTypeResult();
            result.setValue(g.getSecKillTimeId());
            result.setLabel(g.getStartHour()+"点开始");
            resultList.add(result);
        }
        return ResultUtil.success(resultList);
    }

    @Override
    public Result saveBody(SelfSecKillTime entity) {
        List<SelfSecKillTime> all = jpaRepository.findAll();
        for (SelfSecKillTime s : all) {
            if (s.getStartHour().equals(entity.getStartHour())){
                return ResultUtil.error(-1, "秒杀时段重复");
            }
        }
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfSecKillTime entity) {
        List<SelfSecKillTime> all = jpaRepository.findAll();
        for (SelfSecKillTime s : all) {
            if (s.getStartHour().equals(entity.getStartHour()) && entity.getSecKillTimeId() != s.getSecKillTimeId()){
                return ResultUtil.error(-1, "秒杀时段重复");
            }
        }
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

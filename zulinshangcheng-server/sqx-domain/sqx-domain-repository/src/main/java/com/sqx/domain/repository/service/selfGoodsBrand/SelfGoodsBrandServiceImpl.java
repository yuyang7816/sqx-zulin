package com.sqx.domain.repository.service.selfGoodsBrand;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.GoodsTypeResult;
import com.sqx.domain.repository.entity.sys.SelfGoodsBrand;
import com.sqx.domain.repository.mapper.selfGoodsBrand.SelfGoodsBrandJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelfGoodsBrandServiceImpl implements SelfGoodsBrandService {
    @Autowired
    private SelfGoodsBrandJpaRepository jpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SelfGoodsBrand> pageList = jpaRepository.findAll(pageable);
        return ResultUtil.success(pageList);
    }

    @Override
    public Result result() {
        List<SelfGoodsBrand> type1 = jpaRepository.findAll();
        List<GoodsTypeResult> resultList = new ArrayList<>();
        for (SelfGoodsBrand g : type1) {
            GoodsTypeResult result = new GoodsTypeResult();
            result.setValue(g.getBrandId());
            result.setLabel(g.getBrandName());
            resultList.add(result);
        }
        return ResultUtil.success(resultList);
    }

    @Override
    public Result saveBody(SelfGoodsBrand entity) {
        entity.setCreateTime(DateUtils.createTime()); //创建时间
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfGoodsBrand entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        SelfGoodsBrand s = jpaRepository.findById(id).orElse(null);
        return ResultUtil.success(s);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

}

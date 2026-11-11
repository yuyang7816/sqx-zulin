package com.sqx.domain.repository.service.jifen;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.JiFen;
import com.sqx.domain.repository.mapper.jifen.JiFenJpaRepository;
import com.sqx.domain.repository.mapper.jifen.JiFenRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 积分的实现类
 */
@Service
public class JiFenServiceImpl implements IJiFenService {
    @Autowired
    private JiFenJpaRepository jiFenJpaRepository;
    @Autowired
    private JiFenRepository jiFenRepository;


    @Override
    public Result findAll() {
        return ResultUtil.success(jiFenJpaRepository.findAll());
    }

    @Override
    public Result saveBody(JiFen jiFen) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        jiFen.setCreateAt(sdf.format(now));
        return ResultUtil.success(jiFenJpaRepository.save(jiFen));
    }

    @Override
    public Result findOne(long id) {
        return ResultUtil.success(jiFenJpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result findList(long id) {
        return ResultUtil.success(jiFenJpaRepository.findAllByUserIdOrderByCreateAtDesc(String.valueOf(id)));
    }

    @Override
    public Result delete(long id) {
        jiFenJpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result updateBody(JiFen jiFen) {
        JiFen jiFen1 = jiFenJpaRepository.findById(jiFen.getId()).orElse(null);
        assert jiFen1 != null;
        BeanUtils.copyProperties(jiFen1, jiFen);
        return ResultUtil.success();
    }
}

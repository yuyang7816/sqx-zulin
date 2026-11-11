package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.ScFollow;
import com.sqx.domain.repository.entity.sys.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScFollowDao extends BaseMapper<ScFollow> {



    int getMyFollowCount(Long userId);

    int getMyFollowGoodsCount(Long userId);

    Page<Goods> getFollowGoodsList(@Param("pages") Page<Goods> pages,
                                   @Param("userId") Long userId,
                                   @Param("follow") ScFollow follow);

    List<ScFollow> selectByUserId(Long userId);

    List<ScFollow> selectByFanId(Long userId);
}

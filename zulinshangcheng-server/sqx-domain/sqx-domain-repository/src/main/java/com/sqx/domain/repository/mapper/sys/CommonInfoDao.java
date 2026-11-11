package com.sqx.domain.repository.mapper.sys;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CommonInfoDao extends BaseMapper<CommonInfo> {

    List<CommonInfo> findByCondition(@Param("condition") String condition);

    CommonInfo findOne(@Param("type") int type);



}
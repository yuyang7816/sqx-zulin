package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.app.App;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户升级
 *
 */
@Mapper
public interface AppDao extends BaseMapper<App> {

    List<App> selectNewApp();

}

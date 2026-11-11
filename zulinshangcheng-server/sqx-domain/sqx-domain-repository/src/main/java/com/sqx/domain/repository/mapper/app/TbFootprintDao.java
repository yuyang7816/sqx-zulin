package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.TbFootprint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Admin
 * @description 针对表【tb_footprint(用户浏览足迹表)】的数据库操作Mapper
 * @createDate 2024-09-20 10:04:53
 * @Entity generator.domain.TbFootprint
 */
@Mapper
public interface TbFootprintDao extends BaseMapper<TbFootprint> {

    IPage<TbFootprint> selectByUserId(@Param("pages") Page<TbFootprint> pages,
                                      @Param("userId") Long userId);

    Long selectCountByUserId( @Param("userId") Long userId);
}





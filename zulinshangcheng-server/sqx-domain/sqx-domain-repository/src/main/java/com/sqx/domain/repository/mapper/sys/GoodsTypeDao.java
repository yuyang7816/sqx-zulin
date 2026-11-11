package com.sqx.domain.repository.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.domain.repository.entity.sys.GoodsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【goods_type(商品分类)】的数据库操作Mapper
* @createDate 2024-09-14 14:38:18
* @Entity generator.domain.GoodsType
*/
@Mapper
public interface GoodsTypeDao extends BaseMapper<GoodsType> {

    List<GoodsType> findAllLevel1ByName(@Param("name")String name);

    List<GoodsType> findAllTypesByName(@Param("name")String name);

    List<GoodsType> findAllTypes(@Param("name")String name);
}





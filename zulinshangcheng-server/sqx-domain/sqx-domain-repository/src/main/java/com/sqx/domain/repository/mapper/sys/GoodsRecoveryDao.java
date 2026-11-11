package com.sqx.domain.repository.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.GoodsRecovery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【goods_recovery(二手物品回收估价表)】的数据库操作Mapper
* @createDate 2024-06-28 10:50:20
* @Entity generator.domain.GoodsRecovery
*/
@Mapper
public interface GoodsRecoveryDao extends BaseMapper<GoodsRecovery> {

    List<GoodsRecovery> selectListByBrandId(@Param("name") String name,
                                            @Param("brandId")Long brandId);
}





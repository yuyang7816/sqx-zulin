package com.sqx.domain.repository.mapper.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.domain.repository.entity.sys.Goods;
import org.springframework.data.repository.query.Param;

/**
* @author Admin
* @description 针对表【goods(商品表)】的数据库操作Mapper
* @createDate 2024-09-14 14:38:18
* @Entity generator.domain.Goods
*/
public interface GoodsDao extends BaseMapper<Goods> {

    IPage<Goods> selectGoodsList(@Param("pages") IPage<Goods> pages,
                                 @Param("typeId") Long typeId,
                                 @Param("title") String title);

    IPage<Goods> selectGoodsByBrand(@Param("pages")IPage<Goods> pages,
                                    @Param("typeId")Long typeId,
                                    @Param("brandId")Long brandId,
                                    @Param("title")String title);

    IPage<Goods> selectPageByList(IPage<Goods> pages,Long threeTypeId,String title,Long brandId,Integer type);

}





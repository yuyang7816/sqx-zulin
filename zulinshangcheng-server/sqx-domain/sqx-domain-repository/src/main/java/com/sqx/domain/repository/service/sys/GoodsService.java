//package com.sqx.domain.repository.service.sys;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.sys.Goods;
//
///**
//* @author Admin
//* @description 针对表【goods(商品表)】的数据库操作Service
//* @createDate 2024-09-14 14:38:18
//*/
//public interface GoodsService extends IService<Goods> {
//
//    Result addGoodsByAdmin(Goods goods);
//    Result addGoods(Goods goods);
//
//    Result examineGoods(Long id, Integer status);
//
//    Result deleteGoods(Long id);
//
//    IPage<Goods> getGoodsList(Integer page, Integer limit, Goods goods);
//
//    Result selectByGoodsId(Long id,Long userId);
//
//    IPage<Goods> selectBySectionId(Integer page, Integer limit,Long sectionId,Long brandId,Integer priceSort);
//
//    IPage<Goods> selectHotGoods(Integer page, Integer limit,Long labelId,Integer typeState);
//
//    IPage<Goods> selectedRecommendations(Integer page, Integer limit,Long brandId,Integer priceSort,Integer typeState);
//}

package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsAccessory;

/**
* @author Admin
* @description 针对表【goods_accessory(商品配件表)】的数据库操作Service
* @createDate 2024-12-24 14:58:39
*/
public interface GoodsAccessoryService extends IService<GoodsAccessory> {

    Result addGoodsAccessory(GoodsAccessory goodsAccessory);

    Result deleteGoodsAccessory(Long id);

    Result selectGoodsAccessory(Long id);

    Result selectGoodsAccessoryList(Integer page, Integer limit,GoodsAccessory goodsAccessory);

    Result selectListByTypeId(Long typeId);
}

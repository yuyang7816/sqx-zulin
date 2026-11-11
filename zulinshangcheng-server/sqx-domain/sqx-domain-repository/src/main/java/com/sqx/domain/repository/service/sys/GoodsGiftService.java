package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsGift;

/**
* @author Admin
* @description 针对表【goods_gift(赠品数量)】的数据库操作Service
* @createDate 2024-12-24 14:58:44
*/
public interface GoodsGiftService extends IService<GoodsGift> {

    Result addGift(GoodsGift goodsGift);

    Result deleteGift(Long id);

    Result selectGift(Long id);

    Result selectGiftList(Integer page,Integer limit,GoodsGift goodsGift);

    Result selectListByTypeId(Long typeId);
}

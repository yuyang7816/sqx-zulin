package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsSpecifications;

/**
* @author Admin
* @description 针对表【goods_specifications(回收的二手物品的规格表)】的数据库操作Service
* @createDate 2024-09-14 14:38:18
*/
public interface GoodsSpecificationsService extends IService<GoodsSpecifications> {

    Result addGoodsSpecifications(GoodsSpecifications goodsSpecifications);

    Result deleteGoodsSpecifications(Long id);


    IPage<GoodsSpecifications> getGoodsSpecificationsList(Integer page, Integer limit, GoodsSpecifications goodsSpecifications);

    Result selectBySpecId(Long id);


}

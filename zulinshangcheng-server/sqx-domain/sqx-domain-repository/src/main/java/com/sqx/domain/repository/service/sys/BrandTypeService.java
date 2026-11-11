package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.BrandType;

/**
* @author Admin
* @description 针对表【brand_type(品牌分类表)】的数据库操作Service
* @createDate 2024-09-14 14:35:38
*/
public interface BrandTypeService extends IService<BrandType> {

    Result addBrandType(BrandType brandType);

    Result deleteBrandType(Long id);

    IPage<BrandType> getBrandTypeList(Integer page,Integer limit, BrandType brandType);

    Result selectById(Long id);

    Result selectBySectionId(Long sectionId);


}

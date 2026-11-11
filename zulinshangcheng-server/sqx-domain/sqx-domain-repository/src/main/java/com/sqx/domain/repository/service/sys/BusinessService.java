package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Business;

/**
* @author Admin
* @description 针对表【business(商家认证表)】的数据库操作Service
* @createDate 2024-09-14 14:37:31
*/
public interface BusinessService extends IService<Business> {

    Result addBusiness(Business business);

    Result updateBusiness(Business business);

    Result deleteBusiness(Long id);

    IPage<Business> getBusinessList(Integer page, Integer limit, Business business);

    Result selectByBusinessId(Long id);

    Result selectBusinessByUserId(Long userId);

    Result examineByBusinessId(Business business);

    Result repeatedBusiness(Business business);
}

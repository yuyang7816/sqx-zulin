package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.RentalGuidelines;

/**
* @author Admin
* @description 针对表【rental_guidelines(租赁须知表)】的数据库操作Service
* @createDate 2024-09-19 16:02:07
*/
public interface RentalGuidelinesService extends IService<RentalGuidelines> {

    Result addRentalGuidelines(RentalGuidelines rentalGuidelines);

    Result deleteRentalGuidelines(Long id);


    IPage<RentalGuidelines> getRentalGuidelinesList(Integer page, Integer limit, RentalGuidelines rentalGuidelines);

    Result selectByRentalGuidelinesId(Long id);
}

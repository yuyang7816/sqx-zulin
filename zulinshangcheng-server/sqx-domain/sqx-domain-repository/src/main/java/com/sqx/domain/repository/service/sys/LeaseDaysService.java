package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.LeaseDays;

/**
* @author Admin
* @description 针对表【lease_days(租赁天数配置表)】的数据库操作Service
* @createDate 2024-09-14 14:41:55
*/
public interface LeaseDaysService extends IService<LeaseDays> {

    Result addLeaseDays(LeaseDays leaseDays);

    Result deleteLeaseDays(Long id);

    Result selectDaysById(Long id);

    IPage<LeaseDays> getLeaseDaysList(Integer page, Integer limit, LeaseDays leaseDays);
}

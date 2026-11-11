package com.sqx.domain.repository.service.expressDelivery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.ExpressDelivery;

/**
* @author Admin
* @description 针对表【express_delivery(快递公司表)】的数据库操作Service
* @createDate 2024-12-16 09:48:57
*/
public interface ExpressDeliveryService extends IService<ExpressDelivery> {

    Result addExpressDelivery(ExpressDelivery expressDelivery);

    Result deleteExpressDeliveryById(Long id);

    Result selectExpressDeliveryList(Integer page, Integer limit, ExpressDelivery expressDelivery);

    Result selectExpressDeliveryById(Long id);

}

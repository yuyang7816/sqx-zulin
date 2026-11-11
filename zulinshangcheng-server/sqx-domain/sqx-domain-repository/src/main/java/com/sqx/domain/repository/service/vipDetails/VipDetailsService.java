package com.sqx.domain.repository.service.vipDetails;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.VipDetails;
import com.sqx.domain.repository.entity.app.vo.PaymentFormVO;

import javax.servlet.http.HttpServletRequest;

public interface VipDetailsService extends IService<VipDetails> {


    IPage<VipDetails> getVipDetailsList(Integer page, Integer limit,Integer isEnable);

    IPage<VipDetails> selectVipDetailsList(Integer page, Integer limit,Integer isEnable);

    Result insetVipDetails(VipDetails vipDetails);

    Result updateVipDetails(VipDetails vipDetails);

    Result deleteVipDetails(Long id);

    Result userBuyVip(Long userId, PaymentFormVO paymentFormVO) throws Exception;

    Result balancePay(Long userId, Long id);

    void callbackBusiness(String outTradeNo);

    Result giveUserVip(Long userId, Integer num);

    Result cancelUserVip(Long userId);

    void callBackByVip();
}

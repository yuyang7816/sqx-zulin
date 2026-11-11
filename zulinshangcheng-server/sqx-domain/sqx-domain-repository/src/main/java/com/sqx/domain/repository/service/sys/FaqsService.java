package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Faqs;

/**
* @author Admin
* @description 针对表【faqs(常见问题表)】的数据库操作Service
* @createDate 2024-09-19 16:01:59
*/
public interface FaqsService extends IService<Faqs> {

    Result addFaqs(Faqs faqs);

    Result deleteFaqs(Long id);


    IPage<Faqs> getFaqsList(Integer page, Integer limit, Faqs faqs);

    Result selectByFaqsId(Long id);


}

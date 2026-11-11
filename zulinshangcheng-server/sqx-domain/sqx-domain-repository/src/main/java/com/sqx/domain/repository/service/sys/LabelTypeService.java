package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.LabelType;

/**
* @author Admin
* @description 针对表【label_type(分类标签)】的数据库操作Service
* @createDate 2024-09-14 14:41:55
*/
public interface LabelTypeService extends IService<LabelType> {

    Result addLabelType(LabelType labelType);

    Result deleteLabelType(Long id);

    IPage<LabelType> getLabelTypeList(Integer page, Integer limit, LabelType labelType);

    Result selectByLabelTypeId(Long id);

}

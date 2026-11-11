package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Section;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/18 13:22
 */
public interface SectionService extends IService<Section> {

    Result addSection(Section section);

    Result deleteSection(Long id);

    IPage<Section> getSectionList(Integer page, Integer limit, Section section);

    Result selectSection(Long id);

}

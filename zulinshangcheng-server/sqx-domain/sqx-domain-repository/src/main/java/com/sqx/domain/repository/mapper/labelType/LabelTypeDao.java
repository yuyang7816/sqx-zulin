package com.sqx.domain.repository.mapper.labelType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.sys.LabelType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 分类标签 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-05-29
 */
@Mapper
public interface LabelTypeDao extends BaseMapper<LabelType> {
    IPage<LabelType> selectPageByType(Page<LabelType> pages, LabelType labelType);

    LabelType selectLabelByIsRecovery(@Param("label")String label);

    LabelType selectLabelByOrdinary(@Param("label")String label);
}

package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.SysEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @description 针对表【sys_evaluate(评价表 用户)】的数据库操作Mapper
 * @createDate 2024-07-10 15:03:40
 * @Entity generator.domain.SysEvaluate
 */
@Mapper
public interface SysEvaluateDao extends BaseMapper<SysEvaluate> {

    SysEvaluate getSysEvaluate(Long orderId);

    List<SysEvaluate> getTbOrdersListByTurnover(Page<SysEvaluate> pages);

    List<SysEvaluate> selectListByUserId(@Param("params") Map<String, Object> params);

    IPage<SysEvaluate> selectPageByEvaluateList(@Param("pages") Page<SysEvaluate> pages,
                                                @Param("sysEvaluate") SysEvaluate sysEvaluate);

    IPage<SysEvaluate> selectPageByOrderEvaluateList(@Param("pages") Page<SysEvaluate> pages,
                                                     @Param("sysEvaluate") SysEvaluate sysEvaluate);

    List<SysEvaluate> selectListByIndentNumber(@Param("params") Map<String, Object> params);

    IPage<SysEvaluate> selectListByBuyUserId(@Param("byUserId") Long byUserId);

    int selectCountByUserId(Long byUserId, List<String> orderNumbers);

    List<SysEvaluate> selectAllEvaluationsBySellerAndBuyers(@Param("params") Map<String, Object> params);

    IPage<SysEvaluate> selectEvaluateListByRecycle(@Param("pages") Page<SysEvaluate> pages,
                                                   @Param("sysEvaluate") SysEvaluate sysEvaluate);
}





package com.sqx.system.service;




import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.sys.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

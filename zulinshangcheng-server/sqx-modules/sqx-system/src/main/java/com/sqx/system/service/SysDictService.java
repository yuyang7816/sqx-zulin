package com.sqx.system.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.sys.SysDictEntity;

import java.util.Map;

/**
 * 数据字典
 *
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


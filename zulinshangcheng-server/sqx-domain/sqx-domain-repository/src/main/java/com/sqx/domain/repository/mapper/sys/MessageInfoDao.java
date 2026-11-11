package com.sqx.domain.repository.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/24 13:23
 */
public interface MessageInfoDao extends BaseMapper<MessageInfo> {
    int updateSendState(@Param("userId") Long userId);
}

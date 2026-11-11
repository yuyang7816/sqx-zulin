package com.sqx.auth.service;



import com.sqx.domain.repository.entity.sys.AdminInfo;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.entity.sys.SysUserTokenEntity;
import com.sqx.domain.repository.entity.sys.Token;

import java.util.Set;

/**
 * @author chengming
 * @since 2024/7/11
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);

    Token selectByToken(String accessToken);

    AdminInfo queryUserByAdminId(Long adminId);
}

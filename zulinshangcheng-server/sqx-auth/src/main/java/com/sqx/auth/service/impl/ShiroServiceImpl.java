package com.sqx.auth.service.impl;


import com.sqx.auth.service.ShiroService;
import com.sqx.common.core.constant.Constant;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.admin.AdminRepository;
import com.sqx.domain.repository.mapper.sys.SysMenuDao;
import com.sqx.domain.repository.mapper.sys.SysUserDao;
import com.sqx.domain.repository.mapper.sys.SysUserTokenDao;
import com.sqx.domain.repository.mapper.token.TokenJpaRepository;
import com.sqx.domain.repository.mapper.token.TokenRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengming
 * @since 2024/7/11
 */
@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }

    @Override
    public Token selectByToken(String accessToken) {
        Token token = tokenRepository.findOne(accessToken);
        if (token != null) {
            return token;
        }
        return null;
    }

    @Override
    public AdminInfo queryUserByAdminId(Long adminId) {
        AdminInfo adminInfo = adminRepository.getUserByAdminId(adminId);
        if (adminInfo != null) {
            return adminInfo;
        }
        return null;
    }
}

package com.sqx.auth.oauth2;

import com.sqx.auth.service.ShiroService;
import com.sqx.domain.repository.entity.sys.AdminInfo;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.entity.sys.SysUserTokenEntity;
import com.sqx.domain.repository.entity.sys.Token;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 *
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
//        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        Token tokenEntity = shiroService.selectByToken(accessToken);
        //token失效
        if(tokenEntity == null){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        AdminInfo user = shiroService.queryUserByAdminId(Long.valueOf(tokenEntity.getAdminId()));
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}

package com.sqx.auth.config;

import com.sqx.auth.oauth2.OAuth2Filter;
import com.sqx.auth.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chengming
 * @since 2024/7/11
 *
 * shiro 配置
 */
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/wxPay/notifyJsApi", "anon");
        filterMap.put("/app/wxPay/notifyMp", "anon");
        filterMap.put("/app/wxPay/notify", "anon");
        filterMap.put("/app/aliPay/notifyApp", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/activity/**", "anon");
        filterMap.put("/banner/**", "anon");
        filterMap.put("/courseClassification/selectCourseClassification", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/file/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/chatSocket/**", "anon");
        filterMap.put("/websocket/**", "anon");
        filterMap.put("/search/**", "anon");
        filterMap.put("/alioss/**","anon");
        filterMap.put("/captcha/**", "anon");
        filterMap.put("/admin/login/**", "anon");
//        filterMap.put("/goods/**", "anon");
//        filterMap.put("/goodsType/**", "anon");
//        filterMap.put("/selfActivity/**", "anon");
//        filterMap.put("/selfBanner/**", "anon");
//        filterMap.put("/advert/**", "anon");
//        filterMap.put("/orders/**", "anon");
        filterMap.put("/selfGoodsBrand/**", "anon");
        filterMap.put("/selfCouponUser/**", "anon");
        filterMap.put("/selfCouponIssue/**", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}

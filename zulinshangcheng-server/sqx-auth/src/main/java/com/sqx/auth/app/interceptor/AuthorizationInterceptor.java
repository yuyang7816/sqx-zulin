package com.sqx.auth.app.interceptor;


import com.sqx.auth.app.annotation.Login;
import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.exception.SqxException;
import com.sqx.domain.repository.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.pagehelper.util.MetaObjectUtil.method;

/**
 * 权限(Token)验证
 *
 */
@Slf4j
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 检查 handler 是否为 HandlerMethod 类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            annotation = method.getAnnotation(Login.class);
//            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }
        if(annotation == null){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestPath = request.getRequestURI();
        log.debug("Method: " + method.getName() + ", IgnoreSecurity: " + method.isAnnotationPresent(IgnoreSecurity.class));
        log.debug("requestPath: " + requestPath);
        if (requestPath.contains("/swagger") || requestPath.contains("/configuration/ui")) {
            return true;
        }
        if (requestPath.contains("/jd") || requestPath.contains("/pdd") || requestPath.contains("/error") || requestPath.contains("/taobao")) {
            return true;
        }
//        if(handler instanceof HandlerMethod) {
//            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
//        }else{
//            return true;
//        }
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }

        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new SqxException(jwtUtils.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
        }

        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new SqxException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));

        return true;
    }
}

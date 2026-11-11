package com.sqx.common.core.domain;


import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengming
 * @since 2024/7/11
 */
public class Res extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Res() {
        put("code", 0);
        put("msg", "success");
    }
    public static Res upStatus(Integer rows) {
        return rows > 0 ? success() : error();
    }

    public static Res error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static Res error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static Res error(int code, String msg) {
        Res r = new Res();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Res success(String msg) {
        Res r = new Res();
        r.put("msg", msg);
        return r;
    }

    public static Res success(Map<String, Object> map) {
        Res r = new Res();
        r.putAll(map);
        return r;
    }

    public static Res success() {
        return new Res();
    }

    @Override
    public Res put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

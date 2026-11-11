package com.sqx.common.core.domain;

import com.sqx.common.core.enums.IErrorCode;
import com.sqx.common.core.enums.SystemErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author chengming
 * @since 2024/7/11
 * 全局返回结果
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6720704866912777982L;


    //    error_code 状态值：0 极为成功，其他数值代表失败
    private Integer status;

    //    error_msg 错误信息，若status为0时，为success
    private String msg;

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 数据
     */
    private T data;

    public Result() {
    }
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer code) {
//        this.status = code;
//    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
//        put("code", code);
//        put("msg", message);
//        if (data != null) {
//            put("data", data);
//        }
    }

//    @Override
//    public Result put(String key, Object value) {
//        super.put(key, value);
//        // 特别处理"data"键，同时更新data字段
//        if ("data".equals(key)) {
//            this.data = (T) value;
//        }
//        return this;
//    }
    /**
     * 成功返回结果
     * @return
     */
    public static <T> Result<T> success() {
        return new Result<>(SystemErrorCode.SUCCESS.getCode(), SystemErrorCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(SystemErrorCode.SUCCESS.getCode(), SystemErrorCode.SUCCESS.getMessage(), data);
//        if (data != null) {
//            result.put("data", data);
//        }
        return result;
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(SystemErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> Result<T> failed(IErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(SystemErrorCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(SystemErrorCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(SystemErrorCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(SystemErrorCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<>(SystemErrorCode.UNAUTHORIZED.getCode(), SystemErrorCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<>(SystemErrorCode.FORBIDDEN.getCode(), SystemErrorCode.FORBIDDEN.getMessage(), data);
    }

    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static Result upStatus(Integer rows) {
        return rows > 0 ? success() : failed();
    }


    //    error_code 状态值：0 极为成功，其他数值代表失败
//    private Integer status;
//
//    //    error_msg 错误信息，若status为0时，为success
//    private String msg;
//
//    //    content 返回体报文的出参，使用泛型兼容不同的类型
//    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer code) {
        this.status = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData(Object object) {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';

    }

}

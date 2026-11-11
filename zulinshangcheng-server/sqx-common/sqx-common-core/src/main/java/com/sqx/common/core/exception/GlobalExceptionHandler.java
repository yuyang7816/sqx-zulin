package com.sqx.common.core.exception;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.enums.SystemErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author chengming
 * @since 2024/7/11
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BizException.class)
    public Result<String> handleBizException(BizException e) {
        if (e.getErrorCode() != null) {
            return Result.failed(e.getErrorCode());
        }
        return Result.failed(e.getMessage());
    }

    @ExceptionHandler(value = SqxException.class)
    public Result<String> handleSqxException(SqxException e) {
        return Result.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerNoFoundException(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.failed(SystemErrorCode.NO_HANDLER_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return Result.failed("数据库中已存在该记录");
    }

/*    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e){
        logger.error(e.getMessage(), e);
        return Result.failed("没有权限，请联系管理员授权");
    }*/
    /**
     * 参数校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return Result.failed(message);
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        logger.error(e.getMessage(), e);
        return Result.failed();
    }
}

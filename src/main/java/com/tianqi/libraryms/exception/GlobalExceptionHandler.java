package com.tianqi.libraryms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GYY
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理方法参数无效异常
     * 当请求参数绑定到方法参数上发生验证错误时，此处理器将被调用
     * 它收集所有的字段错误信息，并以键值对的形式返回，键是字段名，值是错误信息
     * @param ex 方法参数无效异常对象，包含错误的字段和默认错误信息
     * @return 返回一个响应实体，包含错误信息的映射和HTTP状态码
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 创建一个错误信息的映射
        Map<String, String> errors = new HashMap<>();
        // 遍历所有字段错误，将其添加到错误信息映射中
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

        // 返回一个包含错误信息映射和BAD_REQUEST状态的响应实体
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
package com.tianqi.libraryms.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GYY
 * @description 用于封装错误信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    private Integer code;
    // 错误代码
    private String message;
    // 错误消息
    private String details;
    // 错误详细信息

    // 构造函数
    public ErrorInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
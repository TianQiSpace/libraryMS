package com.tianqi.libraryms.result;

import lombok.Data;

/**
 * @author GYY
 * @description 用于进行前后端页面的数据沟通
 */
@Data
public class Result<T>  {
    private Integer code;
    private String msg;
    private T data;
    private Boolean success;
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(false);
        return result;
    }
}

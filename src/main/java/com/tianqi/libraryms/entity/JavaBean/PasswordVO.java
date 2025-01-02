package com.tianqi.libraryms.entity.JavaBean;

import lombok.Data;

/**
 * @author GYY
 * @description 用于修改密码接收信息
 */
@Data
public class PasswordVO {
    private String oldPassword;
    private String newPassword;
}

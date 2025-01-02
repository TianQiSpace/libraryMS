package com.tianqi.libraryms.util;

import com.tianqi.libraryms.config.settingConfig.AppValidationConfig;
import com.tianqi.libraryms.result.Result;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author GYY
 * @date 2024/12/10 10:53
 * @description 校验前端传来的账户信息的基础规范
 */
@Component
public class accountCheck {
    @Resource
    private AppValidationConfig appValidationConfig;

    //1.账户信息校验；
    public Result<String> baseCheck(String username, String password, String email) {
        // 1. 是否为空字符串。
        if (StringUtils.isAllBlank(username, password)) {
            return Result.error(-1, "用户名密码存在空值");
        }
        // 2. 检验用户名和密码是否满足注册的要求。
        if (username.length() < appValidationConfig.getUsername().getMinLength() ||
                username.length() > appValidationConfig.getUsername().getMaxLength() ||
                password.length() != appValidationConfig.getPassword().getLength()) {
            return Result.error(-1, "用户名或密码长度不符合要求");
        }
        // 3. 判断用户名是否存在特殊字符。
        // 匹配中文、英文、数字
        if (!username.matches(appValidationConfig.getUsername().getRegex())) {
            return Result.error(-1, "账户名存在不允许的特殊字符");
        }
        //4.邮箱校验
        if (!"不校验".equals(email)) {
            if (!email.matches(appValidationConfig.getEmail().getRegex())) {
                return Result.error(-1, "邮箱格式不正确！");
            }
        }

        return Result.success("校验通过");
    }

}

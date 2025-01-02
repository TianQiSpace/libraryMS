package com.tianqi.libraryms.config.settingConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author GYY
 * @description 用来从系统配置文件中动态加载业务配置【这里是注册登录时候的数据长度限制】
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.validation")
public class AppValidationConfig {
    private Username username = new Username();
    private Password password = new Password();
    private Email email = new Email();

    @Data
    public static class Username {
        private int minLength;
        private int maxLength;
        private String regex;
    }

    @Data
    public static class Password {
        private int length;
    }
    @Data
    public static class Email {
        private String regex;
    }
}
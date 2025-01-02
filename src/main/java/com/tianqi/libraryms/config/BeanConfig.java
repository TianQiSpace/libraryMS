package com.tianqi.libraryms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author GYY
 * @description 用于配置扫描路径
 */
@Configuration
@ComponentScan("com.tianqi.libraryms")
@MapperScan("com.tianqi.libraryms.mapper")
public class BeanConfig {
}

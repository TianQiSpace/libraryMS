package com.tianqi.libraryms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GYY
 * @description 启动类
 */
@SpringBootApplication(scanBasePackages = {"com.tianqi.libraryms"})
public class LibraryMsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryMsApplication.class, args);
    }

}

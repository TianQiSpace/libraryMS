package com.tianqi.libraryms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author GYY
 * @description 用于进行页面控制
 */
@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public String showAndRedirectLoginPage() {
        return "index";
    }
}

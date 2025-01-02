package com.tianqi.libraryms.controller.NotificationsController;

import com.tianqi.libraryms.service.NotificationsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author GYY
 * @description 消息控制器
 */
@Controller
public class NotificationsController {
    @Resource
    private NotificationsService notificationsService;
    @GetMapping("/normalUserNotifications/readAll/{userId}")
    public String readAllNotifications(@PathVariable Integer userId)
    {
        notificationsService.readAllNormalUserNotifications(userId);
        return "redirect:/normalUserNotifications/index";
    }
}

package com.tianqi.libraryms.controller.NotificationsController;

import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.entity.Notifications;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.NotificationsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.tianqi.libraryms.constant.GlobalConstant.NORMAL_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 用于初始化加载消息界面
 */
@Controller
public class NotificationsPageController {
    @Resource
    private NotificationsService notificationsService;
    @GetMapping("/normalUserNotifications/index")
    public String notificationsIndexPage(Model model, HttpServletRequest request) {
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        final Result<List<Notifications>> userNotifications = notificationsService.getNormalUserNoReadNotifications(user.getId());
        if (userNotifications.getData().size()==0){
            return "normalUser/missPage";
        }else{
            model.addAttribute("notifications", userNotifications.getData());
            // 在控制器中设置 showIcon 变量
            model.addAttribute("showIcon", false);
            return "normalUser/notifications";
        }

    }
}

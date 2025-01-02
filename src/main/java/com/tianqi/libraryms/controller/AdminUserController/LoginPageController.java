package com.tianqi.libraryms.controller.AdminUserController;

import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.AdminUserService;
import com.tianqi.libraryms.service.SuperAdminUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static com.tianqi.libraryms.constant.GlobalConstant.ADMIN_USER_LOGIN_STATUS;
import static com.tianqi.libraryms.constant.GlobalConstant.SUPER_ADMIN_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 用于管理管理员的登录登出
 */
@Controller
public class LoginPageController {
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private SuperAdminUserService superAdminUserService;

    /**
     * @return 返回视图信息。
     * @description 用于管理员登录：普通管理员和超级管理员
     */
    @PostMapping("/admin/loginForm")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, Object> loginData, HttpServletRequest request) {
        String username = (String) loginData.get("username");
        String password = (String) loginData.get("password");
        int role = (int) loginData.get("role");

        if (role == 2) {
            // 普通管理员
            if (request.getSession().getAttribute(ADMIN_USER_LOGIN_STATUS) != null) {
                return ResponseEntity.ok(Map.of("success", true, "message", "已登录", "redirectTo", "/admin/adminUser/index"));
            } else {
                Result<Integer> result = adminUserService.adminLogin(username, password, request);
                if (result.getCode() == 200) {
                    return ResponseEntity.ok(Map.of("success", true, "message", "登录成功", "redirectTo", "/admin/adminUser/index"));
                } else if (result.getCode() == -1) {
                    return ResponseEntity.ok(Map.of("success", false, "message", result.getMsg()));
                } else {
                    return ResponseEntity.status(401).body(Map.of("message", result.getMsg()));
                }
            }
        } else if (role == 3) {
            // 超级管理员
            if (request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS) != null) {
                return ResponseEntity.ok(Map.of("success", true, "message", "已登录", "redirectTo", "/admin/superAdmin/index"));
            } else {
                Result<Integer> result = superAdminUserService.superAdminLogin(username, password, request);
                if (result.getCode() == 200) {
                    return ResponseEntity.ok(Map.of("success", true, "message", "登录成功", "redirectTo", "/admin/superAdmin/index"));
                } else if (result.getCode() == -1) {
                    return ResponseEntity.ok(Map.of("success", false, "message", result.getMsg()));
                } else {
                    return ResponseEntity.status(401).body(Map.of("message", result.getMsg()));
                }
            }
        } else {
            return ResponseEntity.ok(Map.of("success", false, "message", "无效的用户类型"));
        }
    }
}

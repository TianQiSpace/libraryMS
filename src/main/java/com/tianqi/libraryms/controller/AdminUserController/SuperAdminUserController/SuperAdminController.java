package com.tianqi.libraryms.controller.AdminUserController.SuperAdminUserController;

import com.tianqi.libraryms.service.SuperAdminUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author GYY
 * @description 超管控制器类
 */
@Controller
public class SuperAdminController {
    @Resource
    private SuperAdminUserService superAdminUserService;
    //1.超管登出
    @PostMapping("/admin/superAdmin/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        superAdminUserService.superAdminLogout(request);
        // 返回成功响应
        return ResponseEntity.ok(Map.of("success", true, "message", "登出成功！"));
    }
}

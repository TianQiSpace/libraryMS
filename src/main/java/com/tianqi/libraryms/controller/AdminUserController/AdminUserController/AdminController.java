package com.tianqi.libraryms.controller.AdminUserController.AdminUserController;

import com.tianqi.libraryms.entity.AdminUser;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.AdminUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author GYY
 * @description 用于管理员的控制类
 */
@Controller
public class AdminController {
    @Resource
    private AdminUserService adminUserService;

    /**
     * @description 管理员退出登录。
     * @param request 请求对象
     * @return 重定向到index页面。
     */
    @PostMapping("/admin/adminUser/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        adminUserService.adminLogout(request);
        // 返回成功响应
        return ResponseEntity.ok(Map.of("success", true, "message", "登出成功！"));
    }

    /**
     * @description 用与将管理员注册信息提交到数据库中。
     * @param adminUser 管理员注册信息的封装
     * @param request 请求对象
     * @param model 模型，用于传递数据
     * @return 重定向到管理员登录页面。
     */
    @PostMapping("/superAdmin/adminRegister")
    public String adminRegisterHandle(AdminUser adminUser, HttpServletRequest request, Model model) {
        Result<String> result = adminUserService.addAdminUser(adminUser, request);
        model.addAttribute("result", result);
        System.out.println(result.getMsg());
        if(result.getCode()!=200){
            //就说明有错误，就返回错误页面
            //TODO：跳转到错误页面。
        }
        return "redirect:/superAdmin/adminUserManageIndex";
    }
    //3.管理员管理界面
    @GetMapping("/superAdmin/adminUserManageIndex")
    public String adminUserManage(Model model,HttpServletRequest request) {
        //1.查询展示所有的普通管理员信息
        Result<List<AdminUser>> result = adminUserService.getAllAdminUser(request);
        model.addAttribute("adminUserList", result.getData());
        return "admin/superAdmin/adminUserManage";
    }

    /**
     * @description 用于收集前端传来的管理员信息，进行更新。
     * @param adminUser 前端传来的管理员信息的封装
     * @param model 用于传递数据
     * @param request 请求对象
     * @return 重定向到管理员管理界面。
     */
    @PostMapping("/superAdmin/adminUserUpdate")
    public String adminUserUpdateHandle(AdminUser adminUser, Model model, HttpServletRequest request) {
        Result<String> result = adminUserService.updateAdminUser(adminUser, request);
        System.out.println(result.getMsg());
        model.addAttribute("result", result);
        return "redirect:/superAdmin/adminUserManageIndex";
    }

    /**
     * @description 超级管理员删除管理员信息。
     * @param id 管理员id
     * @param request 请求对象
     * @param model 模型，用于传递数据
     * @return 重定向到管理员管理界面。
     */
    @GetMapping("/superAdmin/deleteAdminUserById/{id}")
    public String deleteAdminUserById(@PathVariable("id") Integer id,HttpServletRequest request,Model model) {
        Result<String> result = adminUserService.deleteAdminUser(id, request);
        model.addAttribute("result", result);
        return "redirect:/superAdmin/adminUserManageIndex";
    }
}

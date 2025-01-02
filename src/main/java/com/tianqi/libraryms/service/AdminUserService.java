package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.AdminUser;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author GYY
* @description 针对表【admin_user(用户表，存储系统中的用户信息)】的数据库操作Service
*/
public interface AdminUserService extends IService<AdminUser> {
    /**
     * @description  面向普通管理员进行登录。
     * @param username 用户名
     * @param password 密码
     * @param request 用于提供服务的session信息
     * @return 返回标准的结果对象。
     */
    //普通管理员登录【完全体】
    Result<Integer> adminLogin(String username, String password, HttpServletRequest request);

    /**
     * @description  面向普通管理员进行登出。
     * @param request 用于提供服务的session信息
     * @return 返回标准的结果对象。
     */
    //普通管理员登出【完全体】
    Result<Integer> adminLogout(HttpServletRequest request);
    /**
     * @description  面向超级管理员进行新增管理员操作。
     * @param adminUser 管理员信息封装
     * @param request 用于提供服务的session信息
     * @return 返回标准的结果对象，封装了新增管理员的ID。
     */
    //超管新增管理员【完全体】
    Result<String> addAdminUser(AdminUser adminUser, HttpServletRequest request);
    //超管删除管理员信息【完全体】
    Result<String> deleteAdminUser(Integer id, HttpServletRequest request);
    //超管修改管理员信息【完全体】
    Result<String> updateAdminUser(AdminUser adminUser, HttpServletRequest request);
    //超管获取所有管理员信息【完全体】
    Result<List<AdminUser>> getAllAdminUser(HttpServletRequest request);
    //超管获取单个管理员信息【完全体】
    Result<AdminUser> getAdminUserById(Integer id, HttpServletRequest request);
}

package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.SuperAdminUser;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author GYY
* @description 针对表【super_admin_user(用户表，存储系统中的用户信息)】的数据库操作Service
*/
public interface SuperAdminUserService extends IService<SuperAdminUser> {
Result<Integer> superAdminLogin(String username, String password, HttpServletRequest request);
Result<Integer> superAdminLogout(HttpServletRequest request);
}

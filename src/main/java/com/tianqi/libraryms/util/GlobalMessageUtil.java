package com.tianqi.libraryms.util;

import com.tianqi.libraryms.entity.AdminUser;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.entity.SuperAdminUser;

/**
 * @author GYY
 * @description 用于封装普通用户的一系列方法
 */
public class GlobalMessageUtil {
    //1.对普通用户信息进行脱敏
    public static NormalUser getSafetyUser(NormalUser normalUser) {
        NormalUser safetyUser = new NormalUser();
        safetyUser.setId(normalUser.getId());
        safetyUser.setUsername(normalUser.getUsername());
        safetyUser.setEmail(normalUser.getEmail());
        safetyUser.setGender(normalUser.getGender());
        safetyUser.setAge(normalUser.getAge());
        safetyUser.setRole(normalUser.getRole());
        safetyUser.setCreatedTime(normalUser.getCreatedTime());
        safetyUser.setUpdatedTime(normalUser.getUpdatedTime());
        return safetyUser;
    }
    //2.对普通管理员信息进行脱敏
    public static AdminUser getSafetyAdmin(AdminUser adminUser) {
        AdminUser safetyAdmin = new AdminUser();
        safetyAdmin.setId(adminUser.getId());
        safetyAdmin.setUsername(adminUser.getUsername());
        safetyAdmin.setEmail(adminUser.getEmail());
        safetyAdmin.setGender(adminUser.getGender());
        safetyAdmin.setAge(adminUser.getAge());
        safetyAdmin.setRole(adminUser.getRole());
        safetyAdmin.setCreatedTime(adminUser.getCreatedTime());
        safetyAdmin.setUpdatedTime(adminUser.getUpdatedTime());
        return safetyAdmin;
    }
    //3.对Super管理员信息进行脱敏
    public static SuperAdminUser getSafetySuperAdmin(SuperAdminUser superAdminUser) {
        SuperAdminUser safetySuperAdmin = new SuperAdminUser();
        safetySuperAdmin.setId(superAdminUser.getId());
        safetySuperAdmin.setUsername(superAdminUser.getUsername());
        safetySuperAdmin.setEmail(superAdminUser.getEmail());
        safetySuperAdmin.setGender(superAdminUser.getGender());
        safetySuperAdmin.setAge(superAdminUser.getAge());
        safetySuperAdmin.setRole(superAdminUser.getRole());
        safetySuperAdmin.setCreatedTime(superAdminUser.getCreatedTime());
        safetySuperAdmin.setUpdatedTime(superAdminUser.getUpdatedTime());
        return safetySuperAdmin;
    }
}

package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.constant.GlobalConstant;
import com.tianqi.libraryms.entity.SuperAdminUser;
import com.tianqi.libraryms.mapper.SuperAdminUserMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.SuperAdminUserService;
import com.tianqi.libraryms.util.GlobalMessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author GYY
* @description 针对表【super_admin_user(用户表，存储系统中的用户信息)】的数据库操作Service实现
*/
@Service
public class SuperAdminUserServiceImpl extends ServiceImpl<SuperAdminUserMapper, SuperAdminUser>
    implements SuperAdminUserService{

    @Override
    public Result<Integer> superAdminLogin(String username, String password, HttpServletRequest request) {
        // 创建一个默认的结果对象，用于返回
        Result<Integer> loginResult=null;
        // 1. 首先进行数据校验。
        if (StringUtils.isAllBlank(username, password)) {
            loginResult = Result.error(-1, "账号密码存在空值");
            return loginResult;
        }
        //2. 校验账号长度信息[4-20], 密码长度8
        if (username.length() < 4 || username.length() > 20 || password.length() != 8) {
            loginResult = Result.error(-2, "账号或密码信息长度不合法");
            return loginResult;
        }
        //3. 校验账户是否存在特殊字符，匹配中文、英文、数字
        String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        if (!username.matches(regex)) {
            loginResult = Result.error(-3, "账号信息含有非法字符");
            return loginResult;
        }
        //4. 进行密码的加密并匹配
        String safePassword = DigestUtils.md5Hex(GlobalConstant.SALT + password);
        QueryWrapper<SuperAdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        final SuperAdminUser superAdminUser = this.getOne(queryWrapper);
        if (superAdminUser == null) {
            loginResult = Result.error(-4, "账户不存在!");
            return loginResult;
        }
        if (!superAdminUser.getPassword().equals(safePassword)) {
            loginResult = Result.error(-5, "账户密码错误!");
            return loginResult;
        }
        //5. 对用户信息进行脱敏处理。
        final SuperAdminUser safetySuperAdmin = GlobalMessageUtil.getSafetySuperAdmin(superAdminUser);
        loginResult = Result.success(superAdminUser.getId());
        loginResult.setCode(200);
        request.getSession().setAttribute(GlobalConstant.SUPER_ADMIN_USER_LOGIN_STATUS, safetySuperAdmin);
        return loginResult;

    }

    @Override
    public Result<Integer> superAdminLogout(HttpServletRequest request) {
        //业务逻辑如下：
        //1.如果存在，则进行删除操作。
        request.getSession().removeAttribute(GlobalConstant.SUPER_ADMIN_USER_LOGIN_STATUS);
        //2.返回成功信息
        return Result.success(200);

    }
}





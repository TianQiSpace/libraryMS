package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.constant.GlobalConstant;
import com.tianqi.libraryms.entity.AdminUser;
import com.tianqi.libraryms.mapper.AdminUserMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.AdminUserService;
import com.tianqi.libraryms.util.CharUtil;
import com.tianqi.libraryms.util.GlobalMessageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tianqi.libraryms.constant.GlobalConstant.*;

/**
* @author GYY
* @description 针对表【admin_user(用户表，存储系统中的用户信息)】的数据库操作Service实现
*/
@Service
@Slf4j
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService{
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private CharUtil charUtil;
    @Override
    public Result<Integer> adminLogin(String username, String password, HttpServletRequest request) {
        Result<String> result=charUtil.loginInfoCheck(username, password);
        if(result.getCode()==-1){
            return Result.error(result.getCode(), result.getMsg());
        }
        // 4. 进行密码的加密
        String safePassword = DigestUtils.md5Hex(GlobalConstant.SALT + password);

        // 5. 根据用户名和密码查询用户信息
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        final AdminUser adminUser = adminUserMapper.selectOne(queryWrapper);

        if (adminUser == null) {
            return Result.error(-4, "账户不存在！");
        }
        if (!adminUser.getPassword().equals(safePassword)) {
            return Result.error(-5, "账户密码错误！");
        }

        // 6. 校验用户角色，确保是管理员或超级管理员
        if (!adminUser.getRole().equals(ADMIN_USER_ROLE)) {
            return Result.error(-5, "您不具有管理员身份！");
        }
        //7.判断账号是否处于封禁状态
        if (adminUser.getStatus().equals(1)) {
            return Result.error(-6, "您的账号已封禁，请联系管理员！");
        }
        // 8. 对用户信息进行脱敏处理。
        final AdminUser safetyAdmin = GlobalMessageUtil.getSafetyAdmin(adminUser);

        // 9. 记录用户的登录态。
        if (adminUser.getRole().equals(ADMIN_USER_ROLE)) {
            request.getSession().setAttribute(ADMIN_USER_LOGIN_STATUS, safetyAdmin);
        }
        // 9. 登录成功，返回用户ID
        return Result.success(adminUser.getId());
    }

    @Override
    public Result<Integer> adminLogout(HttpServletRequest request) {
        // 1. 移除会话中的管理员登录状态信息
        request.getSession().removeAttribute(ADMIN_USER_LOGIN_STATUS);
        // 2. 登出成功，返回成功信息
        return  Result.success(200);
    }

    @Override
    public Result<String> addAdminUser(AdminUser adminUser, HttpServletRequest request) {
        //1.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "未取得此操作权限！");
        }
        //2.进行数据的校验操作：对于管理员的信息需要一次性进行完全录入，并进行校验之后才进行下一步操作。
        if(StringUtils.isAnyBlank(adminUser.getUsername(),adminUser.getPassword(),adminUser.getEmail(),adminUser.getGender())){
            System.out.println(adminUser.getUsername()+adminUser.getPassword()+adminUser.getEmail()+adminUser.getGender());
            return Result.error(-1,"基础信息填写不完整！");
        }
        final Result<String> result = charUtil.registerInfoCheck(adminUser.getUsername(), adminUser.getPassword(), adminUser.getEmail());
        if(result.getCode()==-1){
            return result;
        }
        if(!Objects.equals(adminUser.getGender(), "男") && !Objects.equals(adminUser.getGender(), "女")){
            return Result.error(-1,"非法性别参数！只能为男或女!");
        }
        if(adminUser.getAge()!=null && (adminUser.getAge()<0 || adminUser.getAge()>120)){
            return Result.error(-1,"非法年龄范围！");
        }
        //3.判断账号是否已存在
        final AdminUser adminUserByUsername = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", adminUser.getUsername()));
        if(adminUserByUsername!=null){
            return Result.error(-1,"该账号已存在！");
        }
        //4.进行密码的加密
        String safePassword = DigestUtils.md5Hex(GlobalConstant.SALT + adminUser.getPassword());
        adminUser.setPassword(safePassword);
        adminUser.setRole(2);
        adminUser.setStatus(0);
        final int insert = adminUserMapper.insert(adminUser);
        if(insert!=1){
            return Result.error(-1,"添加失败！");
        }
        return Result.success("添加成功！");
    }

    @Override
    public Result<String> deleteAdminUser(Integer id, HttpServletRequest request) {
        //1.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "未取得此操作权限！");
        }
        //2.执行删除操作
        final int i = adminUserMapper.delete(new QueryWrapper<AdminUser>().eq("id", id));
        if(i!=1){
            return Result.error(-1,"删除失败");
        }
        return Result.success("删除成功！");
    }

    @Override
    public Result<String> updateAdminUser(AdminUser adminUser, HttpServletRequest request) {

        //1.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "未取得此操作权限！");
        }
        //2.进行数据校验：获取封装对象中不为空的值，进行判断合法性
        //2.1将所有数据封装到map中，方便后续处理。
        final Map<String, Object> map = getStringObjectMap(adminUser);
        //2.2对数据进行校验
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if("password".equals(key)){
                if (value.toString().length()!=8) {
                    return Result.error(-1, "密码不满足条件");
                }
            }
            if("gender".equals(key)){
                if(!Objects.equals(value.toString(), "男") && !Objects.equals(value.toString(), "女")){
                    return Result.error(-1,"非法性别参数！只能为男或女!");
                }
            }
            if("age".equals(key)){
                if(adminUser.getAge()!=null && (adminUser.getAge()<0 || adminUser.getAge()>120)){
                    return Result.error(-1,"非法年龄范围！");
                }
            }
            if("email".equals(key)){
                if (!value.toString().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    return Result.error(-1, "邮箱格式不正确");
                }
            }
        }
        //3.执行修改操作
        final int i = adminUserMapper.update(adminUser, new QueryWrapper<AdminUser>().eq("id", adminUser.getId()));
        if(i!=1){
            return Result.error(-1,"修改失败！");
        }
        return Result.success("修改成功！");
    }

    @Override
    public Result<List<AdminUser>> getAllAdminUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute != null) {
            List<AdminUser> list = adminUserMapper.selectList(null);
            return Result.success(list);
        }
        return Result.error(-1, "您没有权限执行此操作！");
    }

    @Override
    public Result<AdminUser> getAdminUserById(Integer id, HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute != null) {
            AdminUser adminUser = adminUserMapper.selectById(id);
            return Result.success(adminUser);
        }
        return Result.error(-1, "您没有权限执行此操作！");
    }

    private static Map<String, Object> getStringObjectMap(AdminUser adminUser) {
        Map<String,Object> map = new HashMap<>();

        if(adminUser.getPassword()!=null){
            map.put("password", adminUser.getPassword());
        }
        if(adminUser.getEmail()!=null){
            map.put("email", adminUser.getEmail());
        }
        if(adminUser.getGender()!=null){
            map.put("gender", adminUser.getGender());
        }
        if(adminUser.getAge()!=null){
            map.put("age", adminUser.getAge());
        }
        return map;
    }


}





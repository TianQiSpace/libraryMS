package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.JavaBean.PasswordVO;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author GYY
* @description 针对表【normal_user(用户表，存储系统中的用户信息)】的数据库操作Service
*/
public interface NormalUserService extends IService<NormalUser> {
    /**
     * @param normalUser   普通用户封装信息
     * @return Integer类型的用户id或者是错误状态码。
     */
    //1.普通用户注册【完全体】
    Result<Integer> normalRegister(NormalUser normalUser);
    //2.普通用户登录【完全体】
    Result<Integer> normalLogin(String username, String password, HttpServletRequest request);
    //3.id查询单个普通用户信息。【完全体】
    Result<NormalUser> getNormalUserInfo(Integer id, HttpServletRequest request);
    //4.修改单个普通用户信息。【完全体】
    Result<NormalUser> updateNormalUserInfo(NormalUser normalUser, HttpServletRequest request);
    //5.普通用户退出登录;【完全体】
    Result<String> normalLogout(HttpServletRequest request);
    //6.注册时检验是否具有某一用户：根据用户名【完全体】
    Result<Boolean> registerCheckUserName(String username);
    //7.关键词获取用户列表信息。【完全体】
    Result<List<NormalUser>> getNormalUserInfo(String keyword, HttpServletRequest request);
    //8.搜索全部用户【完全体】
    Result<List<NormalUser>> getAllNormalUserInfo(HttpServletRequest request);
    //9.名称查询单个用户信息。【完全体】
    Result<NormalUser> getNormalUserInfoByName(String username, HttpServletRequest request);
    //10.根据id,激活用户。
    Result<Integer> activeById(Integer id, HttpServletRequest request);
    //11.修改密码。【完全体】
    Result<String> changePassword(PasswordVO data, HttpServletRequest request);

}

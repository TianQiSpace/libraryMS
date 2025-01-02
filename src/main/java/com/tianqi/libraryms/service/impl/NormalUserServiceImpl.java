package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.JavaBean.PasswordVO;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.mapper.NormalUserMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.NormalUserService;
import com.tianqi.libraryms.util.CharUtil;
import com.tianqi.libraryms.util.GlobalMessageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.tianqi.libraryms.constant.GlobalConstant.*;

/**
 * @author GYY
 * @description 针对表【normal_user(用户表，存储系统中的用户信息)】的数据库操作Service实现
 */
@Service
@Slf4j
public class NormalUserServiceImpl extends ServiceImpl<NormalUserMapper, NormalUser>
        implements NormalUserService {
    @Resource
    private NormalUserMapper normalUserMapper;
    @Resource
    private CharUtil charUtil;

    @Override
    public Result<Integer> normalRegister(NormalUser normalUser) {
        //TODO:这里将来要实现配置文件读取
        normalUser.setRole(1);
        normalUser.setBorrowQuota(10);
        normalUser.setCreditPoints(100);
        normalUser.setBorrowNumber(0);
        normalUser.setStatus(0);

        // 判断传入的参数是否有判断必要.
        if (normalUser == null) {
            return Result.error(-1, "参数全空");
        }

        // 首先进行数据校验：判断给出的对象中的基础数据是否合法。
        String username = normalUser.getUsername();
        String password = normalUser.getPassword();

        Result<String> result = charUtil.loginInfoCheck(username, password);
        if (result.getCode() == -1) {
            return Result.error(result.getCode(), result.getMsg());
        }
        // 4. 判断用户名是否已经存在。
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        if (this.getOne(queryWrapper) != null) {
            return Result.error(-1, "此账户已存在");
        }

        // 5. 将数据插入到数据库中。
        // 5.1 进行密码的Md5加密,并进行封装
        String safePassword = DigestUtils.md5Hex(SALT + password);
        //对密码进行更新加密
        normalUser.setPassword(safePassword);
        //设置用户的权限.
        normalUser.setRole(1);

        // 5.2 向数据库中插入数据。
        boolean save = this.save(normalUser);
        if (!save) {
            return Result.error(-1, "程序异常！注册失败！");
        }
        // 6. 返回成功插入后的自增的用户id
        return Result.success(normalUser.getId());
    }

    /**
     * @description 适用对象为所有类型用户.
     */
    @Override
    public Result<Integer> normalLogin(String username, String password, HttpServletRequest request) {

        //1. 首先进行数据校验。
        //TODO:对数据集校验进行更新
        Result<String> result = charUtil.loginInfoCheck(username, password);
        if (result.getCode() == -1) {
            return Result.error(result.getCode(), result.getMsg());
        }
        // 4. 进行密码的加密；
        String safePassword = DigestUtils.md5Hex(SALT + password);

        // 5. 根据用户名和密码查询用户信息；
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        //和数据库中进行比对
        NormalUser normalUser = normalUserMapper.selectOne(queryWrapper);

        if (normalUser == null) {
            return Result.error(-1, "账户不存在或已被封禁！");
        } else {
            //再进行密码比对
            queryWrapper.eq("password", safePassword);
            NormalUser normalUser1 = normalUserMapper.selectOne(queryWrapper);
            if (normalUser1 == null) {
                return Result.error(-1, "账户密码错误！请重试！");
            }
        }
        // 6. 对用户信息进行脱敏处理：密码不返回。
        NormalUser safetyUser = GlobalMessageUtil.getSafetyUser(normalUser);
        // 7. 记录用户的登录态。
        request.getSession().setAttribute(NORMAL_USER_LOGIN_STATUS, safetyUser);
        // 8. 登录成功，返回用户ID
        return Result.success(200);
    }

    /**
     * 重写获取普通用户信息的方法
     * @param id      用户ID
     * @param request HTTP请求对象，用于获取session信息
     * @return 返回包含普通用户信息的结果对象
     */
    @Override
    public Result<NormalUser> getNormalUserInfo(Integer id, HttpServletRequest request) {
        // 1. 判断用户id是否为空
        if (id == null) {
            return Result.error(-1, "传入参数为空");
        }

        // 3. 根据id查询用户信息
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        // 4. 处理并且返回用户信息：从数据库中查询出来的用户信息的密码经过了加密
        NormalUser normalUser = normalUserMapper.selectOne(queryWrapper);
        if (normalUser == null) {
            return Result.error(-1, "该条件未查到任何信息,为后端数据错误");
        }
        // 5. 查询成功，返回用户信息
        return Result.success(GlobalMessageUtil.getSafetyUser(normalUser));
    }

    /**
     * @param normalUser 要进行数据修改的用户信息封装
     * @param request    用于获取用户登录状态
     * @return 实时返回修改后的用户信息。
     */
    @Override
    public Result<NormalUser> updateNormalUserInfo(NormalUser normalUser, HttpServletRequest request) {

        // 2. 判断传递进来的这个用户的封装信息是否为空。
        if (normalUser == null) {
            return Result.error(-1, "用户封装信息为空！");
        }
        // 3. 得到要修改的数据。
        NormalUser newNormalUser = new NormalUser();
        // 设置更改后的用户ID:就是将ID复制一份.这是必须要有的不会发生变化.
        newNormalUser.setId(normalUser.getId());

        // 4. 校验并设置用户名:如果填写了再进行校验.
        String username = normalUser.getUsername();
        if (username != null) {
            if (username.length() >= 4 && username.length() <= 20) {
                String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
                if (username.matches(regex)) {
                    newNormalUser.setUsername(username);
                } else {
                    return Result.error(-1, "新修改的用户名包含非法字符");
                }
            } else {
                return Result.error(-1, "新修改的用户名长度不符合要求");
            }
        }
        // 6. 校验并设置邮箱,如果填写了,才进行校验,不然就不管
        String email = normalUser.getEmail();
        if (email != null) {
            if (email.length() >= 5 && email.length() <= 25) {
                newNormalUser.setEmail(email);
            } else {
                return Result.error(-1, "邮箱长度不符合条件！");
            }
        }

        // 7. 校验并设置性别
        String gender = normalUser.getGender();
        if (gender != null) {
            if ("男".equals(gender) || "女".equals(gender)) {
                newNormalUser.setGender(gender);
            } else {
                return Result.error(-1, "非法性别参数！只能为男或女!");
            }
        }

        // 8. 校验并设置年龄
        Integer age = normalUser.getAge();
        if (age != null) {
            if (age >= 0 && age <= 120) {
                newNormalUser.setAge(age);
            } else {
                return Result.error(-1, "非法年龄参数！");
            }
        }

        // 9. 更新时间
        newNormalUser.setUpdatedTime(new Date());

        // 10. 调用服务更新用户信息
        boolean updateSuccess = normalUserMapper.updateById(newNormalUser) > 0;
        if (!updateSuccess) {
            return Result.error(-1, "更新失败！");
        }
        // 11. 更新成功，返回用户信息
        newNormalUser = normalUserMapper.selectById(normalUser.getId());
        //进行信息脱敏
        newNormalUser = GlobalMessageUtil.getSafetyUser(newNormalUser);
        //如果使用这个方法的是普通用户,还要将其登录状态内的数据也进行修改.
        if (request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS) != null) {
            request.getSession().setAttribute(NORMAL_USER_LOGIN_STATUS, newNormalUser);
        }

        return Result.success(newNormalUser);
    }

    /**
     * @param request
     * @return
     * @description 适用对象为普通用户
     */
    @Override
    @SuppressWarnings("all")
    public Result<String> normalLogout(HttpServletRequest request) {
        // 创建一个默认的结果对象，用于返回
        Result<String> logoutResult;
        // 1. 检查当前会话中是否存在普通用户登录状态.
        Object normalUserLoginStatus = request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);

        // 2. 如果会话中不存在普通用户登录状态，则表示未登录或已登出
        if (normalUserLoginStatus == null) {
            logoutResult = Result.error(-1, "您尚未登录，无需登出！");
            return logoutResult;
        }

        // 3. 移除会话中的普通用户登录状态信息
        request.getSession().removeAttribute(NORMAL_USER_LOGIN_STATUS);

        // 4. 登出成功，返回成功信息
        logoutResult = Result.success("退出登录!");

        return logoutResult;
    }

    @Override
    public Result<Boolean> registerCheckUserName(String username) {
        System.out.println("---------------------------------------------------------------");
        Result<Boolean> result = new Result<>();
       QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("username",username);
        final NormalUser normalUser = normalUserMapper.selectOne(queryWrapper);
        if(normalUser!=null){
            result.setSuccess(false);
            System.out.println("---------------------------------------------------------------");
            return Result.error(-1, "用户名已存在");
        }
        result.setSuccess(true);
        return Result.success(true);

    }

    @Override
    public Result<List<NormalUser>> getNormalUserInfo(String keyword, HttpServletRequest request) {

        // 1. 检查当前会话中是否存在管理员登录状态
        Object adminUserLoginStatus = request.getSession().getAttribute(ADMIN_USER_LOGIN_STATUS);

        // 2. 如果会话中不存在管理员登录状态，则表示未登录或权限不足
        if (adminUserLoginStatus == null) {
            return Result.error(-1, "您尚未登录或没有权限执行此操作！");
        }

        // 4. 构建查询条件:这里允许用户根据用户名、ID或邮箱进行模糊查询
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", keyword)
                .or()
                .like("id", keyword)
                .or()
                .like("email", keyword);

        // 5. 查询符合条件的普通用户
        List<NormalUser> normalUsers = normalUserMapper.selectList(queryWrapper);
        // 6. 返回查询结果
        return Result.success(normalUsers);
    }

    @Override
    public Result<List<NormalUser>> getAllNormalUserInfo(HttpServletRequest request) {
        return Result.success(normalUserMapper.selectList(null));
    }

    @Override
    public Result<NormalUser> getNormalUserInfoByName(String username, HttpServletRequest request) {
        //1.判断数据合法性：不支持模糊查询
        if (username == null || StringUtils.isBlank(username)) {
            return Result.error(-1, "用户名不能为空！");
        }
        //2.进行查询
        NormalUser normalUser = normalUserMapper.selectOne(new QueryWrapper<NormalUser>().eq("username", username));
        if (normalUser == null) {
            return Result.error(-1, "用户不存在！");
        }
        return Result.success(normalUser);
    }

    @Override
    public Result<Integer> activeById(Integer id, HttpServletRequest request) {
      //删除用户
        final NormalUser newNormalUser = normalUserMapper.selectById(id);
        return Result.success(newNormalUser.getId());
    }

    @Override
    public Result<String> changePassword(PasswordVO data, HttpServletRequest request) {
        //1.获取当前用户的session数据。
        Object user = request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        if (user == null) {
            return Result.error(-1, "您尚未登录，无法修改密码！");
        }
        //2.判断旧密码是否正确
        NormalUser normalUser = (NormalUser) user;
        String oldPassword = data.getOldPassword();
        String newPassword = data.getNewPassword();
        //对旧密码进行MD5加密：
        String s = DigestUtils.md5Hex(SALT + oldPassword);
        //判断和数据库中的密码是否一致
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", normalUser.getId());
        final NormalUser one = normalUserMapper.selectOne(queryWrapper);
        if(!one.getPassword().equals(s)){
            //说明密码不正确
            return Result.error(-1, "原始密码错误！");
        }
        //3.判断新密码是否为空
        if (newPassword == null || StringUtils.isBlank(newPassword)) {
            return Result.error(-1, "新密码不能为空！");
        }
        //4.判断新密码是否合法
        if (newPassword.length() !=8) {
            return Result.error(-1, "新密码长度必须在为8位！");
        }
        //5.对新密码进行加密
        newPassword = DigestUtils.md5Hex(SALT + newPassword);
        //6.修改密码
        one.setPassword(newPassword);
        normalUserMapper.updateById(one);
        //7.返回成功信息
        return Result.success("密码修改成功！");
    }

}






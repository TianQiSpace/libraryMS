package com.tianqi.libraryms.util;

import com.tianqi.libraryms.result.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author GYY
 *
 */

@Component
public class CharUtil {
    @Resource
    private accountCheck accountCheck;
    /**
     * 生成6位随机数字验证码
     * 该方法通过创建一个随机数生成器，依次生成6个随机数字并拼接成字符串作为验证码
     * 验证码通常用于验证用户身份，这里生成的验证码可以用于各种需要临时验证的场景
     * @return 返回一个6位数字的随机验证码字符串
     */
    public static String randomVerify() {
        // 创建随机数生成器实例
        Random random = new Random();
        // 初始化验证码字符串
        StringBuilder result = new StringBuilder();
        // 循环6次，每次生成一个随机数字并将其添加到结果字符串中
        for (int i = 0; i < 6; i++) {
            // 生成0-9之间的随机数字，并将其转换为字符后添加到结果字符串中
            result.append(random.nextInt(10));
        }
        // 返回生成的随机验证码字符串
        return result.toString();
    }

    /**
     * @description 用于集中校验登录信息
     * @param username 用户名
     * @param password 密码
     * @return 标准结果封装
     */
    public Result<String> loginInfoCheck(String username, String password) {
        return accountCheck.baseCheck(username, password, "不校验");
    }
    public Result<String> registerInfoCheck(String username, String password, String email) {
       return accountCheck.baseCheck(username, password, email);
    }
}
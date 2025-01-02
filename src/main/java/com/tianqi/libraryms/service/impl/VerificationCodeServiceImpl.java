package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.VerificationCode;
import com.tianqi.libraryms.mapper.VerificationCodeMapper;
import com.tianqi.libraryms.service.VerificationCodeService;
import com.tianqi.libraryms.util.CharUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author GYY
 * @description 针对表【verification_code】的数据库操作Service实现
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode>
        implements VerificationCodeService {
    @Resource
    private VerificationCodeMapper verificationCodeMapper;

    /**
     * 生成验证码并保存到数据库
     *
     * @param email 用户的电子邮件地址，用于关联验证码记录
     * @return 生成的验证码字符串
     */
    @Override
    public String generateVerificationCode(String email) {
        // 生成6位随机验证码
        String code = CharUtil.randomVerify();
        // 设置过期时间为10分钟后
        Date expiresTime = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        // 保存到数据库
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setCreatedTime(new Date());
        verificationCode.setExpiresTime(expiresTime);
        verificationCodeMapper.insert(verificationCode);
        return code;
    }

    /**
     * 验证邮箱和验证码是否匹配
     *
     * @param email 用户的邮箱地址
     * @param code  用户输入的验证码
     * @return 如果邮箱和验证码匹配，则返回true；否则返回false
     */
    @Override
    public boolean verifyCode(String email, String code) {
        // 查询未过期的验证码
        VerificationCode verificationCode = verificationCodeMapper.selectOne(
                new QueryWrapper<VerificationCode>()
                        .eq("email", email)
                        .gt("expires_time", new Date())
        );
        if (verificationCode != null && verificationCode.getCode().equals(code)) {
            log.info("验证码验证成功！");
            // 验证成功后删除验证码记录
            verificationCodeMapper.deleteById(verificationCode.getId());
            return true;
        }
        return false;
    }
}





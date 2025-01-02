package com.tianqi.libraryms.service;

import com.tianqi.libraryms.entity.VerificationCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 11357
 * @description 针对表【verification_code】的数据库操作Service
 */
public interface VerificationCodeService extends IService<VerificationCode> {
    String generateVerificationCode(String email);

    boolean verifyCode(String email, String code);
}

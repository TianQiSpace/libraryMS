package com.tianqi.libraryms.controller.UtilController;

import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GYY
 * @description 邮箱验证
 */
@Slf4j
@RestController
@RequestMapping("/mail")
public class EmailController {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/send")
    public Result<String> sendVerificationCode(@RequestBody VerificationCodeRequest request) {
        String email = request.getEmail();
        // 生成验证码
        String code = verificationCodeService.generateVerificationCode(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("智能图书管理系统-注册验证");
        message.setText("<h1>尊敬的用户您好：</h1><br>" +
                "<h5> 您正在进行邮箱验证，本次验证码为：<span style='color:#ec0808;font-size: 20px;'>" + code + "</span>，请在10分钟内进行使用。</h5>" +
                "<h5>如非本人操作，请忽略此邮件，由此给您带来的不便请谅解！</h5> <h5 style='text-align: right;'>--智能图书管理平台</h5>");
        try {
            mailSender.send(message);
            return Result.success("验证码发送成功");
        } catch (Exception e) {
            return Result.error(500, "验证码系统发送失败，请检查邮箱账号是否正确！");
        }
    }


    @PostMapping("/verify")
    public Result<String> verifyVerificationCode(@RequestBody VerificationCodeRequest request) {
        boolean isValid = verificationCodeService.verifyCode(request.getEmail(), request.getVerificationCode());
        if (isValid) {
            log.info("验证码验证成功！");
            return Result.success("验证码验证成功");
        } else {
            log.info("验证码验证失败！");
            return Result.error(400, "验证码验证失败");
        }
    }


    // 辅助类
    @Setter
    @Getter
    public static class VerificationCodeRequest {
        private String email;
        private String verificationCode;

    }
}
package com.tianqi.libraryms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author GYY
 * @description 验证码表，用于存储用户邮箱和验证码信息
 * @TableName verification_code
 */
@TableName(value ="verification_code")
@Data
public class VerificationCode implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户邮箱地址
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码创建时间
     */
    private Date createdTime;

    /**
     * 验证码过期时间
     */
    private Date expiresTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
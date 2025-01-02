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
 * 用户表，存储系统中的用户信息
 * @author GYY
 * @TableName admin_user
 */
@TableName(value ="admin_user")
@Data
public class AdminUser implements Serializable {
    /**
     * 管理员用户唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员电子邮件
     */
    private String email;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户角色，1: 普通用户, 2: 管理员, 3: 超级管理员
     */
    private Integer role;

    /**
     * 用户创建时间
     */
    private Date createdTime;

    /**
     * 用户信息最后更新时间
     */
    private Date updatedTime;

    /**
     * 账户是否能够正常使用，针对管理员用户
     */
    private Integer status;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
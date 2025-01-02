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
 * @TableName super_admin_user
 */
@TableName(value ="super_admin_user")
@Data
public class SuperAdminUser implements Serializable {
    /**
     * 超管用户唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 超管用户名
     */
    private String username;

    /**
     * 超管密码
     */
    private String password;

    /**
     * 超管电子邮件
     */
    private String email;

    /**
     * 超管性别
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
     * 超管用户创建时间
     */
    private Date createdTime;

    /**
     * 超管用户信息最后更新时间
     */
    private Date updatedTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
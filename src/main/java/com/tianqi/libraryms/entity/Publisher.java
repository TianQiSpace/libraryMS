package com.tianqi.libraryms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 出版社表
 * @author GYY
 * @TableName publisher
 */
@TableName(value ="publisher")
@Data
public class Publisher implements Serializable {
    /**
     * 出版社ID
     */
    @TableId(type = IdType.AUTO)
    private Integer publisherId;

    /**
     * 出版社名称
     */
    private String publisherName;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 官方网站
     */
    private String website;

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
}
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
 * 存储系统中的所有普通用户的通知记录
 * @author GYY
 * @TableName notifications
 */
@TableName(value ="notifications")
@Data
public class Notifications implements Serializable {
    /**
     * 主键，唯一标识每条通知
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 消息要发送到的用户标识
     */
    private Integer userId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息创建的时间
     */
    private Date createDate;

    /**
     * 消息状态（未读/已读）
     */
    private String messageStatus;

    /**
     * 消息类型（系统通知/个人消息）
     */
    private String type;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
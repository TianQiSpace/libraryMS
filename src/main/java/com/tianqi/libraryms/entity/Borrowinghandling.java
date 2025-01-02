package com.tianqi.libraryms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author GYY
 * @TableName borrowinghandling
 */
@TableName(value ="borrowinghandling")
@Data
public class Borrowinghandling implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer bookId;

    /**
     * 
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
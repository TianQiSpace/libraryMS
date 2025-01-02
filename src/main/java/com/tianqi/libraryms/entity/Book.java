package com.tianqi.libraryms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 图书表
 * @author GYY
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book implements Serializable {
    /**
     * 图书ID
     */
    @TableId(type = IdType.AUTO)
    private Integer bookId;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * ISBN号
     */
    private String isbn;

    /**
     * 出版日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    /**
     * 出版社ID
     */
    private Integer publisherId;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 简介
     */
    private String summary;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 书籍数量
     */
    private Integer quantity;
    private Integer malePoint;
    private Integer femalePoint;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
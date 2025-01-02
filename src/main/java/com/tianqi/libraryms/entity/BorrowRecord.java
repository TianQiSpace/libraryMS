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
     * 借阅记录表 - 增加了借阅状态文本描述
     * @author GYY
     * @TableName borrow_record
     */
    @TableName(value ="borrow_record")
    @Data
    public  class BorrowRecord implements Serializable {
        /**
         * 借阅记录ID
         */
        @TableId(type = IdType.AUTO)
        private Integer recordId;

        /**
         * 用户ID
         */
        private Integer userId;

        /**
         * 图书ID
         */
        private Integer bookId;

        /**
         * 借阅书籍的名称
         */
        private String bookTitle;

        /**
         * 图书类别ID
         */
        private Integer categoryId;

        /**
         * 借阅日期
         */
        private Date borrowDate;

        /**
         * 归还日期
         */
        private Date returnDate;

        /**
         * 应还日期
         */
        private Date dueDate;

        /**
         * 状态：判断这条记录是否有效
         */
        private Integer borrowStatus;

        /**
         *
         */
        private Object borrowStatusText;

        /**
         * 创建时间
         */
        private Date createdAt;

        /**
         * 更新时间
         */
        private Date updatedAt;

        @Serial
        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }

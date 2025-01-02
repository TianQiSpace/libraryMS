package com.tianqi.libraryms.entity.JavaBean;

import lombok.Data;

/**
 * @author GYY
 * @date 2024/11/19 18:34
 * @description 用来将需要处理的信息进行补充
 */
@Data
public class BorrowinghandlingVO {
    private Integer id;
    /**
     * 操作类型：1：借阅，2：归还
     */
    private String type;

    private Integer userId;
    private String userName;

    private Integer bookId;
    private String title;

}

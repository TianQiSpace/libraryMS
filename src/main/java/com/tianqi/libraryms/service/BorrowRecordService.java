package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.BorrowRecord;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 11357
* @description 针对表【borrow_record(借阅记录表)】的数据库操作Service
*/
public interface BorrowRecordService extends IService<BorrowRecord> {
    //1.获取某个用户借阅记录列表；
    Result<List<BorrowRecord>> getBorrowRecordList(Integer userId, HttpServletRequest request);
    //2.图书借阅；
    Result<String> borrowBook(Integer bookId,Integer userId, HttpServletRequest request);
    //3.图书归还；
    Result<String> returnBookForUser(Integer bookId, Integer userId, HttpServletRequest request);
    void returnBookForAdminUser(Integer id);

    //4.获取全部借阅记录列表；
    Result<List<BorrowRecord>> getAllBorrowRecordList(HttpServletRequest request);
}

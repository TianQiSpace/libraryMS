package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.Borrowinghandling;
import com.tianqi.libraryms.entity.JavaBean.BorrowinghandlingVO;
import com.tianqi.libraryms.result.Result;

import java.util.List;

/**
* @author GYY
* @description 针对表【borrowinghandling】的数据库操作Service
* @createDate 2024-11-19 15:46:36
*/
public interface BorrowinghandlingService extends IService<Borrowinghandling> {
    Result<List<BorrowinghandlingVO>> getAllBorrowRecordList();

}

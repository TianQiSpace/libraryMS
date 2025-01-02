package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Borrowinghandling;
import com.tianqi.libraryms.entity.JavaBean.BorrowinghandlingVO;
import com.tianqi.libraryms.mapper.BorrowinghandlingMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BorrowinghandlingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author GYY
* @description 针对表【borrowinghandling】的数据库操作Service实现
* @createDate 2024-11-19 15:46:36
*/
@Service
public class BorrowinghandlingServiceImpl extends ServiceImpl<BorrowinghandlingMapper, Borrowinghandling>
    implements BorrowinghandlingService{
    @Resource
    private BorrowinghandlingMapper borrowinghandlingMapper;


    @Override
    public Result<List<BorrowinghandlingVO>> getAllBorrowRecordList() {
        //1.查询所有的数据
        final List<Borrowinghandling> borrowinghandlings = borrowinghandlingMapper.selectList(null);
        //2.将数据进行补充完成VO对象的初始化
        //进行多表的关联查询
        ArrayList<BorrowinghandlingVO> borrowinghandlingVOS = new ArrayList<>();
        for (Borrowinghandling borrowinghandling : borrowinghandlings) {
            BorrowinghandlingVO borrowinghandlingVO = borrowinghandlingMapper.selectAllBorrowRecord(borrowinghandling.getUserId(), borrowinghandling.getBookId());
            borrowinghandlingVOS.add(borrowinghandlingVO);
        }
        return Result.success(borrowinghandlingVOS);
    }
}





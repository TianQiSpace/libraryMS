package com.tianqi.libraryms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianqi.libraryms.entity.Borrowinghandling;
import com.tianqi.libraryms.entity.JavaBean.BorrowinghandlingVO;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【borrowinghandling】的数据库操作Mapper
* @createDate 2024-11-19 15:46:36
* @Entity com.tianqi.libraryms.entity.Borrowinghandling
*/
@Mapper
public interface BorrowinghandlingMapper extends BaseMapper<Borrowinghandling> {
   BorrowinghandlingVO selectAllBorrowRecord(Integer userId , Integer bookId);

}





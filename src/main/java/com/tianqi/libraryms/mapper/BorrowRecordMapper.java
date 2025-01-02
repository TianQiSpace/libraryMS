package com.tianqi.libraryms.mapper;

import com.tianqi.libraryms.entity.BorrowRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【borrow_record(借阅记录表)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.BorrowRecord
*/
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

}





package com.tianqi.libraryms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianqi.libraryms.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【book(图书表)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.Book
*/
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}





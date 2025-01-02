package com.tianqi.libraryms.mapper;

import com.tianqi.libraryms.entity.BookCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【book_category(图书分类表)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.BookCategory
*/
@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {

}





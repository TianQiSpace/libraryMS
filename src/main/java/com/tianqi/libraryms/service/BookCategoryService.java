package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author GYY
* @description 针对表【book_category(图书分类表)】的数据库操作Service
*/
public interface BookCategoryService extends IService<BookCategory> {
    //添加书籍类别【完全体】
    Result<BookCategory> addBookCategory(BookCategory bookCategory, HttpServletRequest request);
    //删除书籍类别【完全体】
    Result<String> deleteBookCategory(Integer categoryId, HttpServletRequest request);
    //根据名称查询单个书籍类别【完全体】
    Result<BookCategory> getBookCategoryByName(String categoryName, HttpServletRequest request);
    //根据ID查询单个书籍类别【完全体】
    Result<BookCategory> getBookCategoryById(Integer categoryId, HttpServletRequest request);
    //获取所有书籍类别【完全体】
    Result<List<BookCategory>> getAllBookCategory(HttpServletRequest request);
    //更新书籍类别
    Result<String> updateBookCategory(BookCategory bookCategory, HttpServletRequest request);
}

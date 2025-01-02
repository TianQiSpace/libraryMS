package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 11357
* @description 针对表【book(图书表)】的数据库操作Service
*/
public interface BookService extends IService<Book> {
    /**
     * @description 所有用户都可以搜索书籍
     * @param keyword 搜索关键字
     * @param request http请求
     * @return 搜索结果
     */
    //1.按照书名或者作者名搜索书籍【完全体】
    Result<List<Book>> searchBookByNameOrAuthor(String keyword, HttpServletRequest request);
    //2.根据出版社ID查询书籍【完全体】
    Result<List<Book>> searchBookByPublisher(Integer publisherId, HttpServletRequest request);
    /**
     * @description 按照图书分类名称搜索书籍
     * @description 所有用户
     * @param categoryId 书籍分类ID
     * @param request http请求
     * @return 书籍列表
     */
    //3.按照图书分类id搜索书籍【完全体】
    Result<List<Book>> searchBookByCategoryId(Integer categoryId, HttpServletRequest request);

    /**
     * @description 超级管理员和管理员可以添加书籍
     * @param book 书籍信息封装
     * @param request http请求
     * @return 添加结果
     */
    //4.添加书籍
    Result<String> addBook(Book book,HttpServletRequest request);
    //5.编辑书籍
    Result<String> editAndUpdateBook(Book book,HttpServletRequest request);
    //5.删除书籍
    Result<String> deleteBook(Integer id, HttpServletRequest request);
    //6.查看单本书籍
    Result<Book> getBookById(Integer bookId, HttpServletRequest request);
    //7.直接获取到所有书籍
    Result<List<Book>> getAllBooks(HttpServletRequest request);
    //8.书籍积分积累
   void addBookPointBecauseSearch(Integer bookId,Integer userId, HttpServletRequest request);
}

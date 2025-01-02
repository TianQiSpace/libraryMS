package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.mapper.BookCategoryMapper;
import com.tianqi.libraryms.mapper.BookMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tianqi.libraryms.constant.GlobalConstant.SUPER_ADMIN_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 针对表【book_category(图书分类表)】的数据库操作Service实现
 */
@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory>
        implements BookCategoryService {
    @Resource
    private BookCategoryMapper bookCategoryMapper;
    @Resource
    private BookMapper bookMapper;

    /**
     * 添加书籍类别
     * @param bookCategory 书籍类别对象，包含要添加的类别的信息
     * @param request      HTTP请求对象，用于获取会话信息以检查用户登录和权限状态
     * @return 返回添加操作的结果，包含成功或错误信息以及可能的书籍类别对象
     * @description 方法对象:超级管理员可添加书籍类别
     */
    @Override
    public Result<BookCategory> addBookCategory(BookCategory bookCategory, HttpServletRequest request) {

        Object superAdminUserLoginStatus = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        // 1. 确认管理员角色
        if (superAdminUserLoginStatus==null) {
            //说明为普通管理员,或是普通用户没有这个权限.
            return Result.error(-1, "您没有权限执行此操作！");
        }

        // 2. 检查分类名称是否已存在
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", bookCategory.getCategoryName());
        BookCategory existingCategory = bookCategoryMapper.selectOne(queryWrapper);
        if (existingCategory != null) {
            return Result.error(-1, "该分类已存在！请换一个吧!");
        }

        // 3. 数据校验：填写分类名称和分类描述
        if (StringUtils.isAllBlank(bookCategory.getCategoryName(), bookCategory.getDescription())) {
            return Result.error(-1, "分类名称及分类描述不能为空！");
        }

        // 6. 添加新的书籍类别
        bookCategoryMapper.insert(bookCategory);
        // 7. 返回成功结果
        return Result.success(bookCategory);
    }

    /**
     * @param categoryId 要删除的类别的名称
     * @param request      http请求对象，用于获取会话信息以检查用户登录和权限状态
     * @return 返回删除操作的结果，包含成功或错误信息
     * @description 超级管理员
     */
    @Override
    public Result<String> deleteBookCategory(Integer categoryId, HttpServletRequest request) {
        // 1. 检查当前会话中的登录状态
        Object superAdminUserLoginStatus = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (superAdminUserLoginStatus == null) {
            return Result.error(-1, "您尚未登录或没有权限执行此操作！");
        }
        //3.查询是否具有该类别.
        BookCategory bookCategory = bookCategoryMapper.selectOne(new QueryWrapper<BookCategory>().eq("category_id", categoryId));
        if (bookCategory == null) {
            return Result.error(-1, "该分类不存在！");
        }
        //4.查询该类别是否被图书所使用,如果检查不通过就取消
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", bookCategory.getCategoryId());
        final List<Book> books = bookMapper.selectList(queryWrapper);
        if (!books.isEmpty()) {
            return Result.error(-1, "该类别下还有图书，无法删除！");
        }
        //5.删除
        bookCategoryMapper.delete(new QueryWrapper<BookCategory>().eq("category_id", categoryId));
        return Result.success("删除成功！");
    }
    /**
     * @param categoryName 要查询的类别名称
     * @param request      http请求对象，用于获取会话信息以检查用户登录和权限状态
     * @return 返回查询结果，包含书籍类别对象或错误信息
     * @description 所有人
     */
    @Override
    public Result<BookCategory> getBookCategoryByName(String categoryName, HttpServletRequest request) {
        //1.数据校验
        if (StringUtils.isBlank(categoryName)){
            return Result.error(-1, "请输入要查询的类别名称！");
        }
        //2.进行模糊查询该类别
        BookCategory categoryName1 = bookCategoryMapper.selectOne(new QueryWrapper<BookCategory>().like("category_name", categoryName));
/*
        BookCategory categoryName1 = bookCategoryMapper.selectOne(new QueryWrapper<BookCategory>().eq("category_name", categoryName));
*/
        if (categoryName1 == null){
            return Result.error(-1, "该类别不存在！");
        }
        return Result.success(categoryName1);

    }

    @Override
    public Result<BookCategory> getBookCategoryById(Integer categoryId, HttpServletRequest request) {
        if (categoryId<=0){
            return Result.error(-1, "查询的编号类别编号非法！");
        }
        BookCategory bookCategory = bookCategoryMapper.selectById(categoryId);
        if (bookCategory!=null){
            return Result.success(bookCategory);
        }
        return Result.error(-1, "该类别不存在！");
    }

    @Override
    public Result<List<BookCategory>> getAllBookCategory(HttpServletRequest request) {
        List<BookCategory> bookCategoryList = bookCategoryMapper.selectList(null);
        if (bookCategoryList.isEmpty()) {
            return Result.error(-1, "暂无分类信息！");
        }
        return Result.success(bookCategoryList);
    }

    @Override
    public Result<String> updateBookCategory(BookCategory bookCategory, HttpServletRequest request) {
        Object superAdminUserLoginStatus = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        // 1. 确认管理员角色
        if (superAdminUserLoginStatus==null) {
            //说明为普通管理员,或是普通用户没有这个权限.
            return Result.error(-1, "您没有权限执行此操作！");
        }
        //2.数据校验
        if (StringUtils.isBlank(bookCategory.getDescription())) {
            return Result.error(-1, "分类描述不能为空！");
        }
       //3.更新
        bookCategoryMapper.updateById(bookCategory);
        return Result.success("更新成功！");
    }

}





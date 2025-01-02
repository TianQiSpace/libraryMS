package com.tianqi.libraryms.controller.BookCategoryController;

import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author GYY
 * @description 用来控制分类页面的初始化工作
 */
@Controller
public class BookCategoryPageController {
    @Resource
    private BookCategoryService bookCategoryService;

    /**
     * 初始化书籍分类页面
     * 该方法通过发送GET请求到/category/index来初始化书籍分类页面它负责查询所有的书籍分类，
     * 并将这些分类信息添加到Model中，以便在页面上显示
     * @param model 用于存储分类信息的Model对象，Spring MVC会自动实例化并使用它来传递数据到视图
     * @param request HTTP请求对象，用于获取请求相关的信息
     * @return 返回书籍分类页面的路径
     */
    @GetMapping("/category/index")
    public String initCategoryPage(Model model, HttpServletRequest request){
        //1.查询所有的分类。
        Result<List<BookCategory>> allBookCategory = bookCategoryService.getAllBookCategory(request);
        // 将查询到的所有分类数据添加到Model中，以便在页面上使用
        model.addAttribute("allCategory", allBookCategory.getData());
        // 返回书籍分类页面的路径
        return "normalUser/category";
    }

}

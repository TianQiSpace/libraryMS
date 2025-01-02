package com.tianqi.libraryms.controller.BookCategoryController;

import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GYY
 * @description 用来作为书籍类别管理的总控制类
 */
@Controller
@RequestMapping("/bookCategory")
public class BookCategoryController {
    @Resource
    private BookCategoryService bookCategoryService;

    /**
     * @description 超级管理员图书类别管理初始化界面：一旦点击到了这个界面就要进行查询太填充页面。
     * @param model 模型，用来传递数据
     * @param request 请求
     * @return 返回超级管理员图书类别管理界面
     */
    @GetMapping("/superAdmin/indexSearch")
    public String bookCategoryPage(Model model, HttpServletRequest request) {
        Result<List<BookCategory>> result = bookCategoryService.getAllBookCategory(request);
        model.addAttribute("bookCategoryList", result.getData());
        model.addAttribute("error", result.getMsg());
        return "admin/superAdmin/bookCategory";
    }

    /**
     * @description 超级管理员按照名称搜索图书类别
     * @param categoryName 要搜索的书籍类别名称
     * @param model 模型，用来传递数据。
     * @param request 请求。
     * @return 返回书籍类别界面。
     */
    @GetMapping("/superAdmin/searchBookCategoryByName")
    public String searchBookCategoryByName(String categoryName, Model model, HttpServletRequest request) {
        //1.判断数据合法性
        if (StringUtils.isBlank(categoryName)) {
            model.addAttribute("error", "搜索关键词不能为空！");
            return "admin/superAdmin/bookCategory";
        }
        //2.查询
        Result<BookCategory> result = bookCategoryService.getBookCategoryByName(categoryName, request);
        model.addAttribute("bookCategoryList", result.getData());
        model.addAttribute("error", result.getMsg());
        return "admin/superAdmin/bookCategory";
    }

    /**
     * @description 超级管理员添加图书类别界面的页面跳转
     * @return 添加图书类别界面
     */
    @GetMapping("/superAdmin/addBookCategoryPage")
    public String addBookCategoryPage() {
        return "admin/superAdmin/addBookCategory";
    }

    /**
     * @description 超级管理员添加图书类别
     * @param bookCategory 要添加的图书类别封装对象
     * @param model 模型，用来传递数据。
     * @param request 请求信息。
     * @return 返回重定向到图书类别管理界面。
     */
    @PostMapping("/superAdmin/addBookCategory")
    public String addBookCategory(BookCategory bookCategory, Model model, HttpServletRequest request) {
        //1.判断数据合法性
        if (StringUtils.isBlank(bookCategory.getCategoryName())) {
            model.addAttribute("error", "图书类别名称不能为空！");
            return "admin/superAdmin/bookCategory";
        }
        //2.添加图书类别
        Result<BookCategory> result = bookCategoryService.addBookCategory(bookCategory, request);
        model.addAttribute("error", result.getMsg());
        return "redirect:/bookCategory/superAdmin/indexSearch";
    }

    /**
     * @description 超级管理员删除图书类别
     * @param id 要删除的图书类别id
     * @param request 请求对象
     * @param model 模型，用来传递数据。
     * @return 返回重定向到图书类别管理界面。
     */
    @GetMapping("/superAdmin/deleteBookCategoryById/{id}")
    public String deleteBookCategoryById(@PathVariable("id") Integer id,HttpServletRequest request,Model model){
        final Result<String> stringResult = bookCategoryService.deleteBookCategory(id, request);
        model.addAttribute("error", stringResult.getMsg());
        return "redirect:/bookCategory/superAdmin/indexSearch";
    }

    /**
     * @description 更新图书类别界面的页面跳转
     * @param id 要更新的图书类别id
     * @param model 模型，用来传递数据。
     * @param request 请求对象
     * @return 返回更新图书类别界面视图。
     */
    @GetMapping("/superAdmin/updateBookCategoryPage/{id}")
    public String updateBookCategoryPage(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        //1.判断数据合法性
        if (id == null) {
            model.addAttribute("error", "图书类别id不能为空！");
            return "admin/superAdmin/bookCategory";
        }
        final BookCategory bookCategory = bookCategoryService.getBookCategoryById(id, request).getData();
        model.addAttribute("bookCategory", bookCategory);
        return "admin/superAdmin/updateBookCategory";
    }
    @PostMapping("/superAdmin/updateBookCategory")
    public String updateBookCategory(BookCategory bookCategory, Model model, HttpServletRequest request) {
        //1.判断数据合法性
        if (StringUtils.isBlank(bookCategory.getCategoryName())) {
            model.addAttribute("error", "图书类别名称不能为空！");
            return "admin/superAdmin/bookCategory";
        }
        //2.更新图书类别
        Result<String> result = bookCategoryService.updateBookCategory(bookCategory, request);
        model.addAttribute("error", result.getMsg());
        return "redirect:/bookCategory/superAdmin/indexSearch";
    }
    /*注：用在了，普通管理员添加书籍时获取分类信息：bookUpdate页面*/
    @GetMapping("/categories")
    @ResponseBody
    public List<BookCategory> getAllCategories(HttpServletRequest request) {
        final Result<List<BookCategory>> allBookCategory = bookCategoryService.getAllBookCategory(request);
        // 从数据库获取所有分类数据
        return allBookCategory.getData();
    }
}

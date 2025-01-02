package com.tianqi.libraryms.controller.AdminUserController.AdminUserController;

import com.tianqi.libraryms.entity.*;
import com.tianqi.libraryms.entity.JavaBean.BorrowinghandlingVO;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GYY
 * @description 用于定义普通管理员的页面跳转重定向
 */
@Controller
public class AdminPageController {
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private NormalUserService normalUserService;
    @Resource
    private BorrowRecordService borrowRecordService;
    @Resource
    private BookService bookService;
    @Resource
    private BookCategoryService bookCategoryService;
    @Resource
    private BorrowinghandlingService borrowinghandlingService;


    /**
     * @param request 请求
     * @param model   模型：用于传递数据
     * @return 返回视图信息。
     * @description 为普通管理员的首页，在这里主要进行页面数据的准备。
     */
    @GetMapping("/admin/adminUser/index")
    public String initAdminIndexPage(HttpServletRequest request, Model model) {
        //初始化加载数据：
        //1.用户信息
        Result<List<NormalUser>> normalUserList = normalUserService.getAllNormalUserInfo(request);
        List<NormalUser> users = normalUserList.getData();
        //进行倒序选择3人，不够三人就显示全部
        if (users.size() < 3) {
            model.addAttribute("normalUserList", users);
        } else {
            int start = users.size() - 3;
            model.addAttribute("normalUserList", users.subList(start, users.size()));
        }
        //4.热门书籍。
        //获取所有的借阅记录，得到其中被借阅数量最多的5本书，如果少于5本，则显示全部
        Result<List<BorrowRecord>> borrowRecordList = borrowRecordService.getAllBorrowRecordList(request);
        List<BorrowRecord> records = borrowRecordList.getData();
        Map<String, Integer> map = new HashMap<>();
        //计数操作。
        for (BorrowRecord record : records) {
            String bookTitle = record.getBookTitle();
            if (map.containsKey(bookTitle)) {
                map.put(bookTitle, map.get(bookTitle) + 1);
            } else {
                map.put(bookTitle, 1);
            }
        }
        //排序
        List<Map.Entry<String, Integer>> list = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
        if (list.size() < 4) {
            model.addAttribute("hotBookList", list);
        } else {
            model.addAttribute("hotBookList", list.subList(list.size() - 4, list.size()));
        }
        return "admin/adminUser/index";
    }

    /**
     * @param request 请求
     * @param model   模型:用于传递数据
     * @return 返回视图信息。
     * @description 用于书籍管理页面/admin/adminUser/bookManage的初始化。
     */
    @GetMapping("/admin/adminUser/bookManage")
    public String initAdminBookManagePage(HttpServletRequest request, Model model) {
        //1.查询所有的分类,将来会根据这些分类信息进行数据查询。
        Result<List<BookCategory>> allBookCategory = bookCategoryService.getAllBookCategory(request);
        model.addAttribute("allCategory", allBookCategory.getData());
        return "admin/adminUser/bookManage";
    }

    /**
     * @return 返回管理员注册页面：admin/adminUser/adminRegister的视图信息。
     * @description 用于管理员注册页面的初始化。
     */
    @GetMapping("/superAdmin/adminRegisterPage")
    public String adminRegister() {
        return "admin/superAdmin/adminRegister";

    }

    @GetMapping("/admin/adminUser/bookUpdate/{bookId}")
    public String initAdminBookUpdatePage(@PathVariable Integer bookId, Model model, HttpServletRequest request) {
        //1.查询书籍信息。
        Result<Book> bookById = bookService.getBookById(bookId, request);
        model.addAttribute("book", bookById.getData());
        //TODO：未完成
        return "admin/adminUser/bookUpdate";
    }

    /**
     * @param id      普通管理员的Id
     * @param model   模型，用于传递数据
     * @param request 请求
     * @return 返回视图信息。
     * @description 用于超级管理员在更新普通管理员（具体到某个人）的时候的页面跳转和数据准备。
     */
    @GetMapping("/superAdmin/adminUserUpdatePage/{id}")
    public String adminUserUpdate(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        //1.判断数据合法性
        if (id == null) {
            model.addAttribute("error", "管理员id不能为空！");
            return "admin/superAdmin/adminUserManage";
        }
        //2.查询用户信息
        Result<AdminUser> result = adminUserService.getAdminUserById(id, request);
        model.addAttribute("adminUser", result.getData());
        //到达更新页面。
        return "admin/superAdmin/updateAdminUser";
    }

    @GetMapping("/admin/adminUser/normalUserManage")
    public String initAdminNormalUserManagePage(HttpServletRequest request, Model model) {
        //1.查询所有的用户信息。
        Result<List<NormalUser>> result = normalUserService.getAllNormalUserInfo(request);
        model.addAttribute("normalUserList", result.getData());

        return "admin/adminUser/normalUserManage";
    }

    @GetMapping("/admin/adminUser/borrowManage")
    public String initAdminBorrowAndReturnManagePage(Model model) {
        //1.查询所有的待处理记录
        final Result<List<BorrowinghandlingVO>> allBorrowinghandlingVOList = borrowinghandlingService.getAllBorrowRecordList();
        if (allBorrowinghandlingVOList.getData().isEmpty()) {
            return "admin/missPageAdmin";

        }
        model.addAttribute("allBorrowinghandlingVOList", allBorrowinghandlingVOList.getData());
        return "admin/adminUser/borrow";
    }
}

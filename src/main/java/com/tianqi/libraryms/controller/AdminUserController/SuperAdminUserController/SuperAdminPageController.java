package com.tianqi.libraryms.controller.AdminUserController.SuperAdminUserController;

import com.tianqi.libraryms.entity.AdminUser;
import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.entity.Publisher;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.AdminUserService;
import com.tianqi.libraryms.service.BookCategoryService;
import com.tianqi.libraryms.service.PublisherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GYY
 * @description 用于控制页面的重定向访问
 */
@Controller
public class SuperAdminPageController {
    @Resource
    private PublisherService publisherService;
    @Resource
    private BookCategoryService bookCategoryService;
    @Resource
    private AdminUserService adminUserService;

    @GetMapping("/admin/login")
    public String showNormalUserPage() {
        return "admin/login";
    }

    /**
     * @return 返回视图信息。
     * @description 用于管理员主页数据的初始化和跳转控制。
     */
    @GetMapping("/admin/superAdmin/index")
    public String initSuperAdminIndexPage(HttpServletRequest request, Model model) {
        //1.获取所有的分类列表。
        final Result<List<BookCategory>> allBookCategory = bookCategoryService.getAllBookCategory(request);
        final List<BookCategory> data = allBookCategory.getData();
        //从data中随机选取五个分类，放到主页上，记住是随机。
        final int size = data.size();
        ArrayList<BookCategory> bookCategoryListData = new ArrayList<>();
        int count = 5;
        while (true) {
            int random = (int) (Math.random() * size);
            final BookCategory bookCategory = data.get(random);
            if (!bookCategoryListData.contains(bookCategory)) {
                bookCategoryListData.add(bookCategory);
                count--;
                if (count == 0) {
                    break;
                }
            }
        }

        model.addAttribute("allBookCategory", bookCategoryListData);

        //2.获取所有的出版社列表。
        final Result<List<Publisher>> allPublisher = publisherService.getAllPublisherInfo(request);
        final List<Publisher> data1 = allPublisher.getData();
        //从data1中随机选取五个出版社，放到主页上，记住是随机。
        final int size1 = data1.size();
        ArrayList<Publisher> publisherListData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * size1);
            publisherListData.add(data1.get(random));
        }
        model.addAttribute("allPublisher", publisherListData);
        //3.获取所有的管理员列表。
        final Result<List<AdminUser>> allAdminUser = adminUserService.getAllAdminUser(request);
        model.addAttribute("allAdminUser", allAdminUser.getData());
        return "admin/superAdmin/index";
    }
}

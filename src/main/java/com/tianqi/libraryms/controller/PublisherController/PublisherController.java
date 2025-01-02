package com.tianqi.libraryms.controller.PublisherController;

import com.tianqi.libraryms.entity.Publisher;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.PublisherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GYY
 * @description 出版社控制器类
 */
@Controller
public class PublisherController {
    @Resource
    private PublisherService publisherService;

    /**
     * @param model   用于填充数据
     * @param request 请求对象
     * @return 返回视图信息，用于展示。
     * @description 超级管理员--出版社管理页面
     */
    @GetMapping("/superAdmin/publisherManage")
    public String publisherManage(Model model, HttpServletRequest request) {
        //1.首先进行数据的加载填充，读取所有的出版社信息
        Result<List<Publisher>> result = publisherService.getAllPublisherInfo(request);
        model.addAttribute("publisherList", result.getData());
        return "admin/superAdmin/publisherManage";
    }

    /**
     * @return 返回试图信息。
     * @description 跳转到添加出版社信息页面
     */
    @GetMapping("/superAdmin/addPublisherPage")
    public String addPublisherPage() {
        return "admin/superAdmin/addPublisher";
    }

    /**
     * @param publisher 出版社信息的封装对象
     * @param model     用于填充数据
     * @param request   请求对象
     * @return 添加成功，重定向到出版社管理页面。
     * @description 超级管理员添加出版社信息
     */
    @PostMapping("/superAdmin/addPublisher")
    public String addPublisher(Publisher publisher, Model model, HttpServletRequest request) {
        //1.判断出版社名称是否为空
        if (publisher.getPublisherName() == null || publisher.getPublisherName().equals("")) {
            model.addAttribute("error", "出版社名称不能为空！");
            return "admin/superAdmin/addPublisher";
        }
        //2.添加出版社信息
        Result<String> result = publisherService.addPublisher(publisher, request);
        System.out.println(result.getMsg() + "---------------------");
        model.addAttribute("error", result.getMsg());
        return "redirect:/superAdmin/publisherManage";
    }

    /**
     * @param id      出版社id
     * @param request 请求对象
     * @param model   用于填充数据
     * @return 重定向到出版社管理页面。
     * @description 超级管理员删除出版社信息
     */
    @GetMapping("/superAdmin/deletePublisherById/{id}")
    public String deletePublisherById(@PathVariable("id") Integer id, HttpServletRequest request, Model model) {
        //1.删除出版社信息
        Result<String> result = publisherService.deletePublisher(id, request);
        model.addAttribute("error", result.getMsg());
        return "redirect:/superAdmin/publisherManage";
    }

    //4. 更新出版社信息
    //4.1 跳转至更新出版社信息页面
    @GetMapping("/superAdmin/updatePublisherPage/{id}")
    public String updatePublisherPage(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        //1.根据id获取出版社信息
        Result<Publisher> result = publisherService.getPublisherInfo(id, request);
        model.addAttribute("publisher", result.getData());
        return "admin/superAdmin/updatePublisher";
    }

    //4.2 更新出版社信息
    @PostMapping("/superAdmin/updatePublisher")
    public String updatePublisher(Publisher publisher, Model model, HttpServletRequest request) {
        //1.判断出版社名称是否为空
        if (publisher.getPublisherName() == null || publisher.getPublisherName().equals("")) {
            model.addAttribute("error", "出版社名称不能为空！");
            return "admin/superAdmin/updatePublisher";
        }
        //2.更新出版社信息
        Result<String> result = publisherService.updatePublisher(publisher, request);
        model.addAttribute("error", result.getMsg());
        return "redirect:/superAdmin/publisherManage";
    }

    /*注：用在了，普通管理员添加书籍时获取出版社信息：bookUpdate页面*/
    @GetMapping("/publishers")
    @ResponseBody
    public List<Publisher> getAllPublishers(HttpServletRequest request) {
        // 从数据库获取所有出版社数据
        final Result<List<Publisher>> allPublisherInfo = publisherService.getAllPublisherInfo(request);
        return allPublisherInfo.getData();
    }

}

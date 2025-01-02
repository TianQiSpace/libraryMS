package com.tianqi.libraryms.controller.NormalUserController;

import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.BookCategory;
import com.tianqi.libraryms.entity.JavaBean.PasswordVO;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookCategoryService;
import com.tianqi.libraryms.service.BookService;
import com.tianqi.libraryms.service.NormalUserService;
import com.tianqi.libraryms.vo.UserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tianqi.libraryms.constant.GlobalConstant.NORMAL_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 普通用户控制器类
 */
@Controller
@RequestMapping("/normalUser")
public class NormalUserController {
    @Resource
    private NormalUserService normalUserService;
    @Resource
    private BookCategoryService bookCategoryService;
    @Resource
    private BookService bookService;
    //1.注册接口；
    @GetMapping("/registerPage")
    public String normalRegister() {
        return "normalUser/register";
    }
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody NormalUser user, HttpServletRequest request) {

        Result<Integer> result = normalUserService.normalRegister(user);

        Map<String, Object> response = new HashMap<>();

        if (result.getCode() == 200) {
            request.getSession().setAttribute("newUserId", result.getData());
            response.put("message", "账户信息填写校验通过！");
            response.put("newUserId", result.getData());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "账户信息填写校验失败！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping("/activeById/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> activeById(@PathVariable Integer id, HttpServletRequest request) {
        //将用户的状态设置为已激活
        final Result<Integer> result = normalUserService.activeById(id, request);
        Map<String, Object> response = new HashMap<>();
        if (result.getCode() == 200) {
            response.put("message", "激活失败！");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "激活成功！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //2.登录接口；
    @PostMapping("/login")
    //这里使用VO类型进行替换。
    public ResponseEntity<?> normalLogin(@RequestBody UserVO userVO, HttpServletRequest request) {
        Result<Integer> result = normalUserService.normalLogin(userVO.username(), userVO.password(), request);
        if (result.getCode() == 200) {
            return ResponseEntity.ok(Map.of("success", true, "message", "登录成功"));
        } else if(result.getCode() == -1) {
            return ResponseEntity.ok(Map.of("success", false, "message", result.getMsg()));
        }else{
            return ResponseEntity.status(401).body(Map.of("message", result.getMsg()));
        }
    }
    //3.退出登录接口；
    @RequestMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
       normalUserService.normalLogout(request);
        // 返回成功响应
        return ResponseEntity.ok(Map.of("success", true, "message", "登出成功！"));
    }

    //4.注册时检验用户是否存在；
    @ResponseBody
    @PostMapping("/register/check")
    public Result<Boolean> checkUser(String username) {
        if (username == null) {
            return Result.error(-1, "参数为空");
        }
        final Result<Boolean> booleanResult = normalUserService.registerCheckUserName(username);
        System.out.println(username + "-----------被请求了-------------------" + booleanResult.getSuccess() + "------------------------");
        return normalUserService.registerCheckUserName(username);
    }
    //5.个人设置；
      //5.1个人设置触发；
    @GetMapping("/personInfo")
    public String settingPage(Model model, HttpServletRequest request) {
        //0.进行页面数据的初始化
        if (request.getSession().getAttribute("saveResult") == null) {
            Result<String> saveResult = Result.success("正常");
            request.getSession().setAttribute("saveResult", saveResult);
            model.addAttribute("saveResult", saveResult);
        } else {
            model.addAttribute("saveResult", request.getSession().getAttribute("saveResult"));
        }
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        // 1. 获取用户的所有信息
        Result<NormalUser> userInfo = normalUserService.getNormalUserInfoByName(user.getUsername(), request);
        // 2. 封装用户信息
        model.addAttribute("user", userInfo.getData());

        return "normalUser/personInfo";
    }

      //5.2 个人设置保存
    @PostMapping("/personInfo/save")
    public String saveSetting(NormalUser normalUser, HttpServletRequest request) {
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        // 1. 更新用户信息
        normalUser.setId(user.getId());
        Result<NormalUser> saveResult = normalUserService.updateNormalUserInfo(normalUser, request);
        //2.对session中的saveResult进行更新
        request.getSession().setAttribute("saveResult", saveResult);
        return "redirect:/normalUser/personInfo";
    }
    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordVO data, HttpServletRequest request) {
        try {
            Result<String> result= normalUserService.changePassword(data, request);
            if (result.getSuccess()) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "密码修改成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", result.getMsg()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器内部错误: " + e.getMessage()));
        }
    }
    //6.普通用户进行搜索。
    @GetMapping("/search")
    public String search(String keyword, Model model, HttpServletRequest request) {
        //1.在这里进行收缩过滤，重定向到书籍详情搜索、书籍分类搜索。
        if (StringUtils.isBlank(keyword)) {
            // 输入框为空，不进行查询，直接展示原页面
            return "redirect:/normalUser/index";
        }

        //2.搜索的关键字不为空，根据关键字进行搜索
           //2.1 优先根据关键词进行分类的精准查询，如果有对应完全相同的分类名称就跳转到搜索结果页面。
         Result<BookCategory> category = bookCategoryService.getBookCategoryByName(keyword, request);
        if (category.getSuccess()) {
            // 匹配到了分类，根据分类ID进行查询。
            model.addAttribute("category", category.getData());
            //获取所有分类,用来初始化页面。
            Result<List<BookCategory>> allBookCategory = bookCategoryService.getAllBookCategory(request);
            model.addAttribute("allCategory", allBookCategory.getData());
            return "normalUser/category";
        }
            //2.2.没有根据关键词找到分类，则根据关键词进行书籍名称或作者的模糊查询
        Result<List<Book>> result = bookService.searchBookByNameOrAuthor(keyword, request);
        if (result.getData().size() == 1) {
            // 2.1 匹配到了一本图书，直接跳转到该图书的详情页
            //对排行积分进行更新
             //1.首先确定触发这个方法的用户是普通用户。因为这个控制器是公用的，可能在后期会用到其他类型用户身上。
            NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
            if (user != null) {
                //2.如果用户不为空，则进行积分的更新。
                bookService.addBookPointBecauseSearch(result.getData().get(0).getBookId(), user.getId(), request);
            }
            //2.跳转到书籍详情页
            return "redirect:/books/search/" + result.getData().get(0).getBookId();
        } else if (result.getData().size() > 1) {
            // 2.2 匹配到了多本图书，跳转到搜索结果页
            model.addAttribute("books", result.getData());
            //TODO:多本书籍的展示
            return "redirect:/category/index";
        } else {
            // 2.3 匹配不到，返回设计好的空页面
            return "normalUser/missPage";
        }
    }
    //7.通过AJAX获取用户信息
    @GetMapping("/getUserInfo/{userId}")
    @ResponseBody
    public ResponseEntity<NormalUser> getUserInfo(@PathVariable Integer userId, HttpServletRequest request) {
        //1.根据id获取用户信息。
        Result<NormalUser> result = normalUserService.getNormalUserInfo(userId, request);
        NormalUser user = result.getData();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //8.通过AJAX更新用户信息
    @PostMapping("/updateUserInfo")
    @ResponseBody
    public ResponseEntity<NormalUser> updateUserInfo(NormalUser normalUser, HttpServletRequest request) {
        //1.更新用户信息
        Result<NormalUser> result = normalUserService.updateNormalUserInfo(normalUser, request);
        NormalUser user = result.getData();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

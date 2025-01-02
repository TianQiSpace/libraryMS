package com.tianqi.libraryms.controller.BookController;

import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.exception.ErrorInfo;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GYY
 * @description 用于进行书籍的控制
 */
@Controller
public class BookController {
    @Resource
    private BookService bookService;
    // 查询展示所有书籍
    @GetMapping("/books/showAll")
    public String getBooksByPublisher(HttpServletRequest request, Model model) {
        Result<List<Book>> result = bookService.getAllBooks(request);
        if (result.getSuccess()) {
            model.addAttribute("books", result.getData());
            return "admin/superAdmin/book";
        } else {
            // 处理错误情况
            final ErrorInfo errorInfo = new ErrorInfo(result.getCode(), result.getMsg());
            model.addAttribute("error",errorInfo);
            return "error/errorPage";
        }
    }
    //获取单本书籍以方便修改信息：
    @GetMapping("/books/getMessageToEdit/{id}")
    public String editBook(@PathVariable Integer id, Model model,HttpServletRequest request) {
        // 查询书籍信息并添加到模型中
        final Result<Book> bookById = bookService.getBookById(id, request);
        model.addAttribute("book", bookById.getData());
        return "admin/superAdmin/book";
    }
    //更新书籍信息
    @PostMapping("/books/updateOne")
    @ResponseBody
    public Map<String, Object> updateBook(Book book, HttpServletRequest request) {
        // 更新书籍信息
        Result<String> stringResult = bookService.editAndUpdateBook(book, request);
        // 准备返回给前端的数据
        Map<String, Object> response = new HashMap<>();
        // 可以返回消息给前端显示
        response.put("message", stringResult.getData());
        // 表示操作是否成功
        response.put("success", stringResult.getCode() == 200);
        return response;

    }
    //对一本书籍进行下架处理。
    @PostMapping("/books/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteBook(@PathVariable Integer id, HttpServletRequest request) {
        // 删除书籍信息
        Result<String> stringResult = bookService.deleteBook(id, request);
        // 准备返回给前端的数据
        Map<String, Object> response = new HashMap<>();
        // 可以返回消息给前端显示
        response.put("message", stringResult.getData());
        // 表示操作是否成功
        response.put("success", stringResult.getCode() == 200);
        return response;
    }
    //获取并返回一本书籍的详细信息
    @GetMapping("/books/search/{id}")
    public String searchOneBook(@PathVariable Integer id, Model model, HttpServletRequest request) {
        // 查询书籍信息并添加到模型中
       Result<Book> bookById = bookService.getBookById(id, request);
        model.addAttribute("book", bookById.getData());

        return "normalUser/book";
    }
    //根据书籍分类ID进行查询此类目下全部书籍
    @GetMapping("/searchBookByCategoryId/{id}")
    @ResponseBody
    public Result<List<Book>> searchBookByCategoryId(@PathVariable Integer id, HttpServletRequest request) {

        if(id==0){
            return bookService.getAllBooks(request);
        }
        // 查询书籍信息返回查询结果
        return bookService.searchBookByCategoryId(id, request);
    }
    //普通管理员更新部分书籍信息
    @PostMapping("/books/updateOneByAdmin")
    public String updateBookByAdmin(Book book,Integer quantity,HttpServletRequest request) {
        // 更新书籍信息
        bookService.editAndUpdateBook(book,request);
        return "redirect:/admin/adminUser/bookUpdate/";
    }
    //绑定排行榜功能，初始化页面
    @GetMapping("/books/rankingList")
    public String rankingList(Model model, HttpServletRequest request) {
        //1.获取所有的书籍信息。
        Result<List<Book>> result = bookService.getAllBooks(request);
        //2.分别制作男频、女频排行榜
          //2.1 男频排行榜：根据每本书的malePoint属性从大到小排序，并返回前5个书籍。
        List<Book> maleRankingList = result.getData().stream()
                .sorted((b1, b2) -> b2.getMalePoint() - b1.getMalePoint())
                .limit(6)
                .toList();
        model.addAttribute("maleRankingList", maleRankingList);
        //2.2 女频排行榜：根据每本书的femalePoint属性从大到小排序，并返回前5个书籍。
        List<Book> femaleRankingList = result.getData().stream()
                .sorted((b1, b2) -> b2.getFemalePoint() - b1.getFemalePoint())
                .limit(6)
                .toList();
        model.addAttribute("femaleRankingList", femaleRankingList);
        return "normalUser/Ranking";
    }
    //绑定书籍每日推荐功能，初始化页面
    @GetMapping("/books/recommend")
    public String recommend(Model model, HttpServletRequest request) {
        Result<List<Book>> result = bookService.getAllBooks(request);
        //1.获取所有的书籍信息。
        List<Book> allBooks = result.getData();
        //2.随机选择5本书作为每日推荐。
        List<Book> recommendBooks = allBooks.stream()
                .limit(5)
                .toList();
        model.addAttribute("recommendBooks", recommendBooks);
        System.out.println(recommendBooks.size());
        return "normalUser/recommend";
    }
    //新增书籍
    @PostMapping("/books/addBook")
    @ResponseBody
    public Map<String, Object> addBook(Book book, HttpServletRequest request) {
        Result<String> stringResult = bookService.addBook(book, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", stringResult.getMsg());
        response.put("success", stringResult.getCode() == 200);
        response.put("error",stringResult.getCode()==-1);
        return response;
    }

}

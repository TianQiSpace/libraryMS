package com.tianqi.libraryms.controller.NormalUserController;

import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.BorrowRecord;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookService;
import com.tianqi.libraryms.service.BorrowRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tianqi.libraryms.constant.GlobalConstant.NORMAL_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 用于控制页面路径跳转
 */
@Controller
public class NormalUserPageController {
    @Resource
    private BookService bookService;
    @Resource
    private BorrowRecordService borrowRecordService;

    //1.进行主页数据的初始化获取以及数据提交。
    @GetMapping("/normalUser/index")
    public String redirectNormalUserIndexPage(Model model, HttpServletRequest request) {
        /*----------------------------------------------------------*/
        //用户信息数据。
        model.addAttribute("user", request.getSession().getAttribute("normalUserLoginStatus"));
        //借阅记录信息数据。
        Result<List<BorrowRecord>> borrowRecordList = borrowRecordService.getBorrowRecordList(((NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS)).getId(), request);
        //只返回倒数后三个，按照最新借阅时间排序，同时注意纪录列表是否为空:最终显示两条记录。
        ArrayList<BorrowRecord> borrowRecordListData = new ArrayList<>();
        int number = borrowRecordList.getData().size();
        if (number == 0) {
            model.addAttribute("borrowRecordList", borrowRecordList.getData());
        } else if (number <= 2) {
            //将数据倒序存放到列表中。
            for (int i = number - 1; i >= 0; i--) {
                borrowRecordListData.add(borrowRecordList.getData().get(i));
            }
            model.addAttribute("borrowRecordList", borrowRecordListData);
        } else {
            borrowRecordListData.add(borrowRecordList.getData().get(number - 1));
            borrowRecordListData.add(borrowRecordList.getData().get(number - 2));
            model.addAttribute("borrowRecordList", borrowRecordListData);
        }
        /*---------------------------------------------------------------------------*/
        //热门书籍信息。
        final Result<List<Book>> allBooks = bookService.getAllBooks(request);
        model.addAttribute("allBooks", allBooks.getData());
        //1.给出一本特写书籍:根据性别给出。
        NormalUser normalUser = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        List<Book> featuredBook;
        if ("男".equals(normalUser.getGender())) {
            featuredBook = allBooks.getData().stream()
                    .sorted((b1, b2) -> b2.getMalePoint() - b1.getMalePoint())
                    .limit(6)
                    .toList();
        } else {
            featuredBook = allBooks.getData().stream()
                    .sorted((b1, b2) -> b2.getFemalePoint() - b1.getFemalePoint())
                    .limit(6)
                    .toList();
        }
        model.addAttribute("featuredBook", featuredBook.get(0));
        model.addAttribute("featuredBookList", featuredBook);
        /*---------------------------------------------------------------------*/
        //2.根据借阅记录信息，遍历计数得到用户最喜欢的分类，根据分类ID进行查询有关的书籍。
        //如果是空，则不进行操作。
        List<BorrowRecord> data = borrowRecordList.getData();
        if (!borrowRecordList.getData().isEmpty()) {
            Map<Integer, Integer> map = data.stream()
                    .collect(Collectors.groupingBy(BorrowRecord::getCategoryId, Collectors.summingInt(BorrowRecord::getBorrowStatus)));
            //按照借阅次数从大到小排序。
            List<Map.Entry<Integer, Integer>> list = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
            //获取第一个的分类ID
            Integer categoryId = list.get(list.size() - 1).getKey();
            Result<List<Book>> listResult = bookService.searchBookByCategoryId(categoryId, request);
            if(listResult.getData().size()<=3){
                model.addAttribute("featuredBookList2",listResult.getData());
            }else{
                model.addAttribute("featuredBookList2",listResult.getData().subList(0,3));
            }
        }else{
            model.addAttribute("featuredBookList2",null);
        }

        return "normalUser/index";
    }
}

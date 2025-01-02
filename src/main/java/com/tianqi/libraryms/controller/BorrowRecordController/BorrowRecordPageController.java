package com.tianqi.libraryms.controller.BorrowRecordController;

import com.tianqi.libraryms.entity.BorrowRecord;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BorrowRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.tianqi.libraryms.constant.GlobalConstant.NORMAL_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 用来控制借阅的页面跳转
 */
@Controller
public class BorrowRecordPageController {
    @Resource
    private BorrowRecordService borrowRecordService;
    @GetMapping("/normalUser/borrowAndRecord")
    public String initBorrowRecordPage(Model model, HttpServletRequest request) {
        //1.获取用户对象；
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        //2.获取用户的借阅记录
        Result<List<BorrowRecord>> borrowRecordList = borrowRecordService.getBorrowRecordList(user.getId(), request);
        ArrayList<BorrowRecord> borrowRecordListData = new ArrayList<>();
        //将 borrowRecordList.getData()的数据倒序存放到列表中
        if(borrowRecordList.getData().size()<=10){
            for (int i = borrowRecordList.getData().size()-1; i >=0 ; i--) {
                borrowRecordListData.add(borrowRecordList.getData().get(i));
            }
        }else{
            //将 borrowRecordList.getData()的数据倒序存放到列表中,但是只放置前10条数据。
            for (int i = borrowRecordList.getData().size()-1; i >=borrowRecordList.getData().size()-10 ; i--) {
                borrowRecordListData.add(borrowRecordList.getData().get(i));
            }

        }

        model.addAttribute("borrowRecordList", borrowRecordListData);
        return "normalUser/borrowAndRecord";
    }
}

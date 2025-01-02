package com.tianqi.libraryms.controller.BorrowRecordController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianqi.libraryms.entity.Borrowinghandling;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.mapper.BorrowinghandlingMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BorrowRecordService;
import com.tianqi.libraryms.service.NotificationsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.tianqi.libraryms.constant.GlobalConstant.NORMAL_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 控制器类
 */
@Controller
@Slf4j

public class BorrowRecordController {
    @Resource
    private BorrowRecordService borrowRecordService;
    @Resource
    private NotificationsService notificationsService;
    @Resource
    private BorrowinghandlingMapper borrowinghandlingMapper;

    @GetMapping("/books/borrow/{bookId}")
    @ResponseBody
    public Map<String, Object> borrowBook(@PathVariable("bookId") Integer bookId, HttpServletRequest request, Model model) {
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        final Result<String> stringResult = borrowRecordService.borrowBook(bookId, user.getId(), request);
        //接下来根据借书结果发送通知。
        if (stringResult.getCode() == -1) {
            notificationsService.sendNotificationsToNormalUser(user.getId(), "借阅通知", stringResult.getMsg(), "系统通知");
        } else if (stringResult.getCode() == 200) {
            notificationsService.sendNotificationsToNormalUser(user.getId(), "借阅通知", stringResult.getData(), "系统通知");
        }
        // 准备返回给前端的数据
        Map<String, Object> response = new HashMap<>();
        // 或者根据实际情况设置按钮的状态
        response.put("button", 200);
        // 可以返回消息给前端显示
        response.put("message", stringResult.getMsg());
        // 表示操作是否成功
        response.put("success", stringResult.getCode() == 200);
        return response;
    }

    @GetMapping("/books/return/{bookId}")
    @ResponseBody
    public Map<String, Object> returnBook(@PathVariable("bookId") Integer bookId, HttpServletRequest request) {
        NormalUser user = (NormalUser) request.getSession().getAttribute(NORMAL_USER_LOGIN_STATUS);
        final Result<String> stringResult = borrowRecordService.returnBookForUser(bookId, user.getId(), request);

        // 发送通知
        if (stringResult.getCode() == -1) {
            notificationsService.sendNotificationsToNormalUser(user.getId(), "借阅通知", stringResult.getMsg(), "系统通知");
        } else if (stringResult.getCode() == 200) {
            notificationsService.sendNotificationsToNormalUser(user.getId(), "借阅通知", stringResult.getData(), "系统通知");
        }

        // 准备返回给前端的数据
        Map<String, Object> response = new HashMap<>();
        // 或者根据实际情况设置按钮的状态
        response.put("button", 200);
        // 可以返回消息给前端显示
        response.put("message", stringResult.getMsg());
        // 表示操作是否成功
        response.put("success", stringResult.getCode() == 200);
        return response;
    }

    @PostMapping("/admin/borrowRecord/manage")
    @ResponseBody
    public Map<String, Object> manageBorrowRequest(
            @RequestParam("id") int id,
            @RequestParam("action") String action
    ) {
        log.info("管理员审批请求，ID: {}, 操作: {}", id, action);
        Map<String, Object> response = new HashMap<>();
        try {
            // 根据action的值进行不同的业务逻辑处理
            if ("通过".equals(action)) {
                // 处理通过的逻辑
                borrowRecordService.returnBookForAdminUser(id);
                response.put("success", true);
                response.put("message", "审批成功");
            } else if ("驳回".equals(action)) {
                // 处理驳回的逻辑，向用户发送通知
                QueryWrapper<Borrowinghandling> queryWrapper9 = new QueryWrapper<>();
                queryWrapper9.eq("id", id);
                Borrowinghandling borrowinghandling = borrowinghandlingMapper.selectOne(queryWrapper9);
                final Integer userId = borrowinghandling.getUserId();
                notificationsService.sendNotificationsToNormalUser(userId, "借阅通知", "您的借阅申请已被驳回", "系统通知");
                response.put("success", true);
                response.put("message", "驳回成功");
            } else {
                response.put("success", false);
                response.put("message", "无效的操作类型");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "处理失败: " + e.getMessage());
        }

        return response;
    }
}

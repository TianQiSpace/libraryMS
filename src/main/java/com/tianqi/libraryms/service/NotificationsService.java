package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.Notifications;
import com.tianqi.libraryms.result.Result;

import java.util.List;

/**
* @author 11357
* @description 针对表【notifications(存储系统中的所有普通用户的通知记录)】的数据库操作Service
*/
public interface NotificationsService extends IService<Notifications> {
    //1.获取用户的通知记录.
     Result<List<Notifications>> getNormalUserNoReadNotifications(Integer userId);
     //2.发送通知.
    void sendNotificationsToNormalUser(Integer userId, String title, String message, String type);
    //3.一键已读
    void readAllNormalUserNotifications(Integer userId);
    //4.获取管理员通知记录.
}

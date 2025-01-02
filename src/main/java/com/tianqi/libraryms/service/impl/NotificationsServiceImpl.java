package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Notifications;
import com.tianqi.libraryms.mapper.NotificationsMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.NotificationsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GYY
 * @description 针对表【notifications(存储系统中的所有普通用户的通知记录)】的数据库操作Service实现
 */
@Service
public class NotificationsServiceImpl extends ServiceImpl<NotificationsMapper, Notifications>
        implements NotificationsService {
    @Resource
    private NotificationsMapper notificationsMapper;

    @Override
    public Result<List<Notifications>> getNormalUserNoReadNotifications(Integer userId) {
        QueryWrapper<Notifications> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("message_status","未读");
        List<Notifications> notificationsList = notificationsMapper.selectList(queryWrapper);
        //将查询结果按照日期降序排列
        notificationsList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return Result.success(notificationsList);
    }

    @Override
    public void sendNotificationsToNormalUser(Integer userId, String title, String message, String type) {
        Notifications notifications = new Notifications();
        notifications.setUserId(userId);
        notifications.setTitle(title);
        //TODO：消息类型检验、消息内容检验--> 待完善(扩展)
        notifications.setMessage(message);
        notifications.setMessageStatus("未读");
        notifications.setType(type);
        notificationsMapper.insert(notifications);
        Result.success("发送成功！");
    }

    @Override
    public void readAllNormalUserNotifications(Integer userId) {
        //1.根据用户id获取用户的所有未读消息
        QueryWrapper<Notifications> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("message_status","未读");
        List<Notifications> notificationsList = notificationsMapper.selectList(queryWrapper);
        for (Notifications notifications : notificationsList) {
            notifications.setMessageStatus("已读");
            notificationsMapper.updateById(notifications);
        }

    }
}





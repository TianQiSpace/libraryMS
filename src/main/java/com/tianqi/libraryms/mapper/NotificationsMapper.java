package com.tianqi.libraryms.mapper;

import com.tianqi.libraryms.entity.Notifications;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【notifications(存储系统中的所有普通用户的通知记录)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.Notifications
*/
@Mapper
public interface NotificationsMapper extends BaseMapper<Notifications> {

}





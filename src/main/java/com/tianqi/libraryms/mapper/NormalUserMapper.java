package com.tianqi.libraryms.mapper;

import com.tianqi.libraryms.entity.NormalUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【normal_user(用户表，存储系统中的用户信息)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.NormalUser
*/
@Mapper
public interface NormalUserMapper extends BaseMapper<NormalUser> {

}





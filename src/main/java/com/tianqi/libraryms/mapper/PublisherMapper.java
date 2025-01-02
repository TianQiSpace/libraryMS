package com.tianqi.libraryms.mapper;

import com.tianqi.libraryms.entity.Publisher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GYY
* @description 针对表【publisher(出版社表)】的数据库操作Mapper
* @Entity com.tianqi.libraryms.entity.Publisher
*/
@Mapper
public interface PublisherMapper extends BaseMapper<Publisher> {

}





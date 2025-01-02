package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.Publisher;
import com.tianqi.libraryms.mapper.BookMapper;
import com.tianqi.libraryms.mapper.PublisherMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.PublisherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tianqi.libraryms.constant.GlobalConstant.ADMIN_USER_LOGIN_STATUS;
import static com.tianqi.libraryms.constant.GlobalConstant.SUPER_ADMIN_USER_LOGIN_STATUS;

/**
 * @author GYY
 * @description 针对表【publisher(出版社表)】的数据库操作Service实现
 */
@Service
public class PublisherServiceImpl extends ServiceImpl<PublisherMapper, Publisher>
        implements PublisherService {
    @Resource
    PublisherMapper publisherMapper;
    @Resource
    BookMapper bookMapper;

    @Override
    public Result<List<Publisher>> getAllPublisherInfo(HttpServletRequest request) {
        //1.首先需要进行鉴权：只有管理员才能进行使用该方法。
        final Object attribute = request.getSession().getAttribute(ADMIN_USER_LOGIN_STATUS);
        final Object attribute1 = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute != null || attribute1 != null) {
            //说明是管理员：直接返回所有信息。
            return Result.success(publisherMapper.selectList(null));
        }
        return Result.error(-1, "您没有权限执行此操作！");
    }

    @Override
    public Result<String> addPublisher(Publisher publisher, HttpServletRequest request) {
        //1.数据校验
        if (StringUtils.isAllBlank(publisher.getPublisherName(), publisher.getAddress(), publisher.getContactPhone(), publisher.getEmail(), publisher.getWebsite())) {
            return Result.error(-1, "信息存在空值！");
        }
        //2.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "您没有权限执行此操作！");
        }
        QueryWrapper<Publisher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisher_name", publisher.getPublisherName());
        //3.判断数据库中是否存在该数据
        if (publisherMapper.selectOne(queryWrapper) != null) {
            return Result.error(-1, "该出版社信息已存在！");
        }
        publisherMapper.insert(publisher);
        return Result.success("添加成功！");
    }

    @Override
    public Result<String> deletePublisher(Integer id, HttpServletRequest request) {
        //1.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "您没有权限执行此操作！");
        }
        //2.安全性检查：检查有没有含有此信息的图书,查询所有的图书。
        //2.1 查询是否具有该出版社
        Publisher publisher = publisherMapper.selectOne(new QueryWrapper<Publisher>().eq("publisher_id", id));
        if (publisher == null) {
            return Result.error(-1, "该出版社不存在！");
        }
        //2.2 查询是否含有该出版社的图书
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisher_id", publisher.getPublisherId());
        final List<Book> books = bookMapper.selectList(queryWrapper);
        if (!books.isEmpty()) {
            return Result.error(-1, "该出版社下还有图书，无法删除！");
        }
        publisherMapper.delete(new QueryWrapper<Publisher>().eq("publisher_id", id));
        return Result.success("删除成功！");
    }

    @Override
    public Result<Publisher> getPublisherInfo(Integer id, HttpServletRequest request) {
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        final Object attribute1 = request.getSession().getAttribute(ADMIN_USER_LOGIN_STATUS);
        if (attribute == null&& attribute1==null) {
            return Result.error(-1, "您没有权限执行此操作！");
        }
        return Result.success(publisherMapper.selectOne(new QueryWrapper<Publisher>().eq("publisher_id", id)));

    }

    @Override
    public Result<String> updatePublisher(Publisher publisher, HttpServletRequest request) {
        //1.鉴权
        final Object attribute = request.getSession().getAttribute(SUPER_ADMIN_USER_LOGIN_STATUS);
        if (attribute == null) {
            return Result.error(-1, "您没有权限执行此操作！");
        }
        publisherMapper.update(publisher, new QueryWrapper<Publisher>().eq("publisher_id", publisher.getPublisherId()));
        return Result.success("修改成功！");
    }

}






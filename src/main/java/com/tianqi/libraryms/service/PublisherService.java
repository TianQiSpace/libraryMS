package com.tianqi.libraryms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianqi.libraryms.entity.Publisher;
import com.tianqi.libraryms.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 11357
* @description 针对表【publisher(出版社表)】的数据库操作Service
*/
public interface PublisherService extends IService<Publisher> {
    //1.获取所有的出版社信息。【完全体】
    /**
     * @description 只有管理员才能访问。
     * @param request 请求
     * @return 所有的出版社信息
     */
    Result<List<Publisher>> getAllPublisherInfo(HttpServletRequest request);
    //2.添加一个出版社信息。【完全体】

    /**
     * @description 使用者仅为超级管理员。
     * @param publisher 出版社信息封装
     * @param request 请求
     * @return 插入状况信息。
     */
    Result<String> addPublisher(Publisher publisher, HttpServletRequest request);
    //3.删除一个出版社信息。【完全体】

    /**
     * @description  使用者仅为超级管理员。
     * @param id 出版社的id
     * @param request 请求
     * @return 删除状况信息。
     */
    Result<String> deletePublisher(Integer id, HttpServletRequest request);
    //4.获得一个出版社信息。【完全体】
    Result<Publisher> getPublisherInfo(Integer id, HttpServletRequest request);
    //5.更新一个出版社信息。【完全体】
    Result<String> updatePublisher(Publisher publisher, HttpServletRequest request);
}

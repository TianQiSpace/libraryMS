package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.BorrowRecord;
import com.tianqi.libraryms.entity.Borrowinghandling;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.mapper.BookMapper;
import com.tianqi.libraryms.mapper.BorrowRecordMapper;
import com.tianqi.libraryms.mapper.BorrowinghandlingMapper;
import com.tianqi.libraryms.mapper.NormalUserMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BorrowRecordService;
import com.tianqi.libraryms.service.NotificationsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GYY
 * @description 针对表【borrow_record(借阅记录表)】的数据库操作Service实现
 */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord>
        implements BorrowRecordService {
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private NormalUserMapper normalUserMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BorrowinghandlingMapper borrowinghandlingMapper;
    @Resource
    private NotificationsService notificationsService;

    /**
     * 获取用户借阅记录列表
     *
     * @param userId  用户ID，用于查询借阅记录
     * @param request HTTP请求对象，此处未使用，但可能在后续扩展中用于获取请求信息
     * @return 返回一个Result对象，包含用户借阅记录列表如果userId为空，则返回错误信息
     */
    @Override
    public Result<List<BorrowRecord>> getBorrowRecordList(Integer userId, HttpServletRequest request) {
        // 1. 判断用户id是否为空
        if (userId == null) {
            return Result.error(-1, "传入参数为空");
        }
        //2.根据用户id获取用户的借阅记录
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<BorrowRecord> borrowRecordList = borrowRecordMapper.selectList(queryWrapper);
        return Result.success(borrowRecordList);
    }

    /**
     * 借阅书籍方法
     *
     * @param bookId  书籍ID
     * @param userId  用户ID
     * @param request HTTP请求对象，用于获取请求信息
     * @return 返回借阅结果，包括是否成功和提示信息
     */
    @Override
    public Result<String> borrowBook(Integer bookId, Integer userId, HttpServletRequest request) {
        // 0. 首先将对应要借阅的书籍信息查询出来
        Book book = bookMapper.selectById(bookId);

        // 1. 判断用户当前借阅数量是否小于等于借阅额度，如果是，则继续执行，否则返回错误信息
        QueryWrapper<NormalUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        NormalUser normalUser = normalUserMapper.selectOne(queryWrapper);
        if (normalUser.getBorrowNumber() >= normalUser.getBorrowQuota()) {
            return Result.error(-1, "借阅通知！书籍：【" + book.getTitle() + "】借阅失败！您的借阅额度已达上限啦！");
        }

        // 2. 有额度，判断用户信誉积分是否正常。
        if (normalUser.getCreditPoints() < 60) {
            return Result.error(-1, "借阅通知！书籍：【" + book.getTitle() + "】借阅失败！您的信用积分低于60！请联系管理员申请解禁！");
        }

        // 3. 判断用户是否已经借阅过了这本书籍
        if (borrowRecordMapper.selectOne(new QueryWrapper<BorrowRecord>().eq("user_id", userId).eq("book_id", bookId).eq("borrow_status", 0)) != null) {
            return Result.error(-1, "借阅通知！书籍：【" + book.getTitle() + "】借阅失败！您已经借阅过这本书籍了！");
        }

        // 4. 判断书籍库存还有无
        int quantity = book.getQuantity();
        if (quantity == 0) {
            return Result.error(-1, "借阅通知！书籍：【" + book.getTitle() + "】借阅失败！书籍库存不足！请下次再来看看吧！");
        }

        // 5. 借阅数据写入
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(bookId);
        borrowRecord.setBookTitle(book.getTitle());
        borrowRecord.setCategoryId(book.getCategoryId());
        borrowRecord.setBorrowDate(new java.sql.Date(System.currentTimeMillis()));
        borrowRecord.setDueDate(new java.sql.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        borrowRecord.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
        borrowRecord.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
        borrowRecord.setBorrowStatus(0);
        borrowRecord.setBorrowStatusText("未归还");
        borrowRecordMapper.insert(borrowRecord);

        // 6. 书籍库存数量-1
        QueryWrapper<Book> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("book_id", bookId);
        book.setQuantity(book.getQuantity() - 1);
        //判断这个用户的性别是男还是女。
        if ("男".equals(normalUser.getGender())) {
            book.setMalePoint(book.getMalePoint() + 10);
        } else {
            book.setFemalePoint(book.getFemalePoint() + 10);
        }
        bookMapper.update(book, queryWrapper2);


        // 7. 用户借阅数量+1
        QueryWrapper<NormalUser> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("id", userId);
        normalUser.setBorrowNumber(normalUser.getBorrowNumber() + 1);
        normalUserMapper.update(normalUser, queryWrapper3);
        return Result.success("借阅通知！书籍：【" + book.getTitle() + "】 借阅成功！请于30天内归还！");
    }

    /**
     * 归还书籍服务方法
     *
     * @param bookId  书籍ID，用于标识待归还的书籍
     * @param userId  用户ID，用于标识归还书籍的用户
     * @param request HTTP请求对象，可用于获取请求相关信息
     * @return 返回一个Result对象，包含归还操作的结果信息
     */
    @Override
    public Result<String> returnBookForUser(Integer bookId, Integer userId, HttpServletRequest request) {
        //1.首先进行数据查询，查询可用的借阅记录。
        QueryWrapper<BorrowRecord> borrowRecordQueryWrapper = new QueryWrapper<>();
        borrowRecordQueryWrapper.eq("user_id", userId).eq("book_id", bookId).eq("borrow_status", 0);
        BorrowRecord borrowRecord = borrowRecordMapper.selectOne(borrowRecordQueryWrapper);

        //2.获取用户信息
        QueryWrapper<NormalUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", userId);
        //判断对应的书籍处理申请是否已经发送过了
        if (borrowinghandlingMapper.selectOne(new QueryWrapper<Borrowinghandling>().eq("user_id", userId).eq("book_id", bookId).eq("type", "归还")) != null) {
            return Result.error(-1, "归还通知！您已经申请归还过这本书籍了！请勿重复申请！");
        }
        //3.将请求信息发送到管理员待处理表上面。让管理员处理。
        Borrowinghandling borrowinghandling = new Borrowinghandling();
        borrowinghandling.setUserId(userId);
        borrowinghandling.setBookId(bookId);
        borrowinghandling.setType("归还");
        //设置状态：0代表未处理。
        borrowinghandling.setStatus(0);
        //写入到数据库中：
        borrowinghandlingMapper.insert(borrowinghandling);
        //4.给用户一个提示信息。
        Result<String> result = new Result<>();
        result.setMsg("归还通知！书籍：【" + borrowRecord.getBookTitle() + "】 已申请归还！请等待管理员处理！");
        result.setCode(200);
        result.setData("归还通知！书籍：【" + borrowRecord.getBookTitle() + "】 已申请归还！请等待管理员处理！");
        //5.更新借阅记录
        BorrowRecord borrowRecord1 = new BorrowRecord();
        borrowRecord1.setBorrowStatusText("待处理");
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("book_id", bookId).eq("borrow_status", 0);
        borrowRecordMapper.update(borrowRecord1, queryWrapper);
        return result;
    }

    @Override
    public void returnBookForAdminUser(Integer id) {
        //得到待处理的数据
        QueryWrapper<Borrowinghandling> queryWrapper9 = new QueryWrapper<>();
        queryWrapper9.eq("id", id);
        Borrowinghandling borrowinghandling = borrowinghandlingMapper.selectOne(queryWrapper9);
        Integer bookId = borrowinghandling.getBookId();
        Integer userId = borrowinghandling.getUserId();
        //根据给出的用户和数据信息，进行处理。
        Book book = bookMapper.selectById(bookId);
        //1.首先进行数据查询，查询可用的借阅记录。
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("book_id", bookId).eq("borrow_status", 0);
        BorrowRecord borrowRecord = borrowRecordMapper.selectOne(queryWrapper);

        //2.获取用户信息
        QueryWrapper<NormalUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", userId);
        NormalUser normalUser = normalUserMapper.selectOne(queryWrapper1);
        //3.判断是否超时归还
        if (borrowRecord.getDueDate().getTime() < System.currentTimeMillis()) {
            //3.1 更新借阅记录
            borrowRecord.setBorrowStatus(1);
            borrowRecord.setReturnDate(new java.sql.Date(System.currentTimeMillis()));
            borrowRecord.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
            borrowRecord.setBorrowStatusText("已归还");
            borrowRecordMapper.update(borrowRecord, queryWrapper);
            //3.2 更新用户信息
            normalUser.setCreditPoints(normalUser.getCreditPoints() - 10);
            normalUser.setBorrowQuota(normalUser.getBorrowQuota() - 1);
            normalUser.setBorrowNumber(normalUser.getBorrowNumber() - 1);
            normalUserMapper.update(normalUser, queryWrapper1);
            //3.3 更新库存数量

            QueryWrapper<Book> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("book_id", bookId);
            book.setQuantity(book.getQuantity() + 1);
            bookMapper.update(book, queryWrapper2);

            //3.4 将工作信息进行更新
            QueryWrapper<Borrowinghandling> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("user_id", userId).eq("book_id", bookId).eq("type", "归还");
            //删除
            borrowinghandlingMapper.delete(queryWrapper3);
            //向用户发送信息
            notificationsService.sendNotificationsToNormalUser(userId, "超时归还通知", "书籍：【" + borrowRecord.getBookTitle() + "】 未在额定时间内归还！系统惩罚！用户借阅额度-1 ，信誉积分-10 ，请注意！信誉积分低于60分将无法进行书籍借阅！", "系统通知");

        } else {

            //3.1 更新借阅记录
            borrowRecord.setBorrowStatus(1);
            borrowRecord.setReturnDate(new java.sql.Date(System.currentTimeMillis()));
            borrowRecord.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
            borrowRecord.setBorrowStatusText("已归还");
            borrowRecordMapper.update(borrowRecord, queryWrapper);

            //3.2 更新用户信息
            if (normalUser.getCreditPoints() < 100) {
                int number = normalUser.getCreditPoints() + 2;
                if (number >= 100) {
                    //赋值最大值
                    number = 100;
                }
                normalUser.setCreditPoints(number);
            }
            normalUser.setBorrowNumber(normalUser.getBorrowNumber() - 1);
            normalUserMapper.update(normalUser, queryWrapper1);
            //3.3 更新库存数量
            QueryWrapper<Book> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("book_id", bookId);
            book.setQuantity(book.getQuantity() + 1);
            bookMapper.update(book, queryWrapper2);
            QueryWrapper<Borrowinghandling> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("user_id", userId).eq("book_id", bookId).eq("type", "归还");
            //删除
            borrowinghandlingMapper.delete(queryWrapper3);
            notificationsService.sendNotificationsToNormalUser(userId, "归还通知", "书籍：【" + borrowRecord.getBookTitle() + "】 归还成功！", "系统通知");
        }
    }

    /**
     * 获取所有借阅记录列表
     * 此方法用于从数据库中获取所有借阅记录，并按照创建时间降序排列
     * 使用了QueryWrapper来构建查询条件，主要侧重于获取最新的借阅记录
     *
     * @param request HTTP请求对象，可用于获取请求相关的信息
     * @return 返回一个Result对象，其中包含借阅记录列表如果获取成功，Result对象将表示成功状态
     */
    @Override
    public Result<List<BorrowRecord>> getAllBorrowRecordList(HttpServletRequest request) {
        // 创建一个QueryWrapper对象，用于设置查询条件
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        // 设置查询条件为按照created_at字段降序排序
        queryWrapper.orderByDesc("created_at");
        // 执行查询，获取借阅记录列表
        List<BorrowRecord> borrowRecordList = borrowRecordMapper.selectList(queryWrapper);
        // 返回成功结果，包含借阅记录列表
        return Result.success(borrowRecordList);
    }
}





package com.tianqi.libraryms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianqi.libraryms.entity.Book;
import com.tianqi.libraryms.entity.BorrowRecord;
import com.tianqi.libraryms.entity.NormalUser;
import com.tianqi.libraryms.mapper.BookCategoryMapper;
import com.tianqi.libraryms.mapper.BookMapper;
import com.tianqi.libraryms.mapper.BorrowRecordMapper;
import com.tianqi.libraryms.mapper.PublisherMapper;
import com.tianqi.libraryms.result.Result;
import com.tianqi.libraryms.service.BookService;
import com.tianqi.libraryms.service.NormalUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author GYY
 * @description 针对表【book(图书表)】的数据库操作Service实现
 */

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
        implements BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private NormalUserService normalUserService;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private BookCategoryMapper bookCategoryMapper;
    @Resource
    private PublisherMapper publisherMapper;
    /**
     * 根据关键词搜索图书
     * @param keyword 关键词，可以是书名或作者名的一部分
     * @param request HTTP请求对象，用于获取会话信息以检查用户登录和权限状态
     * @return 包含搜索结果的Result对象，如果成功则包含图书列表，否则包含错误信息
     * @description 所有类型用户
     */
    @Override
    public Result<List<Book>> searchBookByNameOrAuthor(String keyword, HttpServletRequest request) {
        // 按照给出的关键词进行搜索，可以按照书名或作者进行搜索，支持模糊查询
        //0.判断数据合法性
        if (keyword == null || StringUtils.isBlank(keyword)) {
            return Result.error(-1, "关键词不能为空！");
        }
        // 3. 构造查询条件
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword).or().like("author", keyword);
        // 4. 执行查询
        List<Book> books = bookMapper.selectList(queryWrapper);
        // 5. 返回查询结果
        return Result.success(books);
    }

    @Override
    public Result<List<Book>> searchBookByPublisher(Integer publisherId, HttpServletRequest request) {
        //1.判断数据合法性
        if (publisherId<=0) {
            return Result.error(-1, "出版社信息违法!");
        }
        //3.执行查询
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisher_id", publisherId);
        return Result.success(bookMapper.selectList(queryWrapper));
    }

    /**
     * @description 适用于所有类型用户
     * @param categoryId 书籍分类ID
     * @param request http请求
     * @return 单类书籍列表
     */
    @Override
    public Result<List<Book>> searchBookByCategoryId(Integer categoryId, HttpServletRequest request) {
        //1.根据分类ID查询书籍
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        List<Book> books = bookMapper.selectList(queryWrapper);
        //2. 返回查询结果
        return Result.success(books);
    }

    /**
     * @description 管理员添加书籍
     * @param book 书籍信息封装
     * @param request http请求
     * @return 添加结果
     */
    @Override
    public Result<String> addBook(Book book, HttpServletRequest request) {
        //1.数据校验,及其完整性验证
        if (book == null) {
            return Result.error(-1, "添加失败！书籍信息不完整！");
        }else{
            //2.判断数据封装是否完整:除了书名、作者、出版日期、出版社、分类、摘要、封面、状态、其他字段均可为空
            //检验不能存在任何一项为空，获取book的所有属性值。
            if(StringUtils.isAnyEmpty(book.getTitle(),book.getAuthor(),book.getPublishDate().toString(),book.getCategoryId().toString(),book.getPublisherId().toString(),book.getSummary(),book.getCoverImageUrl(),book.getQuantity().toString())){
                return Result.error(-1, "添加失败！请检查数据添加完整性！");
            }
            //2.1 对书名进行检查。
            if (StringUtils.isBlank(book.getTitle())) {
                return Result.error(-1, "添加失败！书名不能为空！");
            }
              //检查书名是否被占用了。
            if (bookMapper.selectOne(new QueryWrapper<Book>().eq("title", book.getTitle())) != null) {
                return Result.error(-1, "添加失败！数据库中已经入库了同名书籍！请检查添加必要性！");
            }
            //2.2 对作者进行检查
            if (StringUtils.isBlank(book.getAuthor())) {
                return Result.error(-1, "添加失败！作者不能为空！");
            }
            //2.3 对出版日期进行检查，不能大于当前时间。
            if (book.getPublishDate() != null && book.getPublishDate().after(new java.util.Date())) {
                return Result.error(-1, "添加失败！出版日期不能大于当前时间！");
            }
            //2.4 对出版社ID进行检查，不能小于等于0。
            if (book.getPublisherId() <= 0) {
                return Result.error(-1, "添加失败！出版社ID不能小于等于0！");
            }
              //出版社信息必须是存在的。
            if (publisherMapper.selectById(book.getPublisherId()) == null) {
                return Result.error(-1, "添加失败！出版社信息不存在！");
            }
            //2.5 对分类ID进行检查，不能小于等于0。
            if (book.getCategoryId() <= 0) {
                return Result.error(-1, "添加失败！分类ID不能小于等于0！");
            }
            //分类信息必须是存在的。
            if (bookCategoryMapper.selectById(book.getCategoryId()) == null) {
                return Result.error(-1, "添加失败！分类信息不存在！");
            }
            //2.6 对摘要进行检查，不能为空。
            if (StringUtils.isBlank(book.getSummary())) {
                return Result.error(-1, "添加失败！书籍摘要不能为空！");
            }
            //2.7 对封面图片路径进行检查，不能为空。
            if (StringUtils.isBlank(book.getCoverImageUrl())) {
                return Result.error(-1, "添加失败！封面图片路径不能为空！");
            }
            //2.8 对书籍库存进行检查，不能小于0。
            if (book.getQuantity() < 0) {
                return Result.error(-1, "添加失败！书籍库存不能小于0！");
            }
            /*----------------------确认数据无误之后，进行数据添加--------------*/
            String time = String.valueOf(System.currentTimeMillis());
            //截取前9位作为随机数ISBN
            String isbn = time.substring(0, 9);
            book.setIsbn(isbn);
            //3.添加书籍
            final int insert = bookMapper.insert(book);
            if (insert <= 0) {
                return Result.error(-1, "数据库错误！书籍添加失败！");
            }
            Result<String> result=new Result<>();
            result.setMsg("书籍添加成功！");
            result.setCode(200);
            return result;
        }
    }

    @Override
    public Result<String> editAndUpdateBook(Book book,HttpServletRequest request) {
        //1.获取要更新的书籍对象
        int bookId = book.getBookId();
        //2.对书籍信息进行基础校验：因为只允许修改：书籍简介、书籍封面路径、书籍库存数量
        if (book.getSummary() == null || book.getCoverImageUrl() == null || book.getQuantity() < 0) {
            return Result.error(-1, "书籍更新参数违法！");
        }
        //3.对书籍信息进行更新。
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        bookMapper.update(book, queryWrapper);
        return Result.success("书籍信息更新成功！");
    }

    @Override
    @Transactional
    public Result<String> deleteBook(Integer id, HttpServletRequest request) {
        //1.检验数据,判断在当前的借阅库中是否具有此书籍的借阅记录。
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", id).eq("borrow_status",0);
        if (borrowRecordMapper.selectOne(queryWrapper) != null) {
            return Result.error(-1, "书籍下架失败！书籍存在借阅记录，无法删除！");
        }else{
            bookMapper.deleteById(id);
            Result<String> result = Result.success("书籍下架成功！");
            result.setMsg("书籍下架成功!");
            return result;
        }
    }


    @Override
    public Result<Book> getBookById(Integer bookId, HttpServletRequest request) {
        //1.检验数据
        if (bookId <= 0) {
            return Result.error(-1, "参数违法！");
        }
        return Result.success(bookMapper.selectById(bookId));
    }

    @Override
    public Result<List<Book>> getAllBooks(HttpServletRequest request) {
        return Result.success(bookMapper.selectList(null));
    }

    @Override
    public void addBookPointBecauseSearch(Integer bookId,Integer userId, HttpServletRequest request) {
        //1.这本书籍被定向搜索到了，就根据搜索它的用户的性别进行对应的指数的更新。
        //查询用户
        NormalUser normalUser = normalUserService.getById(userId);
        //获取用户性别：
        String gender = normalUser.getGender();
        //2.查询书籍信息
        Book book = bookMapper.selectById(bookId);
        if ("女".equals(gender)){
            //3.更新书籍femalePoint
            book.setFemalePoint(book.getFemalePoint()+1);
        }else{
            //3.更新书籍malePoint
            book.setMalePoint(book.getMalePoint()+1);
        }
        bookMapper.updateById(book);
    }
}





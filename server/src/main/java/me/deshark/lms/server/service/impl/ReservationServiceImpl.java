package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.mapper.BookCopyMapper;
import me.deshark.lms.server.model.entity.BookBorrow;
import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.model.entity.User;
import me.deshark.lms.server.mapper.BookBorrowMapper;
import me.deshark.lms.server.mapper.BookMapper;
import me.deshark.lms.server.mapper.UserMapper;
import me.deshark.lms.server.service.intf.IReservationService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ReservationServiceImpl implements IReservationService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BookMapper bookMapper;
    
    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Autowired
    private BookCopyMapper bookCopyMapper;

    // 预借图书
    @Override
    public ResultResponse<String> reserveBook(String isbn, Long userId, Date expectedBorrowDate) {

        // 检查用户是否符合借阅条件
        if (isUserEligibleForBorrowing(userId)) {
            return ResultResponse.error(400, "用户不符合借阅条件");
        }

        // 检查用户是否已预借此书
        if (bookBorrowMapper.getReservedBookCountByIsbn(userId, isbn) > 0) {
            return ResultResponse.error(400, "用户已预借此图书");
        }

        // 检查用户是否已经借阅此书
        if (bookBorrowMapper.getBorrowedBookCountByIsbn(userId, isbn) > 0) {
            return ResultResponse.error(400, "用户已借阅此图书");
        }

        // 检查图书是否可用
        if (!isBookAvailable(isbn)) {
            return ResultResponse.error(400, "图书不可预借");
        }

        // 获取一个可用的图书副本
        Long bookCopyId = bookCopyMapper.getAvailableBookCopyId(isbn);

        // 更新图书副本状态为预借
        bookCopyMapper.updateBookCopyStatus(bookCopyId, "RESERVED");

        // 创建预借记录
        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBookCopyId(bookCopyId);
        bookBorrow.setUserId(userId);
        bookBorrow.setStatus("RESERVED");
        bookBorrow.setExpectedBorrowDate(expectedBorrowDate);

        // 插入预借记录
        bookBorrowMapper.insertBookBorrow(bookBorrow);

        return ResultResponse.success("图书预借成功");
    }

    // 借阅图书
    @Override
    public ResultResponse<String> borrowBook(String isbn, Long userId) {

        // 检查用户是否符合借阅条件
        if (isUserEligibleForBorrowing(userId)) {
            return ResultResponse.error(400, "用户不符合借阅条件");
        }

        // 检查用户是否已预借此书
        if (bookBorrowMapper.getReservedBookCountByIsbn(userId, isbn) == 0) {
            // 如果用户未预借此书，则检查用户是否已借此书
            if (bookBorrowMapper.getBorrowedBookCountByIsbn(userId, isbn) > 0) {
                return ResultResponse.error(400, "用户已借阅此图书");
            }
            // 如果用户未借此书，则检查图书是否可用
            if (!isBookAvailable(isbn)) {
                return ResultResponse.error(400, "图书不可借阅");
            }
            // 如果图书可用，则获取一个可用的图书副本
            Long bookCopyId = bookCopyMapper.getAvailableBookCopyId(isbn);
            // 更新图书副本状态为借阅
            bookCopyMapper.updateBookCopyStatus(bookCopyId, "BORROWED");
            // 创建借阅记录
            BookBorrow bookBorrow = new BookBorrow();
            bookBorrow.setBookCopyId(bookCopyId);
            bookBorrow.setUserId(userId);
            bookBorrow.setStatus("BORROWED");
            bookBorrow.setBorrowDate(new Date());
            // 插入借阅记录
            bookBorrowMapper.insertBookBorrow(bookBorrow);
        } else {
            // 如果用户已预借此书
            // 获取图书副本ID
            Long bookCopyId = bookBorrowMapper.getBookCopyIdByUserIdAndIsbn(userId, isbn);
            // 更新图书副本状态为借阅
            bookCopyMapper.updateBookCopyStatus(bookCopyId, "BORROWED");
            // 更新预借记录为借阅记录
            bookBorrowMapper.updateBookBorrowStatus(userId, bookCopyId, "BORROWED");
        }

        // 计算最晚归还日期（假设借阅期为14天）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        Date dueDate = calendar.getTime();

        return ResultResponse.success("借书成功，最晚归还日期为：" + dueDate);
    }

    // 归还图书
    @Override
    public ResultResponse<String> returnBook(String isbn, Long userId) {

        // 获取图书副本ID
        Long bookCopyId = bookBorrowMapper.getBookCopyIdByUserIdAndIsbn(userId, isbn);

        // 更新图书副本状态为可用
        bookCopyMapper.updateBookCopyStatus(bookCopyId, "AVAILABLE");

        // 更新借阅记录状态为归还
        bookBorrowMapper.updateBookBorrowStatus(userId, bookCopyId, "RETURNED");

        return ResultResponse.success("图书归还成功");
    }

    // 检查用户是否符合借阅条件
    @Override
    public boolean isUserEligibleForBorrowing(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null || !"BORROWER".equals(user.getRole())) {
            return true;
        }
        
        // 检查用户是否有逾期未还的图书
        int overdueCount = bookBorrowMapper.getOverdueBookCount(userId);
        return overdueCount != 0;
    }

    // 检查图书是否可预借
    @Override
    public boolean isBookAvailable(String isbn) {
        BookInfo bookInfo = bookMapper.getBookInfoByIsbn(isbn);
        return bookInfo != null && bookInfo.getAvailableQuantity() > 0;
    }

} 
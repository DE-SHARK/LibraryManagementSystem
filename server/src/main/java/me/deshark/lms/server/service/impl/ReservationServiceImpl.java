package me.deshark.lms.server.service.impl;

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

import java.util.Date;

@Service
public class ReservationServiceImpl implements IReservationService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BookMapper bookMapper;
    
    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    public ResultResponse<String> reserveBook(String isbn, Long userId, Date expectedBorrowDate) {
        // 检查用户是否有效
        if (!isUserEligibleForBorrowing(userId)) {
            return ResultResponse.error(400, "借阅证无效或已过期");
        }
        
        // 检查图书是否存在且可预借
        if (!isBookAvailableForReservation(isbn)) {
            return ResultResponse.error(400, "图书不存在或暂不可预借");
        }
        
        // 获取一个可用的图书副本
        Long bookCopyId = bookBorrowMapper.getAvailableBookCopyId(isbn);
        
        // 创建预借记录
        BookBorrow reservation = new BookBorrow();
        reservation.setBookCopyId(bookCopyId);
        reservation.setUserId(userId);
        reservation.setBorrowDate(new Date());
        reservation.setExpectedBorrowDate(expectedBorrowDate);
        reservation.setStatus("RESERVED");
        
        bookBorrowMapper.insertBookBorrow(reservation);
        
        return ResultResponse.success("图书预借成功");
    }

    @Override
    public boolean isUserEligibleForBorrowing(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null || !"BORROWER".equals(user.getRole())) {
            return false;
        }
        
        // 检查用户是否有逾期未还的图书
        int overdueCount = bookBorrowMapper.getOverdueBookCount(userId);
        return overdueCount == 0;
    }

    @Override
    public boolean isBookAvailableForReservation(String isbn) {
        BookInfo bookInfo = bookMapper.getBookInfoByIsbn(isbn);
        return bookInfo != null && bookInfo.getAvailableQuantity() > 0;
    }
} 
package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.utils.ResultResponse;

import java.util.Date;

public interface IReservationService {
    // 预借图书
    ResultResponse<String> reserveBook(String isbn, Long userId, Date expectedBorrowDate);
    // 借阅图书
    ResultResponse<String> borrowBook(String isbn, Long userId);
    // 归还图书
    ResultResponse<String> returnBook(String isbn, Long userId);
    // 检查用户是否符合借阅条件
    boolean isUserEligibleForBorrowing(Long userId);
    // 检查图书是否可预借
    boolean isBookAvailable(String isbn);
} 
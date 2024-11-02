package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.utils.ResultResponse;

import java.util.Date;

public interface IReservationService {
    // 预借图书
    ResultResponse<String> reserveBook(String isbn, Long userId, Date expectedBorrowDate);
    // 检查用户是否符合借阅条件
    boolean isUserEligibleForBorrowing(Long userId);
    // 检查图书是否可预借
    boolean isBookAvailableForReservation(String isbn);
    // 借阅图书
    ResultResponse<String> borrowBook(String isbn, Long userId);
} 
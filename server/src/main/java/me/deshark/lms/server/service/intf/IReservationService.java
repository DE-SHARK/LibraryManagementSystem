package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.utils.ResultResponse;

import java.util.Date;

public interface IReservationService {
    ResultResponse<String> reserveBook(String isbn, Long userId, Date expectedBorrowDate);
    boolean isUserEligibleForBorrowing(Long userId);
    boolean isBookAvailableForReservation(String isbn);
} 
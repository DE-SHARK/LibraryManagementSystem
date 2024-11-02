package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.dto.LoginUserDetails;
import me.deshark.lms.server.service.intf.IReservationService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    
    @Autowired
    private IReservationService reservationService;
    
    @PostMapping("/reserve")
    public ResultResponse<String> reserveBook(
            @RequestParam String isbn,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expectedBorrowDate) {
        // 从Spring Security上下文中获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();
        // 
        return reservationService.reserveBook(isbn, userId, expectedBorrowDate);
    }

    @PostMapping("/borrow")
    public ResultResponse<String> borrowBook(@RequestParam String isbn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        return reservationService.borrowBook(isbn, userId);
    }

    @PostMapping("/return")
    public ResultResponse<String> returnBook(@RequestParam String isbn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        return reservationService.returnBook(isbn, userId);
    }
} 
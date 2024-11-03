package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.dto.LoginUserDetails;
import me.deshark.lms.server.model.dto.ReservationRequest;
import me.deshark.lms.server.service.intf.IReservationService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    
    @Autowired
    private IReservationService reservationService;
    
    @PostMapping("/reserve")
    public ResultResponse<String> reserveBook(
            @RequestBody ReservationRequest reservationRequest) {
        // 从Spring Security上下文中获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();
        // 调用ReservationService的reserveBook方法  
        return reservationService.reserveBook(reservationRequest.getIsbn(), userId, reservationRequest.getExpectedBorrowDate());
    }

    @PostMapping("/borrow")
    public ResultResponse<String> borrowBook(@RequestBody ReservationRequest reservationRequest) {
        System.out.println(reservationRequest.getIsbn());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        return reservationService.borrowBook(reservationRequest.getIsbn(), userId);
    }

    @PostMapping("/return")
    public ResultResponse<String> returnBook(@RequestBody ReservationRequest reservationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        return reservationService.returnBook(reservationRequest.getIsbn(), userId);
    }

    @PostMapping("/renew")
    public ResultResponse<String> renewBook(@RequestBody ReservationRequest reservationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        return reservationService.renewBook(reservationRequest.getIsbn(), userId);
    }
} 
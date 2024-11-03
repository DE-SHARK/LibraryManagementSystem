package me.deshark.lms.server.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ReservationRequest {
    private String isbn;

    // 预借日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expectedBorrowDate;

}

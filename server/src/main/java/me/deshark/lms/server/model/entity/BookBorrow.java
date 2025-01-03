package me.deshark.lms.server.model.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BookBorrow {
    private Long id;
    private Long bookCopyId;
    private Long userId;
    private Date createDate;
    private String status;  // BORROWED, RETURNED, OVERDUE, RESERVED
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private Date expectedBorrowDate;
}

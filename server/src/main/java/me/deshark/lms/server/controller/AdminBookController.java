package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.service.intf.IBookService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/book")
public class AdminBookController {
    
    @Autowired
    private IBookService bookService;
    
    // 获取所有图书信息
    @GetMapping("/list")
    public ResultResponse<List<BookInfo>> getAllBooks() {
        return ResultResponse.success(bookService.getAllBooks());
    }
    
    // 添加新图书
    @PostMapping("/add")
    public ResultResponse<String> addBook(@RequestBody BookInfo bookInfo) {
        return bookService.addBook(bookInfo);
    }
    
    // 更新图书信息
    @PutMapping("/update")
    public ResultResponse<String> updateBook(@RequestBody BookInfo bookInfo) {
        return bookService.updateBook(bookInfo);
    }
    
    // 删除图书
    @DeleteMapping("/delete/{isbn}")
    public ResultResponse<String> deleteBook(@PathVariable String isbn) {
        return bookService.deleteBook(isbn);
    }
    
    // 获取图书借阅情况
    @GetMapping("/borrow-status/{isbn}")
    public ResultResponse<Map<String, Object>> getBookBorrowStatus(@PathVariable String isbn) {
        return bookService.getBookBorrowStatus(isbn);
    }
} 
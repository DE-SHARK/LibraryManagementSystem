package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.service.intf.IBookService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @GetMapping("/searchByTitle")
    public ResultResponse<List<BookInfo>> searchBooksByTitle(@RequestParam String title) {
        return ResultResponse.success(bookService.findBooksByTitle(title));
    }

    @GetMapping("/searchByAuthor")
    public ResultResponse<List<BookInfo>> searchBooksByAuthor(@RequestParam String author) {
        return ResultResponse.success(bookService.findBooksByAuthor(author));
    }

    @GetMapping("/searchByType")
    public ResultResponse<List<BookInfo>> searchBooksByType(@RequestParam String type) {
        return ResultResponse.success(bookService.findBooksByType(type));
    }

    @GetMapping("/info/{isbn}")
    public ResultResponse<BookInfo> getBookInfo(@PathVariable String isbn) {
        return ResultResponse.success(bookService.getBookInfoByIsbn(isbn));
    }
} 
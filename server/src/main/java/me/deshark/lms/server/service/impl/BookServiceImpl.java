package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.mapper.BookMapper;
import me.deshark.lms.server.mapper.BookBorrowMapper;
import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.service.intf.IBookService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    public List<BookInfo> findBooksByTitle(String title) {
        return bookMapper.findBooksByTitle(title);
    }

    @Override
    public List<BookInfo> findBooksByAuthor(String author) {
        return bookMapper.findBooksByAuthor(author);
    }

    @Override
    public List<BookInfo> findBooksByClcNumber(String clcNumber) {
        return bookMapper.findBooksByClcNumber(clcNumber);
    }

    @Override
    public BookInfo getBookInfoByIsbn(String isbn) {
        return bookMapper.getBookInfoByIsbn(isbn);
    }

    @Override
    public List<BookInfo> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    @Override
    public ResultResponse<String> addBook(BookInfo bookInfo) {
        if (bookMapper.getBookInfoByIsbn(bookInfo.getIsbn()) != null) {
            return ResultResponse.error(400, "该ISBN已存在");
        }
        
        bookMapper.insertBook(bookInfo);
        return ResultResponse.success("添加图书成功");
    }

    @Override
    public ResultResponse<String> updateBook(BookInfo bookInfo) {
        if (bookMapper.getBookInfoByIsbn(bookInfo.getIsbn()) == null) {
            return ResultResponse.error(400, "图书不存在");
        }
        
        bookMapper.updateBook(bookInfo);
        return ResultResponse.success("更新图书成功");
    }

    @Override
    public ResultResponse<String> deleteBook(String isbn) {
        if (bookMapper.getBookInfoByIsbn(isbn) == null) {
            return ResultResponse.error(400, "图书不存在");
        }
        
        if (bookBorrowMapper.getActiveBorrowCount(isbn) > 0) {
            return ResultResponse.error(400, "该图书还有未归还的借阅记录，无法删除");
        }
        
        bookMapper.deleteBook(isbn);
        return ResultResponse.success("删除图书成功");
    }

    @Override
    public ResultResponse<Map<String, Object>> getBookBorrowStatus(String isbn) {
        BookInfo bookInfo = bookMapper.getBookInfoByIsbn(isbn);
        if (bookInfo == null) {
            return ResultResponse.error(400, "图书不存在");
        }
        
        Map<String, Object> status = new HashMap<>();
        status.put("bookInfo", bookInfo);
        status.put("borrowedCount", bookBorrowMapper.getBorrowedCount(isbn));
        status.put("reservedCount", bookBorrowMapper.getReservedCount(isbn));
        status.put("borrowRecords", bookBorrowMapper.getBorrowRecords(isbn));
        
        return ResultResponse.success(status);
    }
} 
package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.utils.ResultResponse;

import java.util.List;
import java.util.Map;

public interface IBookService {
    // 根据书名查找图书
    List<BookInfo> findBooksByTitle(String title);
    // 根据作者查找图书
    List<BookInfo> findBooksByAuthor(String author);
    // 根据CLC号查找图书
    List<BookInfo> findBooksByClcNumber(String clcNumber);
    // 根据ISBN查找图书
    BookInfo getBookInfoByIsbn(String isbn);
    // 获取所有图书
    List<BookInfo> getAllBooks();
    // 添加图书
    ResultResponse<String> addBook(BookInfo bookInfo);
    // 更新图书
    ResultResponse<String> updateBook(BookInfo bookInfo);
    // 删除图书
    ResultResponse<String> deleteBook(String isbn);
    // 获取图书借阅状态
    ResultResponse<Map<String, Object>> getBookBorrowStatus(String isbn);
} 
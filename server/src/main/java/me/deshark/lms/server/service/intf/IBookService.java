package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.model.entity.BookInfo;

import java.util.List;

public interface IBookService {
    List<BookInfo> findBooksByTitle(String title);
    List<BookInfo> findBooksByAuthor(String author);
    List<BookInfo> findBooksByClcNumber(String clcNumber);
    BookInfo getBookInfoByIsbn(String isbn);
} 
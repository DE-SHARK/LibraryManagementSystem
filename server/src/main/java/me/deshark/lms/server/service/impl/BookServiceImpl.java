package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.mapper.BookMapper;
import me.deshark.lms.server.model.entity.BookInfo;
import me.deshark.lms.server.service.intf.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<BookInfo> findBooksByTitle(String title) {
        return bookMapper.findBooksByTitle(title);
    }

    @Override
    public List<BookInfo> findBooksByAuthor(String author) {
        return bookMapper.findBooksByAuthor(author);
    }

    @Override
    public List<BookInfo> findBooksByType(String type) {
        return bookMapper.findBooksByType(type);
    }

    @Override
    public BookInfo getBookInfoByIsbn(String isbn) {
        return bookMapper.getBookInfoByIsbn(isbn);
    }
} 
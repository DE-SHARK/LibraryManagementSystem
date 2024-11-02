package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {

    // 根据标题查找图书
    @Select("SELECT * FROM book_info WHERE title LIKE CONCAT('%', #{title}, '%')")
    List<BookInfo> findBooksByTitle(String title);

    // 根据作者查找图书
    @Select("SELECT * FROM book_info WHERE author LIKE CONCAT('%', #{author}, '%')")
    List<BookInfo> findBooksByAuthor(String author);

    // 根据CLC号查找图书
    @Select("SELECT * FROM book_info WHERE clc_number LIKE CONCAT(#{clcNumber}, '%')")
    List<BookInfo> findBooksByClcNumber(String clcNumber);

    // 根据ISBN查找图书
    @Select("SELECT * FROM book_info WHERE isbn = #{isbn}")
    BookInfo getBookInfoByIsbn(String isbn);
} 
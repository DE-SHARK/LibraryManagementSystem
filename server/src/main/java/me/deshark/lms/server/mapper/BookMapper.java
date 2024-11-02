package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("SELECT * FROM book_info WHERE title LIKE CONCAT('%', #{title}, '%')")
    List<BookInfo> findBooksByTitle(String title);

    @Select("SELECT * FROM book_info WHERE author LIKE CONCAT('%', #{author}, '%')")
    List<BookInfo> findBooksByAuthor(String author);

    @Select("SELECT * FROM book_info WHERE type = #{type}")
    List<BookInfo> findBooksByType(String type);

    @Select("SELECT * FROM book_info WHERE isbn = #{isbn}")
    BookInfo getBookInfoByIsbn(String isbn);
} 
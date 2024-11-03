package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    // 获取所有图书
    @Select("SELECT * FROM book_info")
    List<BookInfo> getAllBooks();

    // 添加图书
    @Insert("INSERT INTO book_info(isbn, title, author, clc_number, location, total_quantity, available_quantity) " +
            "VALUES(#{isbn}, #{title}, #{author}, #{clcNumber}, #{location}, #{totalQuantity}, #{availableQuantity})")
    void insertBook(BookInfo bookInfo);

    // 更新图书
    @Update("UPDATE book_info SET title=#{title}, author=#{author}, clc_number=#{clcNumber}, " +
            "location=#{location}, total_quantity=#{totalQuantity}, available_quantity=#{availableQuantity} " +
            "WHERE isbn=#{isbn}")
    void updateBook(BookInfo bookInfo);

    // 删除图书
    @Delete("DELETE FROM book_info WHERE isbn=#{isbn}")
    void deleteBook(String isbn);
} 
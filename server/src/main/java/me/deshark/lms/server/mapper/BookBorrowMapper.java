package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookBorrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BookBorrowMapper {
    
    @Insert("INSERT INTO book_borrow(book_copy_id, user_id, borrow_date, expected_borrow_date, status) " +
            "VALUES(#{bookCopyId}, #{userId}, #{borrowDate}, #{expectedBorrowDate}, #{status})")
    int insertBookBorrow(BookBorrow bookBorrow);
    
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'OVERDUE'")
    int getOverdueBookCount(Long userId);
    
    @Select("SELECT id FROM book_copy WHERE isbn = #{isbn} AND status = 'AVAILABLE' LIMIT 1")
    Long getAvailableBookCopyId(String isbn);
} 
package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookBorrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookBorrowMapper {

    // 插入借阅记录
    @Insert("INSERT INTO book_borrow(book_copy_id, user_id, borrow_date, expected_borrow_date, status) " +
            "VALUES(#{bookCopyId}, #{userId}, #{borrowDate}, #{expectedBorrowDate}, #{status})")
    void insertBookBorrow(BookBorrow bookBorrow);

    // 获取逾期未还的图书数量
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'OVERDUE'")
    int getOverdueBookCount(Long userId);

    // 获取可用的图书副本ID
    @Select("SELECT id FROM book_copy WHERE isbn = #{isbn} AND status = 'AVAILABLE' LIMIT 1")
    Long getAvailableBookCopyId(String isbn);

    // 获取用户预借的图书数量
    @Select("SELECT COUNT(*) FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bb.user_id = #{userId} AND bc.isbn = #{isbn} AND bb.status = 'RESERVED'")
    int getReservedBookCount(Long userId, String isbn);

    // 获取用户已借的图书数量
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'BORROWED'")
    int getBorrowedBookCount(Long userId);

    // 获取用户已借的图书数量
    @Select("SELECT COUNT(*) FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bb.user_id = #{userId} AND bc.isbn = #{isbn} AND bb.status = 'BORROWED'")
    int getBorrowedBookCountByIsbn(Long userId, String isbn);

    // 检查图书是否过期
    @Select("SELECT COUNT(*) > 0 FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bb.user_id = #{userId} AND bc.isbn = #{isbn} AND bb.status = 'BORROWED' " +
            "AND bb.expected_borrow_date < NOW()")
    boolean isBookOverdue(Long userId, String isbn);

    // 更新借阅记录为已归还
    @Update("UPDATE book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "SET bb.status = #{status} " +
            "WHERE bb.user_id = #{userId} AND bc.isbn = #{isbn} AND bb.status = 'BORROWED'")
    void updateBookBorrowStatus(Long userId, String isbn, String status);
} 
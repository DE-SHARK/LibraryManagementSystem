package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.BookBorrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface BookBorrowMapper {

    // 获取用户的活跃借阅数量（包括已借和预约）
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status IN ('BORROWED', 'RESERVED')")
    int getUserActiveBorrowCount(Long userId);

    // 获取图书的活跃借阅数量（包括已借和预约）
    @Select("SELECT COUNT(*) FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bc.isbn = #{isbn} AND bb.status IN ('BORROWED', 'RESERVED')")
    int getBookActiveBorrowCount(String isbn);

    // 获取用户逾期未还的图书数量
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'BORROWED'")
    int getOverdueBookCount(Long userId);

    // 获取用户预借此书的数量
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'RESERVED'")
    int getReservedBookCountByIsbn(Long userId, String isbn);

    // 获取用户已借此书的数量
    @Select("SELECT COUNT(*) FROM book_borrow WHERE user_id = #{userId} AND status = 'BORROWED'")
    int getBorrowedBookCountByIsbn(Long userId, String isbn);

    // 插入记录
    @Insert("INSERT INTO book_borrow (book_copy_id, user_id, status, borrow_date, expected_borrow_date) VALUES (#{bookCopyId}, #{userId}, #{status}, #{borrowDate}, #{expectedBorrowDate})")
    void insertBookBorrow(BookBorrow bookBorrow);

    // 获取图书副本ID
    @Select("SELECT book_copy_id FROM book_borrow WHERE user_id = #{userId} AND book_copy_id IN (SELECT id FROM book_copy WHERE isbn = #{isbn})")
    Long getBookCopyIdByUserIdAndIsbn(Long userId, String isbn);

    // 更新借阅状态
    @Update("UPDATE book_borrow SET status = #{status} WHERE user_id = #{userId} AND book_copy_id = #{bookCopyId}")
    void updateBookBorrowStatus(Long userId, Long bookCopyId, String status);

    // 获取借阅日期
    @Select("SELECT borrow_date FROM book_borrow WHERE user_id = #{userId} AND book_copy_id = #{bookCopyId}")
    Date getBookBorrowBorrowDate(Long userId, Long bookCopyId);

    // 更新借阅日期
    @Update("UPDATE book_borrow SET borrow_date = #{newBorrowDate} WHERE user_id = #{userId} AND book_copy_id = #{bookCopyId}") 
    void updateBookBorrowBorrowDate(Long userId, Long bookCopyId, Date newBorrowDate);

    // 获取图书的已借阅数量
    @Select("SELECT COUNT(*) FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bc.isbn = #{isbn} AND bb.status = 'BORROWED'")
    int getBorrowedCount(String isbn);

    // 获取图书的已预约数量
    @Select("SELECT COUNT(*) FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "WHERE bc.isbn = #{isbn} AND bb.status = 'RESERVED'")
    int getReservedCount(String isbn);

    // 获取图书的借阅记录
    @Select("SELECT bb.*, u.username FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "JOIN user u ON bb.user_id = u.id " +
            "WHERE bc.isbn = #{isbn} ORDER BY bb.create_date DESC")
    List<Map<String, Object>> getBorrowRecords(String isbn);

    // 获取用户借阅历史
    @Select("SELECT bb.*, b.title, b.author FROM book_borrow bb " +
            "JOIN book_copy bc ON bb.book_copy_id = bc.id " +
            "JOIN book_info b ON bc.isbn = b.isbn " +
            "WHERE bb.user_id = #{userId} " +
            "ORDER BY bb.create_date DESC")
    List<Map<String, Object>> getUserBorrowHistory(Long userId);
}
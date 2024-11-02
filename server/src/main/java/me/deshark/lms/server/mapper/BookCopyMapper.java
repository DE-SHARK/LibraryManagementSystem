package me.deshark.lms.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookCopyMapper {

    // 获取一个可用的图书副本ID
    @Select("SELECT id FROM book_copy WHERE isbn = #{isbn} AND status = 'AVAILABLE' LIMIT 1")
    Long getAvailableBookCopyId(String isbn);

    // 更新图书副本状态
    @Update("UPDATE book_copy SET status = #{status} WHERE id = #{bookCopyId}")
    void updateBookCopyStatus(Long bookCopyId, String status);
}

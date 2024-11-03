package me.deshark.lms.server.mapper;

import me.deshark.lms.server.model.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper()
public interface UserMapper {

    // 根据ID获取用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(Long id);

    // 根据用户名获取用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);

    // 插入用户
    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    int insertUser(User user);

    // 获取所有借阅者
    @Select("SELECT * FROM user WHERE role = 'BORROWER'")
    List<User> getAllBorrowers();

    // 更新用户
    @Update("UPDATE user SET username = #{username}, password = #{password} WHERE id = #{id}")
    void updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(Long id);

    // 获取所有图书管理员信息
    @Select("SELECT * FROM user WHERE role = 'LIBRARIAN'")
    List<User> getAllLibrarians();
}

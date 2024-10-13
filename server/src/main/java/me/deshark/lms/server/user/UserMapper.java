package me.deshark.lms.server.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper()
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);

    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    int insertUser(User user);
}

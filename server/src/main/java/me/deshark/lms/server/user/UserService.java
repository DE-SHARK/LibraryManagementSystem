package me.deshark.lms.server.user;

public interface UserService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean registerUser(User user);
}

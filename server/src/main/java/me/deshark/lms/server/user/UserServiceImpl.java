package me.deshark.lms.server.user;

import me.deshark.lms.server.user.exceptions.UserExitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new NullPointerException("Username is empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new NullPointerException("Password is empty");
        }
        if (user.getUsername().contains(" ")) {
            throw new NullPointerException("Username contains spaces");
        }
        if (user.getPassword().contains(" ")) {
            throw new NullPointerException("Password contains spaces");
        }

        if (getUserByUsername(user.getUsername()) != null) {
            throw new UserExitedException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userMapper.insertUser(user) > 0;

    }
}

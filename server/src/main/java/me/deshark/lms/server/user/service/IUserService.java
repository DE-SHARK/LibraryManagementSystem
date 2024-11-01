package me.deshark.lms.server.user.service;

import me.deshark.lms.server.user.ResultResponse;
import me.deshark.lms.server.user.User;

public interface IUserService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean registerUser(User user);

    ResultResponse<String> loginUser(User user);
}

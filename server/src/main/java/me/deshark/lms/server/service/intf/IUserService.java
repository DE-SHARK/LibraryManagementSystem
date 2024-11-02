package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;

public interface IUserService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean registerUser(User user);

    ResultResponse<String> loginUser(User user);
}

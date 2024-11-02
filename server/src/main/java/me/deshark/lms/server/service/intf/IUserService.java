package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;

public interface IUserService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    ResultResponse<String> registerUser(AuthRequest authRequest);

    ResultResponse<String> loginUser(AuthRequest authRequest);
}

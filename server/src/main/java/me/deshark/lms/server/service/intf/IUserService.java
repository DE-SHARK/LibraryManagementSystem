package me.deshark.lms.server.service.intf;

import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    ResultResponse<String> registerUser(AuthRequest authRequest);

    ResultResponse<String> loginUser(AuthRequest authRequest);

    List<User> getAllBorrowers();

    ResultResponse<String> addBorrower(AuthRequest authRequest);

    ResultResponse<String> updateBorrower(Long id, User user);

    ResultResponse<String> deleteBorrower(Long id);

    ResultResponse<List<Map<String, Object>>> getBorrowerHistory(Long id);

    List<User> getAllLibrarians();

    ResultResponse<String> addLibrarian(AuthRequest authRequest);

    ResultResponse<String> updateLibrarian(Long id, User user);

    ResultResponse<String> deleteLibrarian(Long id);
}

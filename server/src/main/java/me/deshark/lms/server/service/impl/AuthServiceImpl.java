package me.deshark.lms.server.service.impl;

import me.deshark.lms.server.mapper.BookBorrowMapper;
import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.utils.ResultResponse;
import me.deshark.lms.server.model.entity.User;
import me.deshark.lms.server.mapper.UserMapper;
import me.deshark.lms.server.exception.UserExitedException;
import me.deshark.lms.server.service.intf.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.deshark.lms.server.utils.JwtUtil;
import org.springframework.security.core.AuthenticationException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public ResultResponse<String> registerUser(AuthRequest authRequest) {

        if (getUserByUsername(authRequest.getUsername()) != null) {
            throw new UserExitedException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setRole("BORROWER");
        userMapper.insertUser(user);
        return ResultResponse.success();

    }

    @Override
    public ResultResponse<Map<String, String>> loginUser(AuthRequest authRequest) {
        try {
            // 验证用户名密码
            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(authRequest.getUsername(), authRequest.getPassword());
            Authentication authenticationResponse =
                    authenticationManager.authenticate(authenticationRequest);
                    
            // 获取用户信息
            User user = userMapper.getUserByUsername(authRequest.getUsername());
            
            // 生成JWT token
            String token = jwtUtil.generateToken(authRequest.getUsername());
            
            // 构建返回数据
            Map<String, String> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("role", user.getRole());
            
            return ResultResponse.success(responseData);
        } catch (AuthenticationException e) {
            return ResultResponse.error(401, "用户名或密码错误");
        }
    }

    // 获取所有借阅者
    @Override
    public List<User> getAllBorrowers() {
        return userMapper.getAllBorrowers();
    }

    // 添加借阅者
    @Override
    public ResultResponse<String> addBorrower(AuthRequest authRequest) {
        if (getUserByUsername(authRequest.getUsername()) != null) {
            return ResultResponse.error(400, "用户名已存在");
        }
        
        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setRole("BORROWER");
        userMapper.insertUser(user);
        return ResultResponse.success("添加借阅者成功");
    }

    // 更新借阅者信息
    @Override
    public ResultResponse<String> updateBorrower(Long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser == null) {
            return ResultResponse.error(400, "用户不存在");
        }
        if (!"BORROWER".equals(existingUser.getRole())) {
            return ResultResponse.error(400, "只能修改借阅者信息");
        }
        
        user.setId(id);
        user.setRole("BORROWER");
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.updateUser(user);
        return ResultResponse.success("更新借阅者信息成功");
    }

    // 删除借阅者
    @Override
    public ResultResponse<String> deleteBorrower(Long id) {
        User user = getUserById(id);
        if (user == null) {
            return ResultResponse.error(400, "用户不存在");
        }
        if (!"BORROWER".equals(user.getRole())) {
            return ResultResponse.error(400, "只能删除借阅者");
        }
        
        // 检查是否有未归还的图书
        if (bookBorrowMapper.getUserActiveBorrowCount(id) > 0) {
            return ResultResponse.error(400, "该借阅者还有未归还的图书，无法删除");
        }
        
        userMapper.deleteUser(id);
        return ResultResponse.success("删除借阅者成功");
    }

    // 获取借阅者借阅历史
    @Override
    public ResultResponse<List<Map<String, Object>>> getBorrowerHistory(Long id) {
        User user = getUserById(id);
        if (user == null) {
            return ResultResponse.error(400, "用户不存在");
        }
        
        List<Map<String, Object>> history = bookBorrowMapper.getUserBorrowHistory(id);
        return ResultResponse.success(history);
    }

    @Override
    public List<User> getAllLibrarians() {
        return userMapper.getAllLibrarians();
    }

    @Override
    public ResultResponse<String> addLibrarian(AuthRequest authRequest) {
        if (getUserByUsername(authRequest.getUsername()) != null) {
            return ResultResponse.error(400, "用户名已存在");
        }
        
        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setRole("LIBRARIAN");
        userMapper.insertLibrarian(user);
        
        return ResultResponse.success("添加图书管理员成功");
    }

    @Override
    public ResultResponse<String> updateLibrarian(Long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser == null) {
            return ResultResponse.error(400, "用户不存在");
        }
        if (!"LIBRARIAN".equals(existingUser.getRole())) {
            return ResultResponse.error(400, "只能修改图书管理员信息");
        }
        
        // 如果修改了用户名，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            getUserByUsername(user.getUsername()) != null) {
            return ResultResponse.error(400, "用户名已存在");
        }
        
        user.setId(id);
        user.setRole("LIBRARIAN");
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        
        userMapper.updateUser(user);
        return ResultResponse.success("更新图书管理员信息成功");
    }

    @Override
    public ResultResponse<String> deleteLibrarian(Long id) {
        User user = getUserById(id);
        if (user == null) {
            return ResultResponse.error(400, "用户不存在");
        }
        if (!"LIBRARIAN".equals(user.getRole())) {
            return ResultResponse.error(400, "只能删除图书管理员");
        }
        
        userMapper.deleteUser(id);
        return ResultResponse.success("删除图书管理员成功");
    }
}

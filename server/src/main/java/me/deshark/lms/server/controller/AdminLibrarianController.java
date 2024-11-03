package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.model.entity.User;
import me.deshark.lms.server.service.intf.IUserService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/librarian")
public class AdminLibrarianController {
    
    @Autowired
    private IUserService userService;
    
    // 获取所有图书管理员信息
    @GetMapping("/list")
    public ResultResponse<List<User>> getAllLibrarians() {
        return ResultResponse.success(userService.getAllLibrarians());
    }
    
    // 添加新图书管理员
    @PostMapping("/add")
    public ResultResponse<String> addLibrarian(@RequestBody @Validated AuthRequest authRequest) {
        return userService.addLibrarian(authRequest);
    }
    
    // 更新图书管理员信息
    @PutMapping("/update/{id}")
    public ResultResponse<String> updateLibrarian(@PathVariable Long id, @RequestBody User user) {
        return userService.updateLibrarian(id, user);
    }
    
    // 删除图书管理员
    @DeleteMapping("/delete/{id}")
    public ResultResponse<String> deleteLibrarian(@PathVariable Long id) {
        return userService.deleteLibrarian(id);
    }
} 
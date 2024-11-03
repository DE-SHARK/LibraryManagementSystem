package me.deshark.lms.server.controller;

import me.deshark.lms.server.model.dto.AuthRequest;
import me.deshark.lms.server.model.entity.User;
import me.deshark.lms.server.service.intf.IUserService;
import me.deshark.lms.server.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private IUserService userService;

    // 获取所有借阅者信息
    @GetMapping("/borrowers")
    public ResultResponse<List<User>> getAllBorrowers() {
        return ResultResponse.success(userService.getAllBorrowers());
    }

    // 添加新借阅者
    @PostMapping("/borrower")
    public ResultResponse<String> addBorrower(@RequestBody @Validated AuthRequest authRequest) {
        return userService.addBorrower(authRequest);
    }

    // 更新借阅者信息
    @PutMapping("/borrower/{id}")
    public ResultResponse<String> updateBorrower(@PathVariable Long id, @RequestBody User user) {
        return userService.updateBorrower(id, user);
    }

    // 删除借阅者
    @DeleteMapping("/borrower/{id}")
    public ResultResponse<String> deleteBorrower(@PathVariable Long id) {
        return userService.deleteBorrower(id);
    }

    // 获取借阅者借阅历史
    @GetMapping("/borrower/{id}/history")
    public ResultResponse<List<Map<String, Object>>> getBorrowerHistory(@PathVariable Long id) {
        return userService.getBorrowerHistory(id);
    }
} 
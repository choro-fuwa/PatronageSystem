package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.entity.User;
import com.PatronageSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            return Result.success(loginUser);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
    
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        if (registeredUser != null) {
            return Result.success(registeredUser);
        } else {
            return Result.error("用户名已存在");
        }
    }
    
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }
    
    @GetMapping("/list")
    public Result<List<User>> findAll() {
        List<User> users = userService.findAll();
        return Result.success(users);
    }
    
    @PutMapping
    public Result<Boolean> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("更新失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除失败");
        }
    }
} 
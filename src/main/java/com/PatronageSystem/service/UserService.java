package com.PatronageSystem.service;

import com.PatronageSystem.entity.User;
import java.util.List;

public interface UserService {
    
    User login(String username, String password);
    
    User register(User user);
    
    User findById(Long id);
    
    List<User> findAll();
    
    boolean updateUser(User user);
    
    boolean deleteUser(Long id);
} 
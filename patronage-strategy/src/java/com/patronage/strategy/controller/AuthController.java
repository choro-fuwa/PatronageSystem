package src.java.com.patronage.strategy.controller;

import src.java.com.patronage.strategy.common.Result;
import src.java.com.patronage.strategy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // 这里应该从数据库验证用户信息
        // 暂时使用硬编码的用户名密码进行演示
        if ("admin".equals(username) && "123456".equals(password)) {
            String token = jwtUtils.generateToken(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("expiresIn", 24 * 60 * 60); // 24小时
            
            return Result.success("登录成功", response);
        } else {
            return Result.error(401, "用户名或密码错误");
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 这里可以实现token黑名单等逻辑
        return Result.success();
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh")
    public Result<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // 去掉Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String newToken = jwtUtils.refreshToken(token);
            String username = jwtUtils.getUsernameFromToken(newToken);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("username", username);
            response.put("expiresIn", 24 * 60 * 60);
            
            return Result.success("Token刷新成功", response);
        } catch (Exception e) {
            return Result.error(401, "Token刷新失败");
        }
    }

    /**
     * 验证token
     */
    @GetMapping("/verify")
    public Result<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            // 去掉Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String username = jwtUtils.getUsernameFromToken(token);
            boolean isValid = jwtUtils.validateToken(token, username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            response.put("username", username);
            
            return Result.success("Token验证完成", response);
        } catch (Exception e) {
            return Result.error(401, "Token验证失败");
        }
    }
}
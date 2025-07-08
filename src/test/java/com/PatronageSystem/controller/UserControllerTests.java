package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.controller.UserController;
import com.PatronageSystem.entity.User;
import com.PatronageSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 修复序列化问题

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setRealName("Test User");
        testUser.setStatus(1);
        // 移除时间字段，它们不是测试的重点
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(testUser);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testUser"));
    }

    @Test
    void testLoginFailure() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        when(userService.register(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testRegisterFailure() throws Exception {
        when(userService.register(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        when(userService.findById(anyLong())).thenReturn(testUser);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testFindUserByIdFailure() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].username").value("testUser"));
    }

    @Test
    void testFindAllUsersEmpty() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        when(userService.updateUser(any(User.class))).thenReturn(true);

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testUpdateUserFailure() throws Exception {
        when(userService.updateUser(any(User.class))).thenReturn(false);

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("更新失败"));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testDeleteUserFailure() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/user/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("删除失败"));
    }
}
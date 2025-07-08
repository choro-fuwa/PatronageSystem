package com.PatronageSystem.service;

import com.PatronageSystem.entity.User;
import com.PatronageSystem.mapper.UserMapper;
import com.PatronageSystem.service.UserService;
import com.PatronageSystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testuser1");
        testUser1.setPassword("password123");
        testUser1.setEmail("test1@example.com");
        testUser1.setPhone("13800138001");
        testUser1.setRealName("测试用户1");
        testUser1.setStatus(1);
        testUser1.setCreateTime(LocalDateTime.now());
        testUser1.setUpdateTime(LocalDateTime.now());

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testuser2");
        testUser2.setPassword("password456");
        testUser2.setEmail("test2@example.com");
        testUser2.setPhone("13800138002");
        testUser2.setRealName("测试用户2");
        testUser2.setStatus(1);
        testUser2.setCreateTime(LocalDateTime.now().minusDays(1));
        testUser2.setUpdateTime(LocalDateTime.now().minusDays(1));

        testUser3 = new User();
        testUser3.setId(3L);
        testUser3.setUsername("testuser3");
        testUser3.setPassword("password789");
        testUser3.setEmail("test3@example.com");
        testUser3.setPhone("13800138003");
        testUser3.setRealName("测试用户3");
        testUser3.setStatus(0); // 禁用状态
        testUser3.setCreateTime(LocalDateTime.now().minusDays(2));
        testUser3.setUpdateTime(LocalDateTime.now().minusDays(2));
    }

    @Test
    void testLogin_Success() {
        // Given
        String username = "testuser1";
        String password = "password123";
        when(userMapper.login(username, password)).thenReturn(testUser1);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNotNull(loginUser);
        assertEquals(testUser1, loginUser);
        assertEquals(username, loginUser.getUsername());
        assertEquals(password, loginUser.getPassword());
        assertEquals(1, loginUser.getStatus());
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Given
        String username = "invaliduser";
        String password = "wrongpassword";
        when(userMapper.login(username, password)).thenReturn(null);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_DisabledUser() {
        // Given
        String username = "testuser3";
        String password = "password789";
        when(userMapper.login(username, password)).thenReturn(null); // 禁用用户无法登录

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_NullUsername() {
        // Given
        String username = null;
        String password = "password123";
        when(userMapper.login(username, password)).thenReturn(null);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_NullPassword() {
        // Given
        String username = "testuser1";
        String password = null;
        when(userMapper.login(username, password)).thenReturn(null);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_EmptyUsername() {
        // Given
        String username = "";
        String password = "password123";
        when(userMapper.login(username, password)).thenReturn(null);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testLogin_EmptyPassword() {
        // Given
        String username = "testuser1";
        String password = "";
        when(userMapper.login(username, password)).thenReturn(null);

        // When
        User loginUser = userService.login(username, password);

        // Then
        assertNull(loginUser);
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testRegister_Success() {
        // Given
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");
        newUser.setEmail("newuser@example.com");
        newUser.setPhone("13800138004");
        newUser.setRealName("新用户");

        when(userMapper.findByUsername("newuser")).thenReturn(null);
        when(userMapper.insert(newUser)).thenReturn(1);

        // When
        User registeredUser = userService.register(newUser);

        // Then
        assertNotNull(registeredUser);
        assertEquals(newUser, registeredUser);
        assertEquals(1, registeredUser.getStatus()); // 默认状态为1
        assertEquals("newuser", registeredUser.getUsername());
        verify(userMapper, times(1)).findByUsername("newuser");
        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Given
        User newUser = new User();
        newUser.setUsername("testuser1");
        newUser.setPassword("newpassword");
        newUser.setEmail("newuser@example.com");
        newUser.setPhone("13800138004");
        newUser.setRealName("新用户");

        when(userMapper.findByUsername("testuser1")).thenReturn(testUser1);

        // When
        User registeredUser = userService.register(newUser);

        // Then
        assertNull(registeredUser);
        verify(userMapper, times(1)).findByUsername("testuser1");
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testRegister_WithNullValues() {
        // Given
        User newUser = new User();
        newUser.setUsername("nulluser");
        newUser.setPassword("password");
        // 其他字段为null

        when(userMapper.findByUsername("nulluser")).thenReturn(null);
        when(userMapper.insert(newUser)).thenReturn(1);

        // When
        User registeredUser = userService.register(newUser);

        // Then
        assertNotNull(registeredUser);
        assertEquals(newUser, registeredUser);
        assertEquals(1, registeredUser.getStatus());
        verify(userMapper, times(1)).findByUsername("nulluser");
        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    void testRegister_NullUsername() {
        // Given
        User newUser = new User();
        newUser.setUsername(null);
        newUser.setPassword("password");

        when(userMapper.findByUsername(null)).thenReturn(null);
        when(userMapper.insert(newUser)).thenReturn(1);

        // When
        User registeredUser = userService.register(newUser);

        // Then
        assertNotNull(registeredUser);
        assertEquals(newUser, registeredUser);
        assertEquals(1, registeredUser.getStatus());
        verify(userMapper, times(1)).findByUsername(null);
        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    void testFindById_Success() {
        // Given
        Long userId = 1L;
        when(userMapper.findById(userId)).thenReturn(testUser1);

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser1, foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userMapper, times(1)).findById(userId);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        Long userId = 999L;
        when(userMapper.findById(userId)).thenReturn(null);

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertNull(foundUser);
        verify(userMapper, times(1)).findById(userId);
    }

    @Test
    void testFindById_NullId() {
        // Given
        when(userMapper.findById(null)).thenReturn(null);

        // When
        User foundUser = userService.findById(null);

        // Then
        assertNull(foundUser);
        verify(userMapper, times(1)).findById(null);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<User> expectedUsers = Arrays.asList(testUser1, testUser2);
        when(userMapper.findAll()).thenReturn(expectedUsers);

        // When
        List<User> actualUsers = userService.findAll();

        // Then
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(userMapper, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(userMapper.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> actualUsers = userService.findAll();

        // Then
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
        verify(userMapper, times(1)).findAll();
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setUsername("testuser1");
        updateUser.setPassword("newpassword");
        updateUser.setEmail("updated@example.com");
        updateUser.setPhone("13800138005");
        updateUser.setRealName("更新后的用户");
        updateUser.setStatus(1);

        when(userMapper.update(updateUser)).thenReturn(1);

        // When
        boolean result = userService.updateUser(updateUser);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).update(updateUser);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Given
        User updateUser = new User();
        updateUser.setId(999L);
        updateUser.setUsername("nonexistent");
        updateUser.setPassword("password");
        updateUser.setEmail("nonexistent@example.com");
        updateUser.setPhone("13800138006");
        updateUser.setRealName("不存在的用户");
        updateUser.setStatus(1);

        when(userMapper.update(updateUser)).thenReturn(0);

        // When
        boolean result = userService.updateUser(updateUser);

        // Then
        assertFalse(result);
        verify(userMapper, times(1)).update(updateUser);
    }

    @Test
    void testUpdateUser_WithNullValues() {
        // Given
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setUsername("testuser1");
        updateUser.setPassword("password");
        // 其他字段为null

        when(userMapper.update(updateUser)).thenReturn(1);

        // When
        boolean result = userService.updateUser(updateUser);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).update(updateUser);
    }

    @Test
    void testUpdateUser_NullUser() {
        // Given
        when(userMapper.update(null)).thenReturn(0);

        // When
        boolean result = userService.updateUser(null);

        // Then
        assertFalse(result);
        verify(userMapper, times(1)).update(null);
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        Long userId = 1L;
        when(userMapper.deleteById(userId)).thenReturn(1);

        // When
        boolean result = userService.deleteUser(userId);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Given
        Long userId = 999L;
        when(userMapper.deleteById(userId)).thenReturn(0);

        // When
        boolean result = userService.deleteUser(userId);

        // Then
        assertFalse(result);
        verify(userMapper, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NullId() {
        // Given
        when(userMapper.deleteById(null)).thenReturn(0);

        // When
        boolean result = userService.deleteUser(null);

        // Then
        assertFalse(result);
        verify(userMapper, times(1)).deleteById(null);
    }

    @Test
    void testUserDataIntegrity() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("integritytest");
        user.setPassword("password123");
        user.setEmail("integrity@example.com");
        user.setPhone("13800138007");
        user.setRealName("完整性测试用户");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // When & Then
        assertNotNull(user.getId());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertNotNull(user.getStatus());
        assertNotNull(user.getCreateTime());
        assertNotNull(user.getUpdateTime());

        // 验证用户名不为空
        assertFalse(user.getUsername().trim().isEmpty());
        
        // 验证密码不为空
        assertFalse(user.getPassword().trim().isEmpty());
        
        // 验证状态值
        assertTrue(user.getStatus() == 0 || user.getStatus() == 1);
        
        // 验证邮箱格式（简单验证）
        if (user.getEmail() != null) {
            assertTrue(user.getEmail().contains("@"));
        }
        
        // 验证手机号格式（简单验证）
        if (user.getPhone() != null) {
            assertTrue(user.getPhone().length() >= 11);
        }
    }

    @Test
    void testUserStatusManagement() {
        // Given
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername("activeuser");
        activeUser.setPassword("password");
        activeUser.setStatus(1);

        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setPassword("password");
        inactiveUser.setStatus(0);

        // When & Then
        assertEquals(1, activeUser.getStatus());
        assertEquals(0, inactiveUser.getStatus());
        
        // 验证状态转换
        activeUser.setStatus(0);
        assertEquals(0, activeUser.getStatus());
        
        inactiveUser.setStatus(1);
        assertEquals(1, inactiveUser.getStatus());
    }

    @Test
    void testUserRegistrationFlow() {
        // Given
        User newUser = new User();
        newUser.setUsername("flowtest");
        newUser.setPassword("password123");
        newUser.setEmail("flow@example.com");
        newUser.setPhone("13800138008");
        newUser.setRealName("流程测试用户");

        when(userMapper.findByUsername("flowtest")).thenReturn(null);
        when(userMapper.insert(newUser)).thenReturn(1);

        // When
        User registeredUser = userService.register(newUser);

        // Then
        assertNotNull(registeredUser);
        assertEquals(1, registeredUser.getStatus());
        assertEquals("flowtest", registeredUser.getUsername());
        verify(userMapper, times(1)).findByUsername("flowtest");
        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    void testUserLoginFlow() {
        // Given
        String username = "logintest";
        String password = "password123";
        
        User loginUser = new User();
        loginUser.setId(1L);
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        loginUser.setStatus(1);

        when(userMapper.login(username, password)).thenReturn(loginUser);

        // When
        User result = userService.login(username, password);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(1, result.getStatus());
        verify(userMapper, times(1)).login(username, password);
    }

    @Test
    void testUserUpdateFlow() {
        // Given
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setUsername("updateuser");
        originalUser.setPassword("oldpassword");
        originalUser.setEmail("old@example.com");
        originalUser.setStatus(1);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateuser");
        updatedUser.setPassword("newpassword");
        updatedUser.setEmail("new@example.com");
        updatedUser.setStatus(1);

        when(userMapper.update(updatedUser)).thenReturn(1);

        // When
        boolean result = userService.updateUser(updatedUser);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).update(updatedUser);
    }

    @Test
    void testUserDeletionFlow() {
        // Given
        Long userId = 1L;
        when(userMapper.deleteById(userId)).thenReturn(1);

        // When
        boolean result = userService.deleteUser(userId);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).deleteById(userId);
    }

    @Test
    void testFindByUsername_Exists() {
        // Given
        String username = "testuser1";
        when(userMapper.findByUsername(username)).thenReturn(testUser1);

        // When
        User foundUser = userMapper.findByUsername(username);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser1, foundUser);
        assertEquals(username, foundUser.getUsername());
        verify(userMapper, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_NotExists() {
        // Given
        String username = "nonexistent";
        when(userMapper.findByUsername(username)).thenReturn(null);

        // When
        User foundUser = userMapper.findByUsername(username);

        // Then
        assertNull(foundUser);
        verify(userMapper, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_NullUsername() {
        // Given
        when(userMapper.findByUsername(null)).thenReturn(null);

        // When
        User foundUser = userMapper.findByUsername(null);

        // Then
        assertNull(foundUser);
        verify(userMapper, times(1)).findByUsername(null);
    }
}

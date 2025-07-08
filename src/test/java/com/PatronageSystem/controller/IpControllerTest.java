package com.PatronageSystem.controller;

import com.PatronageSystem.domain.IpInfo;
import com.PatronageSystem.domain.vo.ResultVO;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.DateUtil;
import com.PatronageSystem.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IpControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IpInfoService ipInfoService;

    @Autowired
    private RedisUtils redisUtils;

    @BeforeEach
    void setUp() {
        // 清理Redis数据
        redisUtils.del("ipInfo");
        // 添加一些测试数据到Redis
        ipInfoService.saveRedis("192.168.1.1", "Chrome");
        ipInfoService.saveRedis("192.168.1.2", "Firefox");
    }

    @Test
    void testGetToday() {
        ResponseEntity<ResultVO> response = restTemplate.getForEntity("/api/today", ResultVO.class);
        
        assertNotNull(response.getBody(), "响应体不应为null");
        assertEquals(200, response.getBody().getCode(), "状态码应该是200");
        assertNotNull(response.getBody().getData(), "数据不应为null");
        
        // 验证返回的数据包含我们在setUp中添加的测试数据
        List<?> data = (List<?>) response.getBody().getData();
        assertEquals(2, data.size(), "应该返回2条测试数据");
    }

    @Test
    void testGetYesterday() {
        ResponseEntity<ResultVO> response = restTemplate.getForEntity("/api/yesterday", ResultVO.class);
        
        assertNotNull(response.getBody(), "响应体不应为null");
        assertEquals(200, response.getBody().getCode(), "状态码应该是200");
        
        // 验证返回的数据是昨天的数据
        List<?> data = (List<?>) response.getBody().getData();
        assertTrue(data.size() >= 1, "至少应该返回1条昨天的数据");
    }

    @Test
    void testGetTotal() {
        ResponseEntity<ResultVO> response = restTemplate.getForEntity("/api/total", ResultVO.class);
        
        assertNotNull(response.getBody(), "响应体不应为null");
        assertEquals(200, response.getBody().getCode(), "状态码应该是200");
        assertNotNull(response.getBody().getData(), "数据不应为null");
        
        List<?> data = (List<?>) response.getBody().getData();
        assertTrue(data.size() >= 2, "至少应该返回Redis中的2条数据");
    }

    @Test
    void testGetTop100() {
        ResponseEntity<ResultVO> response = restTemplate.getForEntity("/api/limit", ResultVO.class);
        
        assertNotNull(response.getBody(), "响应体不应为null");
        assertEquals(200, response.getBody().getCode(), "状态码应该是200");
        assertNotNull(response.getBody().getData(), "数据不应为null");
        
        List<?> data = (List<?>) response.getBody().getData();
        assertTrue(data.size() <= 100, "返回的数据不应超过100条");
        assertTrue(data.size() >= 2, "至少应该返回Redis中的2条数据");
    }
}
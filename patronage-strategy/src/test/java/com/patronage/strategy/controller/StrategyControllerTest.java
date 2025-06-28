package com.patronage.strategy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronage.strategy.entity.Strategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
class StrategyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetStrategyList() throws Exception {
        mockMvc.perform(get("/api/strategy/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testCreateStrategy() throws Exception {
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetStrategyDetail() throws Exception {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk());

        // 查询策略详情
        mockMvc.perform(get("/api/strategy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testUpdateStrategy() throws Exception {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk());

        // 更新策略
        strategy.setId(1L);
        strategy.setDescription("更新后的描述");

        mockMvc.perform(put("/api/strategy/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testDeleteStrategy() throws Exception {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk());

        // 删除策略
        mockMvc.perform(delete("/api/strategy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testStartStrategy() throws Exception {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk());

        // 启动策略
        mockMvc.perform(post("/api/strategy/1/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testStopStrategy() throws Exception {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        mockMvc.perform(post("/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(strategy)))
                .andExpect(status().isOk());

        // 停止策略
        mockMvc.perform(post("/api/strategy/1/stop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
} 
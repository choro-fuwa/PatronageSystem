package com.PatronageSystem.controller;

import com.PatronageSystem.controller.FactorController;
import com.PatronageSystem.entity.Factor;
import com.PatronageSystem.entity.FactorCategory;
import com.PatronageSystem.service.FactorCategoryService;
import com.PatronageSystem.service.FactorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FactorControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FactorService factorService;

    @Mock
    private FactorCategoryService factorCategoryService;

    @InjectMocks
    private FactorController factorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(factorController).build();
    }

    // 测试获取所有因子分类
    @Test
    void getCategories_Success() throws Exception {
        FactorCategory category = new FactorCategory();
        category.setId(1L);
        category.setCategoryName("Test Category");
        List<FactorCategory> categories = Collections.singletonList(category);

        when(factorCategoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/factor/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].categoryName").value("Test Category"));
    }

    // 测试获取分类树结构
    @Test
    void getCategoryTree_Success() throws Exception {
        FactorCategory root = new FactorCategory();
        root.setId(1L);
        root.setCategoryName("Root");
        List<FactorCategory> tree = Collections.singletonList(root);

        when(factorCategoryService.getCategoryTree()).thenReturn(tree);

        mockMvc.perform(get("/api/factor/categories/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    // 测试新增分类
    @Test
    void addCategory_Success() throws Exception {
        FactorCategory category = new FactorCategory();
        category.setCategoryName("New Category");

        when(factorCategoryService.addCategory(any(FactorCategory.class))).thenReturn(true);

        mockMvc.perform(post("/api/factor/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试更新分类
    @Test
    void updateCategory_Success() throws Exception {
        FactorCategory category = new FactorCategory();
        category.setId(1L);
        category.setCategoryName("Updated");

        when(factorCategoryService.updateCategory(any(FactorCategory.class))).thenReturn(true);

        mockMvc.perform(put("/api/factor/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试删除分类
    @Test
    void deleteCategory_Success() throws Exception {
        when(factorCategoryService.deleteCategory(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/factor/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试获取所有因子
    @Test
    void getFactors_Success() throws Exception {
        Factor factor = new Factor();
        factor.setId(1L);
        factor.setFactorName("Test Factor");
        List<Factor> factors = Collections.singletonList(factor);

        when(factorService.getAllFactors()).thenReturn(factors);

        mockMvc.perform(get("/api/factor/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    // 测试按分类获取因子
    @Test
    void getFactorsByCategory_Success() throws Exception {
        Factor factor = new Factor();
        factor.setId(2L);
        factor.setCategoryId(10L);
        List<Factor> factors = Collections.singletonList(factor);

        when(factorService.getFactorsByCategoryId(10L)).thenReturn(factors);

        mockMvc.perform(get("/api/factor/list/category/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(2));
    }

    // 测试条件查询因子
    @Test
    void searchFactors_Success() throws Exception {
        Factor factor = new Factor();
        factor.setFactorType("Technical");
        factor.setRiskLevel("High");
        List<Factor> factors = Collections.singletonList(factor);

        when(factorService.getFactorsByCondition(eq("Technical"), eq("High"), any()))
                .thenReturn(factors);

        mockMvc.perform(get("/api/factor/search")
                        .param("factorType", "Technical")
                        .param("riskLevel", "High"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].factorType").value("Technical"));
    }

    // 测试获取因子详情
    @Test
    void getFactorDetail_Success() throws Exception {
        Factor factor = new Factor();
        factor.setId(5L);
        factor.setFactorName("Detail Factor");

        when(factorService.getFactorById(5L)).thenReturn(factor);

        mockMvc.perform(get("/api/factor/detail/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(5));
    }

    // 测试新增因子
    @Test
    void addFactor_Success() throws Exception {
        Factor factor = new Factor();
        factor.setFactorCode("F100");

        when(factorService.addFactor(any(Factor.class))).thenReturn(true);

        mockMvc.perform(post("/api/factor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试更新因子
    @Test
    void updateFactor_Success() throws Exception {
        Factor factor = new Factor();
        factor.setId(3L);
        factor.setFactorName("Updated Factor");

        when(factorService.updateFactor(any(Factor.class))).thenReturn(true);

        mockMvc.perform(put("/api/factor/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试删除因子
    @Test
    void deleteFactor_Success() throws Exception {
        when(factorService.deleteFactor(4L)).thenReturn(true);

        mockMvc.perform(delete("/api/factor/delete/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 测试检查因子代码存在
    @Test
    void checkFactorCode_Exists() throws Exception {
        when(factorService.isFactorCodeExists("EXIST123")).thenReturn(true);

        mockMvc.perform(get("/api/factor/check-code/EXIST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    // 测试异常处理
    @Test
    void getCategories_Exception() throws Exception {
        when(factorCategoryService.getAllCategories())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/factor/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("获取分类失败：Database error"));
    }
}

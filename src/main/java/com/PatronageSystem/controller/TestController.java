package com.PatronageSystem.controller;

import com.PatronageSystem.entity.FactorCategory;
import com.PatronageSystem.mapper.FactorCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private FactorCategoryMapper factorCategoryMapper;
    
    @Autowired
    private com.PatronageSystem.service.FactorService factorService;
    
    /**
     * 测试数据库连接
     */
    @GetMapping("/db")
    public Map<String, Object> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 测试查询所有分类
            List<FactorCategory> categories = factorCategoryMapper.selectAll();
            result.put("success", true);
            result.put("message", "数据库连接正常");
            result.put("categoryCount", categories.size());
            result.put("categories", categories);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据库连接失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 测试分类查询
     */
    @GetMapping("/categories")
    public Map<String, Object> testCategories() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 测试各种查询方法
            List<FactorCategory> allCategories = factorCategoryMapper.selectAll();
            List<FactorCategory> rootCategories = factorCategoryMapper.selectByParentId(0L);
            List<FactorCategory> activeCategories = factorCategoryMapper.selectByStatus(1);
            
            result.put("success", true);
            result.put("allCategories", allCategories);
            result.put("rootCategories", rootCategories);
            result.put("activeCategories", activeCategories);
            result.put("totalCount", allCategories.size());
            result.put("rootCount", rootCategories.size());
            result.put("activeCount", activeCategories.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询分类失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 测试因子查询
     */
    @GetMapping("/factors")
    public Map<String, Object> testFactors() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 测试各种查询方法
            List<com.PatronageSystem.entity.Factor> allFactors = factorService.getAllFactors();
            List<com.PatronageSystem.entity.Factor> factorsByCategory = factorService.getFactorsByCategoryId(5L); // 测试分类ID=5
            
            result.put("success", true);
            result.put("allFactors", allFactors);
            result.put("factorsByCategory", factorsByCategory);
            result.put("totalCount", allFactors.size());
            result.put("categoryCount", factorsByCategory.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询因子失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
} 
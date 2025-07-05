package com.PatronageSystem.controller;

import com.PatronageSystem.entity.Factor;
import com.PatronageSystem.entity.FactorCategory;
import com.PatronageSystem.service.FactorCategoryService;
import com.PatronageSystem.service.FactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 因子管理Controller
 */
@RestController
@RequestMapping("/api/factor")
@CrossOrigin(origins = "*")
public class FactorController {
    
    @Autowired
    private FactorService factorService;
    
    @Autowired
    private FactorCategoryService factorCategoryService;
    
    /**
     * 获取所有因子分类
     */
    @GetMapping("/categories")
    public Map<String, Object> getCategories() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FactorCategory> categories = factorCategoryService.getAllCategories();
            result.put("success", true);
            result.put("data", categories);
            result.put("message", "获取分类成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取分类失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取分类树结构
     */
    @GetMapping("/categories/tree")
    public Map<String, Object> getCategoryTree() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FactorCategory> tree = factorCategoryService.getCategoryTree();
            result.put("success", true);
            result.put("data", tree);
            result.put("message", "获取分类树成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取分类树失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 新增因子分类
     */
    @PostMapping("/categories")
    public Map<String, Object> addCategory(@RequestBody FactorCategory category) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorCategoryService.addCategory(category);
            result.put("success", success);
            result.put("message", success ? "新增分类成功" : "新增分类失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增分类失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新因子分类
     */
    @PutMapping("/categories")
    public Map<String, Object> updateCategory(@RequestBody FactorCategory category) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorCategoryService.updateCategory(category);
            result.put("success", success);
            result.put("message", success ? "更新分类成功" : "更新分类失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新分类失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除因子分类
     */
    @DeleteMapping("/categories/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorCategoryService.deleteCategory(id);
            result.put("success", success);
            result.put("message", success ? "删除分类成功" : "删除分类失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除分类失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取所有因子
     */
    @GetMapping("/list")
    public Map<String, Object> getFactors() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Factor> factors = factorService.getAllFactors();
            result.put("success", true);
            result.put("data", factors);
            result.put("message", "获取因子列表成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取因子列表失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据分类ID获取因子
     */
    @GetMapping("/list/category/{categoryId}")
    public Map<String, Object> getFactorsByCategory(@PathVariable Long categoryId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Factor> factors = factorService.getFactorsByCategoryId(categoryId);
            result.put("success", true);
            result.put("data", factors);
            result.put("message", "获取因子列表成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取因子列表失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 条件查询因子
     */
    @GetMapping("/search")
    public Map<String, Object> searchFactors(@RequestParam(required = false) String factorType,
                                             @RequestParam(required = false) String riskLevel,
                                             @RequestParam(required = false) Integer isPublic) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Factor> factors = factorService.getFactorsByCondition(factorType, riskLevel, isPublic);
            result.put("success", true);
            result.put("data", factors);
            result.put("message", "查询因子成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询因子失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取因子详情
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getFactorDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Factor factor = factorService.getFactorById(id);
            if (factor != null) {
                result.put("success", true);
                result.put("data", factor);
                result.put("message", "获取因子详情成功");
            } else {
                result.put("success", false);
                result.put("message", "因子不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取因子详情失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 新增因子
     */
    @PostMapping("/add")
    public Map<String, Object> addFactor(@RequestBody Factor factor) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorService.addFactor(factor);
            result.put("success", success);
            result.put("message", success ? "新增因子成功" : "新增因子失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增因子失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新因子
     */
    @PutMapping("/update")
    public Map<String, Object> updateFactor(@RequestBody Factor factor) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorService.updateFactor(factor);
            result.put("success", success);
            result.put("message", success ? "更新因子成功" : "更新因子失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新因子失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除因子
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteFactor(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = factorService.deleteFactor(id);
            result.put("success", success);
            result.put("message", success ? "删除因子成功" : "删除因子失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除因子失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 检查因子代码是否存在
     */
    @GetMapping("/check-code/{factorCode}")
    public Map<String, Object> checkFactorCode(@PathVariable String factorCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = factorService.isFactorCodeExists(factorCode);
            result.put("success", true);
            result.put("exists", exists);
            result.put("message", exists ? "因子代码已存在" : "因子代码可用");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检查因子代码失败：" + e.getMessage());
        }
        return result;
    }
} 
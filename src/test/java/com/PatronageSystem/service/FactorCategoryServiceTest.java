package com.PatronageSystem.service;

import com.PatronageSystem.entity.FactorCategory;
import com.PatronageSystem.mapper.FactorCategoryMapper;
import com.PatronageSystem.service.impl.FactorCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactorCategoryServiceTest {

    @Mock
    private FactorCategoryMapper factorCategoryMapper;

    @InjectMocks
    private FactorCategoryServiceImpl factorCategoryService;

    // 测试数据
    private final FactorCategory category1 = createCategory(1L, "Category1", "CODE1", 0L);
    private final FactorCategory category2 = createCategory(2L, "Category2", "CODE2", 1L);
    private final FactorCategory newCategory = createCategory(null, "NewCategory", "NEWCODE", null);
    
    private FactorCategory createCategory(Long id, String name, String code, Long parentId) {
        FactorCategory category = new FactorCategory();
        category.setId(id);
        category.setCategoryName(name);
        category.setCategoryCode(code);
        category.setParentId(parentId);
        category.setStatus(1);
        category.setSortOrder(0);
        return category;
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        // 模拟Mapper行为
        when(factorCategoryMapper.selectAll()).thenReturn(Arrays.asList(category1, category2));

        // 调用Service方法
        List<FactorCategory> result = factorCategoryService.getAllCategories();

        // 验证结果
        assertEquals(2, result.size());
        verify(factorCategoryMapper, times(1)).selectAll();
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        // 模拟Mapper行为
        when(factorCategoryMapper.selectById(1L)).thenReturn(category1);

        // 调用Service方法
        FactorCategory result = factorCategoryService.getCategoryById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("Category1", result.getCategoryName());
        assertEquals("CODE1", result.getCategoryCode());
        verify(factorCategoryMapper, times(1)).selectById(1L);
    }

    @Test
    void getCategoriesByParentId_ShouldReturnChildren() {
        // 模拟Mapper行为
        when(factorCategoryMapper.selectByParentId(1L)).thenReturn(Collections.singletonList(category2));

        // 调用Service方法
        List<FactorCategory> result = factorCategoryService.getCategoriesByParentId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("Category2", result.get(0).getCategoryName());
        assertEquals("CODE2", result.get(0).getCategoryCode());
        verify(factorCategoryMapper, times(1)).selectByParentId(1L);
    }

    @Test
    void addCategory_ShouldSetDefaultsAndReturnTrue() {
        // 模拟Mapper行为
        when(factorCategoryMapper.insert(any(FactorCategory.class))).thenReturn(1);

        // 调用Service方法
        boolean result = factorCategoryService.addCategory(newCategory);

        // 验证结果和默认值设置
        assertTrue(result);
        assertEquals(1, newCategory.getStatus());  // 验证默认状态
        assertEquals(0, newCategory.getSortOrder());  // 验证默认排序
        assertEquals(0L, newCategory.getParentId());   // 验证默认父ID
        verify(factorCategoryMapper, times(1)).insert(newCategory);
    }

    @Test
    void updateCategory_ShouldReturnTrueOnSuccess() {
        // 模拟Mapper行为
        when(factorCategoryMapper.update(category1)).thenReturn(1);

        // 调用Service方法
        boolean result = factorCategoryService.updateCategory(category1);

        // 验证结果
        assertTrue(result);
        verify(factorCategoryMapper, times(1)).update(category1);
    }

    @Test
    void deleteCategory_ShouldThrowWhenChildrenExist() {
        // 模拟存在子分类
        when(factorCategoryMapper.selectByParentId(1L)).thenReturn(Arrays.asList(category2));

        // 验证异常抛出
        assertThrows(RuntimeException.class, () -> {
            factorCategoryService.deleteCategory(1L);
        });

        // 验证未执行删除操作
        verify(factorCategoryMapper, never()).deleteById(anyLong());
    }

    @Test
    void deleteCategory_ShouldReturnTrueWhenNoChildren() {
        // 模拟无子分类
        when(factorCategoryMapper.selectByParentId(1L)).thenReturn(Collections.emptyList());
        when(factorCategoryMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = factorCategoryService.deleteCategory(1L);

        // 验证结果
        assertTrue(result);
        verify(factorCategoryMapper, times(1)).deleteById(1L);
    }

    @Test
    void getCategoryTree_ShouldBuildTreeStructure() {
        // 准备测试数据
        FactorCategory root = createCategory(0L, "Root", "ROOT", 0L);
        FactorCategory child1 = createCategory(1L, "Child1", "CHILD1", 0L);
        FactorCategory child2 = createCategory(2L, "Child2", "CHILD2", 0L);

        // 模拟Mapper行为
        when(factorCategoryMapper.selectAll()).thenReturn(Arrays.asList(root, child1, child2));

        // 调用Service方法
        List<FactorCategory> result = factorCategoryService.getCategoryTree();

        // 验证树结构 - 应该返回所有顶级分类
        assertEquals(3, result.size()); // 返回所有parentId=0L的分类
        assertTrue(result.stream().anyMatch(cat -> "Root".equals(cat.getCategoryName())));
        assertTrue(result.stream().anyMatch(cat -> "Child1".equals(cat.getCategoryName())));
        assertTrue(result.stream().anyMatch(cat -> "Child2".equals(cat.getCategoryName())));
        verify(factorCategoryMapper, times(1)).selectAll();
    }
}
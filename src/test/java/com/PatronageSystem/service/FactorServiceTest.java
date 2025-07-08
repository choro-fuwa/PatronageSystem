package com.PatronageSystem.service;

import com.PatronageSystem.entity.Factor;
import com.PatronageSystem.mapper.FactorMapper;
import com.PatronageSystem.service.impl.FactorServiceImpl;
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
public class FactorServiceTest {

    @Mock
    private FactorMapper factorMapper;

    @InjectMocks
    private FactorServiceImpl factorService;

    // 测试数据
    private final Factor factor1 = createFactor(1L, "FACTOR001", "技术因子1", 1L, "技术因子", "daily", "high", 1, 1L, 1);
    private final Factor factor2 = createFactor(2L, "FACTOR002", "基本面因子1", 2L, "基本面因子", "weekly", "medium", 1, 1L, 1);
    private final Factor newFactor = createFactor(null, "FACTOR003", "新因子", 1L, "技术因子", null, "low", null, 1L, null);

    private Factor createFactor(Long id, String factorCode, String factorName, Long categoryId, 
                               String factorType, String updateFrequency, String riskLevel, 
                               Integer isPublic, Long creatorId, Integer status) {
        Factor factor = new Factor();
        factor.setId(id);
        factor.setFactorCode(factorCode);
        factor.setFactorName(factorName);
        factor.setCategoryId(categoryId);
        factor.setFactorType(factorType);
        factor.setUpdateFrequency(updateFrequency);
        factor.setRiskLevel(riskLevel);
        factor.setIsPublic(isPublic);
        factor.setCreatorId(creatorId);
        factor.setStatus(status);
        return factor;
    }

    @Test
    void getAllFactors_ShouldReturnAllFactors() {
        // 模拟Mapper行为
        when(factorMapper.selectAll()).thenReturn(Arrays.asList(factor1, factor2));

        // 调用Service方法
        List<Factor> result = factorService.getAllFactors();

        // 验证结果
        assertEquals(2, result.size());
        verify(factorMapper, times(1)).selectAll();
    }

    @Test
    void getAllFactors_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(factorMapper.selectAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Factor> result = factorService.getAllFactors();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(factorMapper, times(1)).selectAll();
    }

    @Test
    void getFactorById_ShouldReturnFactor() {
        // 模拟Mapper行为
        when(factorMapper.selectById(1L)).thenReturn(factor1);

        // 调用Service方法
        Factor result = factorService.getFactorById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("FACTOR001", result.getFactorCode());
        assertEquals("技术因子1", result.getFactorName());
        verify(factorMapper, times(1)).selectById(1L);
    }

    @Test
    void getFactorById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(factorMapper.selectById(999L)).thenReturn(null);

        // 调用Service方法
        Factor result = factorService.getFactorById(999L);

        // 验证结果
        assertNull(result);
        verify(factorMapper, times(1)).selectById(999L);
    }

    @Test
    void getFactorsByCategoryId_ShouldReturnFactors() {
        // 模拟Mapper行为
        when(factorMapper.selectByCategoryId(1L)).thenReturn(Arrays.asList(factor1));

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCategoryId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("FACTOR001", result.get(0).getFactorCode());
        verify(factorMapper, times(1)).selectByCategoryId(1L);
    }

    @Test
    void getFactorsByCategoryId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(factorMapper.selectByCategoryId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCategoryId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(factorMapper, times(1)).selectByCategoryId(999L);
    }

    @Test
    void getFactorByCode_ShouldReturnFactor() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);

        // 调用Service方法
        Factor result = factorService.getFactorByCode("FACTOR001");

        // 验证结果
        assertNotNull(result);
        assertEquals("FACTOR001", result.getFactorCode());
        verify(factorMapper, times(1)).selectByFactorCode("FACTOR001");
    }

    @Test
    void getFactorByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        Factor result = factorService.getFactorByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(factorMapper, times(1)).selectByFactorCode("NONEXISTENT");
    }

    @Test
    void getFactorsByCondition_ShouldReturnFactors() {
        // 模拟Mapper行为
        when(factorMapper.selectByCondition("技术因子", "high", 1)).thenReturn(Arrays.asList(factor1));

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCondition("技术因子", "high", 1);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("FACTOR001", result.get(0).getFactorCode());
        verify(factorMapper, times(1)).selectByCondition("技术因子", "high", 1);
    }

    @Test
    void getFactorsByCondition_WithNullParameters() {
        // 模拟Mapper行为
        when(factorMapper.selectByCondition(null, null, null)).thenReturn(Arrays.asList(factor1, factor2));

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCondition(null, null, null);

        // 验证结果
        assertEquals(2, result.size());
        verify(factorMapper, times(1)).selectByCondition(null, null, null);
    }

    @Test
    void getFactorsByCreatorId_ShouldReturnFactors() {
        // 模拟Mapper行为
        when(factorMapper.selectByCreatorId(1L)).thenReturn(Arrays.asList(factor1, factor2));

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCreatorId(1L);

        // 验证结果
        assertEquals(2, result.size());
        verify(factorMapper, times(1)).selectByCreatorId(1L);
    }

    @Test
    void getFactorsByCreatorId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(factorMapper.selectByCreatorId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Factor> result = factorService.getFactorsByCreatorId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(factorMapper, times(1)).selectByCreatorId(999L);
    }

    @Test
    void addFactor_ShouldSetDefaultsAndReturnTrue() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR003")).thenReturn(null);
        when(factorMapper.insert(any(Factor.class))).thenReturn(1);

        // 调用Service方法
        boolean result = factorService.addFactor(newFactor);

        // 验证结果和默认值设置
        assertTrue(result);
        assertEquals(1, newFactor.getStatus());  // 验证默认状态
        assertEquals(1, newFactor.getIsPublic());  // 验证默认公开性
        assertEquals("daily", newFactor.getUpdateFrequency());  // 验证默认更新频率
        verify(factorMapper, times(1)).insert(newFactor);
    }

    @Test
    void addFactor_ShouldThrowWhenFactorCodeExists() {
        // 模拟Mapper行为 - 因子代码已存在
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);

        // 验证异常抛出
        assertThrows(RuntimeException.class, () -> {
            factorService.addFactor(factor1);
        });

        // 验证未执行插入操作
        verify(factorMapper, never()).insert(any(Factor.class));
    }

    @Test
    void addFactor_ShouldReturnFalseWhenInsertFails() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR003")).thenReturn(null);
        when(factorMapper.insert(any(Factor.class))).thenReturn(0);

        // 调用Service方法
        boolean result = factorService.addFactor(newFactor);

        // 验证结果
        assertFalse(result);
        verify(factorMapper, times(1)).insert(newFactor);
    }

    @Test
    void updateFactor_ShouldReturnTrueOnSuccess() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);
        when(factorMapper.update(factor1)).thenReturn(1);

        // 调用Service方法
        boolean result = factorService.updateFactor(factor1);

        // 验证结果
        assertTrue(result);
        verify(factorMapper, times(1)).update(factor1);
    }

    @Test
    void updateFactor_ShouldThrowWhenFactorCodeUsedByOtherFactor() {
        // 模拟Mapper行为 - 因子代码被其他因子使用
        Factor otherFactor = createFactor(999L, "FACTOR001", "其他因子", 1L, "技术因子", "daily", "high", 1, 1L, 1);
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(otherFactor);

        // 验证异常抛出
        assertThrows(RuntimeException.class, () -> {
            factorService.updateFactor(factor1);
        });

        // 验证未执行更新操作
        verify(factorMapper, never()).update(any(Factor.class));
    }

    @Test
    void updateFactor_ShouldReturnFalseWhenUpdateFails() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);
        when(factorMapper.update(factor1)).thenReturn(0);

        // 调用Service方法
        boolean result = factorService.updateFactor(factor1);

        // 验证结果
        assertFalse(result);
        verify(factorMapper, times(1)).update(factor1);
    }

    @Test
    void deleteFactor_ShouldReturnTrueOnSuccess() {
        // 模拟Mapper行为
        when(factorMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = factorService.deleteFactor(1L);

        // 验证结果
        assertTrue(result);
        verify(factorMapper, times(1)).deleteById(1L);
    }

    @Test
    void deleteFactor_ShouldReturnFalseWhenDeleteFails() {
        // 模拟Mapper行为
        when(factorMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        boolean result = factorService.deleteFactor(999L);

        // 验证结果
        assertFalse(result);
        verify(factorMapper, times(1)).deleteById(999L);
    }

    @Test
    void isFactorCodeExists_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);

        // 调用Service方法
        boolean result = factorService.isFactorCodeExists("FACTOR001");

        // 验证结果
        assertTrue(result);
        verify(factorMapper, times(1)).selectByFactorCode("FACTOR001");
    }

    @Test
    void isFactorCodeExists_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        boolean result = factorService.isFactorCodeExists("NONEXISTENT");

        // 验证结果
        assertFalse(result);
        verify(factorMapper, times(1)).selectByFactorCode("NONEXISTENT");
    }

    @Test
    void addFactor_WithExistingValues_ShouldNotOverride() {
        // 准备测试数据 - 已有值的因子
        Factor factorWithValues = createFactor(null, "FACTOR004", "已有值因子", 1L, "技术因子", "weekly", "low", 0, 1L, 0);
        
        // 模拟Mapper行为
        when(factorMapper.selectByFactorCode("FACTOR004")).thenReturn(null);
        when(factorMapper.insert(any(Factor.class))).thenReturn(1);

        // 调用Service方法
        boolean result = factorService.addFactor(factorWithValues);

        // 验证结果 - 不应覆盖已有值
        assertTrue(result);
        assertEquals(0, factorWithValues.getStatus());  // 保持原有值
        assertEquals(0, factorWithValues.getIsPublic());  // 保持原有值
        assertEquals("weekly", factorWithValues.getUpdateFrequency());  // 保持原有值
        verify(factorMapper, times(1)).insert(factorWithValues);
    }

    @Test
    void updateFactor_WithSameFactorCode_ShouldSucceed() {
        // 模拟Mapper行为 - 同一个因子的因子代码
        when(factorMapper.selectByFactorCode("FACTOR001")).thenReturn(factor1);
        when(factorMapper.update(factor1)).thenReturn(1);

        // 调用Service方法
        boolean result = factorService.updateFactor(factor1);

        // 验证结果
        assertTrue(result);
        verify(factorMapper, times(1)).update(factor1);
    }
}

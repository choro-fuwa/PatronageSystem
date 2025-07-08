package com.PatronageSystem.service;

import com.PatronageSystem.entity.FundManager;
import com.PatronageSystem.mapper.FundManagerMapper;
import com.PatronageSystem.service.impl.FundManagerServiceImpl;
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
public class FundManagerServiceTest {

    @Mock
    private FundManagerMapper fundManagerMapper;

    @InjectMocks
    private FundManagerServiceImpl fundManagerService;

    // 测试数据
    private final FundManager manager1 = createFundManager(1L, "M001", "张弘弢", 1L, "清华大学", 15, 
            "华夏基金首席投资官，拥有15年投资管理经验", 1, "华夏基金管理有限公司");
    private final FundManager manager2 = createFundManager(2L, "M002", "刘晓艳", 2L, "北京大学", 12, 
            "易方达基金投资总监，专注于权益类投资", 1, "易方达基金管理有限公司");
    private final FundManager manager3 = createFundManager(3L, "M003", "赵学军", 3L, "复旦大学", 18, 
            "嘉实基金总经理，具有丰富的基金管理经验", 1, "嘉实基金管理有限公司");
    private final FundManager manager4 = createFundManager(4L, "M004", "王亚伟", 1L, "中国人民大学", 20, 
            "华夏基金前明星基金经理，现为私募基金经理", 1, "华夏基金管理有限公司");

    private FundManager createFundManager(Long id, String managerCode, String managerName, Long companyId,
                                        String education, Integer experienceYears, String introduction,
                                        Integer status, String companyName) {
        FundManager manager = new FundManager();
        manager.setId(id);
        manager.setManagerCode(managerCode);
        manager.setManagerName(managerName);
        manager.setCompanyId(companyId);
        manager.setEducation(education);
        manager.setExperienceYears(experienceYears);
        manager.setIntroduction(introduction);
        manager.setStatus(status);
        manager.setCompanyName(companyName);
        manager.setCreateTime(LocalDateTime.now());
        manager.setUpdateTime(LocalDateTime.now());
        return manager;
    }

    @Test
    void findAll_ShouldReturnAllManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.findAll()).thenReturn(Arrays.asList(manager1, manager2, manager3, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.findAll();

        // 验证结果
        assertEquals(4, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        assertEquals("张弘弢", result.get(0).getManagerName());
        assertEquals("M002", result.get(1).getManagerCode());
        assertEquals("刘晓艳", result.get(1).getManagerName());
        verify(fundManagerMapper, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundManagerMapper.findAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<FundManager> result = fundManagerService.findAll();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundManagerMapper, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnManager() {
        // 模拟Mapper行为
        when(fundManagerMapper.findById(1L)).thenReturn(manager1);

        // 调用Service方法
        FundManager result = fundManagerService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("M001", result.getManagerCode());
        assertEquals("张弘弢", result.getManagerName());
        assertEquals(1L, result.getCompanyId());
        assertEquals("清华大学", result.getEducation());
        assertEquals(15, result.getExperienceYears());
        assertEquals("华夏基金首席投资官，拥有15年投资管理经验", result.getIntroduction());
        assertEquals(1, result.getStatus());
        assertEquals("华夏基金管理有限公司", result.getCompanyName());
        verify(fundManagerMapper, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundManagerMapper.findById(999L)).thenReturn(null);

        // 调用Service方法
        FundManager result = fundManagerService.findById(999L);

        // 验证结果
        assertNull(result);
        verify(fundManagerMapper, times(1)).findById(999L);
    }

    @Test
    void findById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        FundManager result = fundManagerService.findById(null);

        // 验证结果
        assertNull(result);
        verify(fundManagerMapper, times(1)).findById(null);
    }

    @Test
    void findByCode_ShouldReturnManager() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCode("M001")).thenReturn(manager1);

        // 调用Service方法
        FundManager result = fundManagerService.findByCode("M001");

        // 验证结果
        assertNotNull(result);
        assertEquals("M001", result.getManagerCode());
        assertEquals("张弘弢", result.getManagerName());
        assertEquals("华夏基金管理有限公司", result.getCompanyName());
        verify(fundManagerMapper, times(1)).findByCode("M001");
    }

    @Test
    void findByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        FundManager result = fundManagerService.findByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(fundManagerMapper, times(1)).findByCode("NONEXISTENT");
    }

    @Test
    void findByCode_WithNullCode_ShouldReturnNull() {
        // 调用Service方法
        FundManager result = fundManagerService.findByCode(null);

        // 验证结果
        assertNull(result);
        verify(fundManagerMapper, times(1)).findByCode(null);
    }

    @Test
    void findByCode_WithEmptyCode_ShouldReturnNull() {
        // 调用Service方法
        FundManager result = fundManagerService.findByCode("");

        // 验证结果
        assertNull(result);
        verify(fundManagerMapper, times(1)).findByCode("");
    }

    @Test
    void findByCompanyId_ShouldReturnManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCompanyId(1L)).thenReturn(Arrays.asList(manager1, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.findByCompanyId(1L);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        assertEquals("张弘弢", result.get(0).getManagerName());
        assertEquals("M004", result.get(1).getManagerCode());
        assertEquals("王亚伟", result.get(1).getManagerName());
        assertTrue(result.stream().allMatch(m -> m.getCompanyId().equals(1L)));
        verify(fundManagerMapper, times(1)).findByCompanyId(1L);
    }

    @Test
    void findByCompanyId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCompanyId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<FundManager> result = fundManagerService.findByCompanyId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundManagerMapper, times(1)).findByCompanyId(999L);
    }

    @Test
    void findByCompanyId_WithNullCompanyId_ShouldReturnEmptyList() {
        // 调用Service方法
        List<FundManager> result = fundManagerService.findByCompanyId(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundManagerMapper, times(1)).findByCompanyId(null);
    }

    @Test
    void search_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("张弘弢")).thenReturn(Arrays.asList(manager1));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("张弘弢");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        assertEquals("张弘弢", result.get(0).getManagerName());
        verify(fundManagerMapper, times(1)).search("张弘弢");
    }

    @Test
    void search_ShouldReturnMultipleMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("华夏")).thenReturn(Arrays.asList(manager1, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("华夏");

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getManagerCode().equals("M001")));
        assertTrue(result.stream().anyMatch(m -> m.getManagerCode().equals("M004")));
        verify(fundManagerMapper, times(1)).search("华夏");
    }

    @Test
    void search_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("不存在的经理")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("不存在的经理");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundManagerMapper, times(1)).search("不存在的经理");
    }

    @Test
    void search_WithNullKeyword_ShouldReturnAllManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search(null)).thenReturn(Arrays.asList(manager1, manager2, manager3, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search(null);

        // 验证结果
        assertEquals(4, result.size());
        verify(fundManagerMapper, times(1)).search(null);
    }

    @Test
    void search_WithEmptyKeyword_ShouldReturnAllManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("")).thenReturn(Arrays.asList(manager1, manager2, manager3, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("");

        // 验证结果
        assertEquals(4, result.size());
        verify(fundManagerMapper, times(1)).search("");
    }

    @Test
    void search_WithWhitespaceKeyword_ShouldReturnAllManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("   ")).thenReturn(Arrays.asList(manager1, manager2, manager3, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("   ");

        // 验证结果
        assertEquals(4, result.size());
        verify(fundManagerMapper, times(1)).search("   ");
    }

    @Test
    void search_WithManagerCode_ShouldReturnMatchingManager() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("M001")).thenReturn(Arrays.asList(manager1));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("M001");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        verify(fundManagerMapper, times(1)).search("M001");
    }

    @Test
    void search_WithEducation_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("清华大学")).thenReturn(Arrays.asList(manager1));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("清华大学");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        assertEquals("清华大学", result.get(0).getEducation());
        verify(fundManagerMapper, times(1)).search("清华大学");
    }

    @Test
    void search_WithCompanyName_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("华夏基金")).thenReturn(Arrays.asList(manager1, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("华夏基金");

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> "华夏基金管理有限公司".equals(m.getCompanyName())));
        verify(fundManagerMapper, times(1)).search("华夏基金");
    }

    @Test
    void search_WithPartialName_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("刘")).thenReturn(Arrays.asList(manager2));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("刘");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M002", result.get(0).getManagerCode());
        assertEquals("刘晓艳", result.get(0).getManagerName());
        verify(fundManagerMapper, times(1)).search("刘");
    }

    @Test
    void search_WithExperienceKeyword_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("投资")).thenReturn(Arrays.asList(manager1, manager2, manager3));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("投资");

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getManagerCode().equals("M001")));
        assertTrue(result.stream().anyMatch(m -> m.getManagerCode().equals("M002")));
        assertTrue(result.stream().anyMatch(m -> m.getManagerCode().equals("M003")));
        verify(fundManagerMapper, times(1)).search("投资");
    }

    @Test
    void search_WithSpecialCharacters_ShouldHandleCorrectly() {
        // 模拟Mapper行为
        when(fundManagerMapper.search("张弘弢%")).thenReturn(Arrays.asList(manager1));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("张弘弢%");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        verify(fundManagerMapper, times(1)).search("张弘弢%");
    }

    @Test
    void search_WithCaseInsensitive_ShouldReturnMatchingManagers() {
        // 模拟Mapper行为 - 假设搜索是大小写不敏感的
        when(fundManagerMapper.search("张弘弢")).thenReturn(Arrays.asList(manager1));

        // 调用Service方法
        List<FundManager> result = fundManagerService.search("张弘弢");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getManagerCode());
        verify(fundManagerMapper, times(1)).search("张弘弢");
    }

    @Test
    void findByCompanyId_WithMultipleManagers_ShouldReturnAllManagers() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCompanyId(1L)).thenReturn(Arrays.asList(manager1, manager4));

        // 调用Service方法
        List<FundManager> result = fundManagerService.findByCompanyId(1L);

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getCompanyId().equals(1L)));
        assertTrue(result.stream().anyMatch(m -> m.getManagerName().equals("张弘弢")));
        assertTrue(result.stream().anyMatch(m -> m.getManagerName().equals("王亚伟")));
        verify(fundManagerMapper, times(1)).findByCompanyId(1L);
    }

    @Test
    void findByCompanyId_WithSingleManager_ShouldReturnOneManager() {
        // 模拟Mapper行为
        when(fundManagerMapper.findByCompanyId(2L)).thenReturn(Arrays.asList(manager2));

        // 调用Service方法
        List<FundManager> result = fundManagerService.findByCompanyId(2L);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("M002", result.get(0).getManagerCode());
        assertEquals("刘晓艳", result.get(0).getManagerName());
        assertEquals(2L, result.get(0).getCompanyId());
        verify(fundManagerMapper, times(1)).findByCompanyId(2L);
    }
}

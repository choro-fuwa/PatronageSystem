package com.PatronageSystem.service;

import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.mapper.PositionMapper;
import com.PatronageSystem.Patronage.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @Mock
    private PositionMapper positionMapper;

    @InjectMocks
    private PositionServiceImpl positionService;

    // 测试数据
    private final Position position1 = createPosition(1L, 100L, 200L, 
            new BigDecimal("1000.00"), new BigDecimal("800.00"), new BigDecimal("200.00"),
            new BigDecimal("10.50"), new BigDecimal("11.20"), new BigDecimal("11200.00"),
            new BigDecimal("700.00"), new BigDecimal("150.00"), new BigDecimal("850.00"));

    private final Position position2 = createPosition(2L, 100L, 201L, 
            new BigDecimal("500.00"), new BigDecimal("450.00"), new BigDecimal("50.00"),
            new BigDecimal("15.30"), new BigDecimal("16.80"), new BigDecimal("8400.00"),
            new BigDecimal("750.00"), new BigDecimal("200.00"), new BigDecimal("950.00"));

    private final Position position3 = createPosition(3L, 101L, 200L, 
            new BigDecimal("2000.00"), new BigDecimal("1800.00"), new BigDecimal("200.00"),
            new BigDecimal("12.00"), new BigDecimal("13.50"), new BigDecimal("27000.00"),
            new BigDecimal("3000.00"), new BigDecimal("500.00"), new BigDecimal("3500.00"));

    private Position createPosition(Long id, Long accountId, Long fundId, 
                                   BigDecimal totalQuantity, BigDecimal availableQuantity, BigDecimal frozenQuantity,
                                   BigDecimal avgCost, BigDecimal marketPrice, BigDecimal marketValue,
                                   BigDecimal unrealizedPnl, BigDecimal realizedPnl, BigDecimal totalPnl) {
        Position position = new Position();
        position.setId(id);
        position.setAccountId(accountId);
        position.setFundId(fundId);
        position.setTotalQuantity(totalQuantity);
        position.setAvailableQuantity(availableQuantity);
        position.setFrozenQuantity(frozenQuantity);
        position.setAvgCost(avgCost);
        position.setMarketPrice(marketPrice);
        position.setMarketValue(marketValue);
        position.setUnrealizedPnl(unrealizedPnl);
        position.setRealizedPnl(realizedPnl);
        position.setTotalPnl(totalPnl);
        position.setUpdateTime(LocalDateTime.now());
        return position;
    }

    // ==================== 查询功能测试 ====================

    @Test
    void getAllPositions_ShouldReturnAllPositions() {
        // 模拟Mapper行为
        when(positionMapper.selectAll()).thenReturn(Arrays.asList(position1, position2, position3));

        // 调用Service方法
        List<Position> result = positionService.getAllPositions();

        // 验证结果
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(100L, result.get(0).getAccountId());
        assertEquals(200L, result.get(0).getFundId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(100L, result.get(1).getAccountId());
        assertEquals(201L, result.get(1).getFundId());
        assertEquals(3L, result.get(2).getId());
        assertEquals(101L, result.get(2).getAccountId());
        assertEquals(200L, result.get(2).getFundId());
        verify(positionMapper, times(1)).selectAll();
    }

    @Test
    void getAllPositions_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(positionMapper.selectAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Position> result = positionService.getAllPositions();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(positionMapper, times(1)).selectAll();
    }

    @Test
    void getPositionById_ShouldReturnPosition() {
        // 模拟Mapper行为
        when(positionMapper.selectById(1L)).thenReturn(position1);

        // 调用Service方法
        Position result = positionService.getPositionById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        assertEquals(new BigDecimal("1000.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("800.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("200.00"), result.getFrozenQuantity());
        assertEquals(new BigDecimal("10.50"), result.getAvgCost());
        assertEquals(new BigDecimal("11.20"), result.getMarketPrice());
        assertEquals(new BigDecimal("11200.00"), result.getMarketValue());
        assertEquals(new BigDecimal("700.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("150.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("850.00"), result.getTotalPnl());
        verify(positionMapper, times(1)).selectById(1L);
    }

    @Test
    void getPositionById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(positionMapper.selectById(999L)).thenReturn(null);

        // 调用Service方法
        Position result = positionService.getPositionById(999L);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectById(999L);
    }

    @Test
    void getPositionsByAccountId_ShouldReturnPositions() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountId(100L)).thenReturn(Arrays.asList(position1, position2));

        // 调用Service方法
        List<Position> result = positionService.getPositionsByAccountId(100L);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(100L, result.get(0).getAccountId());
        assertEquals(200L, result.get(0).getFundId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(100L, result.get(1).getAccountId());
        assertEquals(201L, result.get(1).getFundId());
        verify(positionMapper, times(1)).selectByAccountId(100L);
    }

    @Test
    void getPositionsByAccountId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Position> result = positionService.getPositionsByAccountId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(positionMapper, times(1)).selectByAccountId(999L);
    }

    @Test
    void getPositionByAccountAndFund_ShouldReturnPosition() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(100L, 200L)).thenReturn(position1);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(100L, 200L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        assertEquals(new BigDecimal("1000.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("800.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("200.00"), result.getFrozenQuantity());
        verify(positionMapper, times(1)).selectByAccountAndFund(100L, 200L);
    }

    @Test
    void getPositionByAccountAndFund_ShouldReturnNull() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(999L, 999L)).thenReturn(null);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(999L, 999L);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectByAccountAndFund(999L, 999L);
    }

    // ==================== 创建功能测试 ====================

    @Test
    void createPosition_ShouldReturnCreatedPosition() {
        // 准备测试数据
        Position newPosition = createPosition(null, 102L, 203L, 
                new BigDecimal("1500.00"), new BigDecimal("1400.00"), new BigDecimal("100.00"),
                new BigDecimal("20.00"), new BigDecimal("22.50"), new BigDecimal("33750.00"),
                new BigDecimal("3750.00"), new BigDecimal("0.00"), new BigDecimal("3750.00"));

        // 模拟Mapper行为
        when(positionMapper.insert(any(Position.class))).thenAnswer(invocation -> {
            Position position = invocation.getArgument(0);
            position.setId(4L);
            return 1;
        });

        // 调用Service方法
        Position result = positionService.createPosition(newPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals(102L, result.getAccountId());
        assertEquals(203L, result.getFundId());
        assertEquals(new BigDecimal("1500.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("1400.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("100.00"), result.getFrozenQuantity());
        assertEquals(new BigDecimal("20.00"), result.getAvgCost());
        assertEquals(new BigDecimal("22.50"), result.getMarketPrice());
        assertEquals(new BigDecimal("33750.00"), result.getMarketValue());
        assertEquals(new BigDecimal("3750.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("0.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("3750.00"), result.getTotalPnl());
        verify(positionMapper, times(1)).insert(newPosition);
    }

    @Test
    void createPosition_WithNullValues_ShouldReturnCreatedPosition() {
        // 准备测试数据
        Position newPosition = new Position();
        newPosition.setAccountId(103L);
        newPosition.setFundId(204L);
        newPosition.setTotalQuantity(new BigDecimal("500.00"));
        newPosition.setAvailableQuantity(new BigDecimal("500.00"));
        newPosition.setFrozenQuantity(BigDecimal.ZERO);
        newPosition.setAvgCost(new BigDecimal("10.00"));
        newPosition.setMarketPrice(new BigDecimal("10.00"));
        newPosition.setMarketValue(new BigDecimal("5000.00"));
        newPosition.setUnrealizedPnl(BigDecimal.ZERO);
        newPosition.setRealizedPnl(BigDecimal.ZERO);
        newPosition.setTotalPnl(BigDecimal.ZERO);

        // 模拟Mapper行为
        when(positionMapper.insert(any(Position.class))).thenAnswer(invocation -> {
            Position position = invocation.getArgument(0);
            position.setId(5L);
            return 1;
        });

        // 调用Service方法
        Position result = positionService.createPosition(newPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals(103L, result.getAccountId());
        assertEquals(204L, result.getFundId());
        assertEquals(new BigDecimal("500.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("500.00"), result.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, result.getFrozenQuantity());
        verify(positionMapper, times(1)).insert(newPosition);
    }

    // ==================== 更新功能测试 ====================

    @Test
    void updatePosition_ShouldReturnUpdatedPosition() {
        // 准备测试数据
        Position updatedPosition = createPosition(1L, 100L, 200L, 
                new BigDecimal("1200.00"), new BigDecimal("1000.00"), new BigDecimal("200.00"),
                new BigDecimal("10.50"), new BigDecimal("12.00"), new BigDecimal("14400.00"),
                new BigDecimal("1800.00"), new BigDecimal("150.00"), new BigDecimal("1950.00"));

        // 模拟Mapper行为
        when(positionMapper.update(any(Position.class))).thenReturn(1);

        // 调用Service方法
        Position result = positionService.updatePosition(updatedPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        assertEquals(new BigDecimal("1200.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("1000.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("200.00"), result.getFrozenQuantity());
        assertEquals(new BigDecimal("10.50"), result.getAvgCost());
        assertEquals(new BigDecimal("12.00"), result.getMarketPrice());
        assertEquals(new BigDecimal("14400.00"), result.getMarketValue());
        assertEquals(new BigDecimal("1800.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("150.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("1950.00"), result.getTotalPnl());
        verify(positionMapper, times(1)).update(updatedPosition);
    }

    @Test
    void updatePosition_WithZeroValues_ShouldReturnUpdatedPosition() {
        // 准备测试数据
        Position updatedPosition = createPosition(2L, 100L, 201L, 
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        // 模拟Mapper行为
        when(positionMapper.update(any(Position.class))).thenReturn(1);

        // 调用Service方法
        Position result = positionService.updatePosition(updatedPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(201L, result.getFundId());
        assertEquals(BigDecimal.ZERO, result.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, result.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, result.getFrozenQuantity());
        assertEquals(BigDecimal.ZERO, result.getAvgCost());
        assertEquals(BigDecimal.ZERO, result.getMarketPrice());
        assertEquals(BigDecimal.ZERO, result.getMarketValue());
        assertEquals(BigDecimal.ZERO, result.getUnrealizedPnl());
        assertEquals(BigDecimal.ZERO, result.getRealizedPnl());
        assertEquals(BigDecimal.ZERO, result.getTotalPnl());
        verify(positionMapper, times(1)).update(updatedPosition);
    }

    // ==================== 删除功能测试 ====================

    @Test
    void deletePosition_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(positionMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = positionService.deletePosition(1L);

        // 验证结果
        assertTrue(result);
        verify(positionMapper, times(1)).deleteById(1L);
    }

    @Test
    void deletePosition_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePosition(999L);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteById(999L);
    }

    @Test
    void deletePositionByAccountAndFund_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(positionMapper.deleteByAccountAndFund(100L, 200L)).thenReturn(1);

        // 调用Service方法
        boolean result = positionService.deletePositionByAccountAndFund(100L, 200L);

        // 验证结果
        assertTrue(result);
        verify(positionMapper, times(1)).deleteByAccountAndFund(100L, 200L);
    }

    @Test
    void deletePositionByAccountAndFund_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteByAccountAndFund(999L, 999L)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePositionByAccountAndFund(999L, 999L);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteByAccountAndFund(999L, 999L);
    }

    // ==================== 边界情况测试 ====================

    @Test
    void getPositionById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        Position result = positionService.getPositionById(null);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectById(null);
    }

    @Test
    void getPositionsByAccountId_WithNullAccountId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountId(null)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Position> result = positionService.getPositionsByAccountId(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(positionMapper, times(1)).selectByAccountId(null);
    }

    @Test
    void getPositionByAccountAndFund_WithNullAccountId_ShouldReturnNull() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(null, 200L)).thenReturn(null);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(null, 200L);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectByAccountAndFund(null, 200L);
    }

    @Test
    void getPositionByAccountAndFund_WithNullFundId_ShouldReturnNull() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(100L, null)).thenReturn(null);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(100L, null);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectByAccountAndFund(100L, null);
    }

    @Test
    void getPositionByAccountAndFund_WithBothNullIds_ShouldReturnNull() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(null, null)).thenReturn(null);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(null, null);

        // 验证结果
        assertNull(result);
        verify(positionMapper, times(1)).selectByAccountAndFund(null, null);
    }

    @Test
    void createPosition_WithNegativeValues_ShouldReturnCreatedPosition() {
        // 准备测试数据
        Position newPosition = createPosition(null, 104L, 205L, 
                new BigDecimal("-100.00"), new BigDecimal("-100.00"), BigDecimal.ZERO,
                new BigDecimal("-5.00"), new BigDecimal("-5.00"), new BigDecimal("500.00"),
                new BigDecimal("-500.00"), new BigDecimal("-200.00"), new BigDecimal("-700.00"));

        // 模拟Mapper行为
        when(positionMapper.insert(any(Position.class))).thenAnswer(invocation -> {
            Position position = invocation.getArgument(0);
            position.setId(6L);
            return 1;
        });

        // 调用Service方法
        Position result = positionService.createPosition(newPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(6L, result.getId());
        assertEquals(104L, result.getAccountId());
        assertEquals(205L, result.getFundId());
        assertEquals(new BigDecimal("-100.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("-100.00"), result.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, result.getFrozenQuantity());
        assertEquals(new BigDecimal("-5.00"), result.getAvgCost());
        assertEquals(new BigDecimal("-5.00"), result.getMarketPrice());
        assertEquals(new BigDecimal("500.00"), result.getMarketValue());
        assertEquals(new BigDecimal("-500.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("-200.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("-700.00"), result.getTotalPnl());
        verify(positionMapper, times(1)).insert(newPosition);
    }

    @Test
    void updatePosition_WithLargeValues_ShouldReturnUpdatedPosition() {
        // 准备测试数据
        Position updatedPosition = createPosition(3L, 101L, 200L, 
                new BigDecimal("999999.99"), new BigDecimal("999999.99"), BigDecimal.ZERO,
                new BigDecimal("999.99"), new BigDecimal("999.99"), new BigDecimal("999999999.99"),
                new BigDecimal("999999.99"), new BigDecimal("999999.99"), new BigDecimal("1999999.98"));

        // 模拟Mapper行为
        when(positionMapper.update(any(Position.class))).thenReturn(1);

        // 调用Service方法
        Position result = positionService.updatePosition(updatedPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals(101L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        assertEquals(new BigDecimal("999999.99"), result.getTotalQuantity());
        assertEquals(new BigDecimal("999999.99"), result.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, result.getFrozenQuantity());
        assertEquals(new BigDecimal("999.99"), result.getAvgCost());
        assertEquals(new BigDecimal("999.99"), result.getMarketPrice());
        assertEquals(new BigDecimal("999999999.99"), result.getMarketValue());
        assertEquals(new BigDecimal("999999.99"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("999999.99"), result.getRealizedPnl());
        assertEquals(new BigDecimal("1999999.98"), result.getTotalPnl());
        verify(positionMapper, times(1)).update(updatedPosition);
    }

    @Test
    void deletePosition_WithNullId_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteById(null)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePosition(null);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteById(null);
    }

    @Test
    void deletePositionByAccountAndFund_WithNullAccountId_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteByAccountAndFund(null, 200L)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePositionByAccountAndFund(null, 200L);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteByAccountAndFund(null, 200L);
    }

    @Test
    void deletePositionByAccountAndFund_WithNullFundId_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteByAccountAndFund(100L, null)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePositionByAccountAndFund(100L, null);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteByAccountAndFund(100L, null);
    }

    @Test
    void deletePositionByAccountAndFund_WithBothNullIds_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(positionMapper.deleteByAccountAndFund(null, null)).thenReturn(0);

        // 调用Service方法
        boolean result = positionService.deletePositionByAccountAndFund(null, null);

        // 验证结果
        assertFalse(result);
        verify(positionMapper, times(1)).deleteByAccountAndFund(null, null);
    }

    // ==================== 业务逻辑测试 ====================

    @Test
    void createPosition_WithCalculatedValues_ShouldReturnCreatedPosition() {
        // 准备测试数据 - 模拟计算后的持仓数据
        Position newPosition = createPosition(null, 105L, 206L, 
                new BigDecimal("1000.00"), new BigDecimal("1000.00"), BigDecimal.ZERO,
                new BigDecimal("10.00"), new BigDecimal("12.00"), new BigDecimal("12000.00"),
                new BigDecimal("2000.00"), BigDecimal.ZERO, new BigDecimal("2000.00"));

        // 模拟Mapper行为
        when(positionMapper.insert(any(Position.class))).thenAnswer(invocation -> {
            Position position = invocation.getArgument(0);
            position.setId(7L);
            return 1;
        });

        // 调用Service方法
        Position result = positionService.createPosition(newPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals(105L, result.getAccountId());
        assertEquals(206L, result.getFundId());
        
        // 验证持仓数量计算
        assertEquals(new BigDecimal("1000.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("1000.00"), result.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, result.getFrozenQuantity());
        
        // 验证成本计算
        assertEquals(new BigDecimal("10.00"), result.getAvgCost());
        assertEquals(new BigDecimal("12.00"), result.getMarketPrice());
        assertEquals(new BigDecimal("12000.00"), result.getMarketValue());
        
        // 验证盈亏计算
        assertEquals(new BigDecimal("2000.00"), result.getUnrealizedPnl());
        assertEquals(BigDecimal.ZERO, result.getRealizedPnl());
        assertEquals(new BigDecimal("2000.00"), result.getTotalPnl());
        
        verify(positionMapper, times(1)).insert(newPosition);
    }

    @Test
    void updatePosition_WithPartialUpdate_ShouldReturnUpdatedPosition() {
        // 准备测试数据 - 只更新部分字段
        Position updatedPosition = new Position();
        updatedPosition.setId(1L);
        updatedPosition.setAccountId(100L);
        updatedPosition.setFundId(200L);
        updatedPosition.setTotalQuantity(new BigDecimal("1500.00"));
        updatedPosition.setAvailableQuantity(new BigDecimal("1200.00"));
        updatedPosition.setFrozenQuantity(new BigDecimal("300.00"));
        updatedPosition.setAvgCost(new BigDecimal("10.50"));
        updatedPosition.setMarketPrice(new BigDecimal("13.00"));
        updatedPosition.setMarketValue(new BigDecimal("19500.00"));
        updatedPosition.setUnrealizedPnl(new BigDecimal("3750.00"));
        updatedPosition.setRealizedPnl(new BigDecimal("150.00"));
        updatedPosition.setTotalPnl(new BigDecimal("3900.00"));

        // 模拟Mapper行为
        when(positionMapper.update(any(Position.class))).thenReturn(1);

        // 调用Service方法
        Position result = positionService.updatePosition(updatedPosition);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        
        // 验证更新后的持仓数量
        assertEquals(new BigDecimal("1500.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("1200.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("300.00"), result.getFrozenQuantity());
        
        // 验证更新后的价格和市值
        assertEquals(new BigDecimal("10.50"), result.getAvgCost());
        assertEquals(new BigDecimal("13.00"), result.getMarketPrice());
        assertEquals(new BigDecimal("19500.00"), result.getMarketValue());
        
        // 验证更新后的盈亏
        assertEquals(new BigDecimal("3750.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("150.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("3900.00"), result.getTotalPnl());
        
        verify(positionMapper, times(1)).update(updatedPosition);
    }

    @Test
    void getPositionsByAccountId_WithMultiplePositions_ShouldReturnAllPositions() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountId(100L)).thenReturn(Arrays.asList(position1, position2));

        // 调用Service方法
        List<Position> result = positionService.getPositionsByAccountId(100L);

        // 验证结果
        assertEquals(2, result.size());
        
        // 验证第一个持仓
        Position firstPosition = result.get(0);
        assertEquals(1L, firstPosition.getId());
        assertEquals(100L, firstPosition.getAccountId());
        assertEquals(200L, firstPosition.getFundId());
        assertEquals(new BigDecimal("1000.00"), firstPosition.getTotalQuantity());
        
        // 验证第二个持仓
        Position secondPosition = result.get(1);
        assertEquals(2L, secondPosition.getId());
        assertEquals(100L, secondPosition.getAccountId());
        assertEquals(201L, secondPosition.getFundId());
        assertEquals(new BigDecimal("500.00"), secondPosition.getTotalQuantity());
        
        verify(positionMapper, times(1)).selectByAccountId(100L);
    }

    @Test
    void getPositionByAccountAndFund_WithExistingPosition_ShouldReturnPosition() {
        // 模拟Mapper行为
        when(positionMapper.selectByAccountAndFund(100L, 200L)).thenReturn(position1);

        // 调用Service方法
        Position result = positionService.getPositionByAccountAndFund(100L, 200L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAccountId());
        assertEquals(200L, result.getFundId());
        
        // 验证持仓详情
        assertEquals(new BigDecimal("1000.00"), result.getTotalQuantity());
        assertEquals(new BigDecimal("800.00"), result.getAvailableQuantity());
        assertEquals(new BigDecimal("200.00"), result.getFrozenQuantity());
        assertEquals(new BigDecimal("10.50"), result.getAvgCost());
        assertEquals(new BigDecimal("11.20"), result.getMarketPrice());
        assertEquals(new BigDecimal("11200.00"), result.getMarketValue());
        assertEquals(new BigDecimal("700.00"), result.getUnrealizedPnl());
        assertEquals(new BigDecimal("150.00"), result.getRealizedPnl());
        assertEquals(new BigDecimal("850.00"), result.getTotalPnl());
        
        verify(positionMapper, times(1)).selectByAccountAndFund(100L, 200L);
    }
}

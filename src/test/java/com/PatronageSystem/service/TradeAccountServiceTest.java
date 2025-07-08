package com.PatronageSystem.service;

import com.PatronageSystem.Patronage.entity.TradeAccount;
import com.PatronageSystem.Patronage.mapper.TradeAccountMapper;
import com.PatronageSystem.Patronage.service.TradeAccountService;
import com.PatronageSystem.Patronage.service.impl.TradeAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
public class TradeAccountServiceTest {

    @Mock
    private TradeAccountMapper tradeAccountMapper;

    @InjectMocks
    private TradeAccountServiceImpl tradeAccountService;

    private TradeAccount testAccount1;
    private TradeAccount testAccount2;
    private TradeAccount testAccount3;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        testAccount1 = new TradeAccount();
        testAccount1.setId(1L);
        testAccount1.setAccountCode("ACC001");
        testAccount1.setAccountName("个人交易账户1");
        testAccount1.setUserId(1001L);
        testAccount1.setAccountType("个人");
        testAccount1.setBroker("中信证券");
        testAccount1.setAccountStatus("ACTIVE");
        testAccount1.setTotalAssets(new BigDecimal("100000.00"));
        testAccount1.setAvailableCash(new BigDecimal("50000.00"));
        testAccount1.setFrozenCash(new BigDecimal("0.00"));
        testAccount1.setMarketValue(new BigDecimal("50000.00"));
        testAccount1.setUnrealizedPnl(new BigDecimal("2000.00"));
        testAccount1.setRealizedPnl(new BigDecimal("3000.00"));
        testAccount1.setTotalPnl(new BigDecimal("5000.00"));
        testAccount1.setCreateTime(LocalDateTime.now());
        testAccount1.setUpdateTime(LocalDateTime.now());

        testAccount2 = new TradeAccount();
        testAccount2.setId(2L);
        testAccount2.setAccountCode("ACC002");
        testAccount2.setAccountName("机构交易账户1");
        testAccount2.setUserId(1002L);
        testAccount2.setAccountType("机构");
        testAccount2.setBroker("华泰证券");
        testAccount2.setAccountStatus("ACTIVE");
        testAccount2.setTotalAssets(new BigDecimal("500000.00"));
        testAccount2.setAvailableCash(new BigDecimal("200000.00"));
        testAccount2.setFrozenCash(new BigDecimal("50000.00"));
        testAccount2.setMarketValue(new BigDecimal("250000.00"));
        testAccount2.setUnrealizedPnl(new BigDecimal("15000.00"));
        testAccount2.setRealizedPnl(new BigDecimal("25000.00"));
        testAccount2.setTotalPnl(new BigDecimal("40000.00"));
        testAccount2.setCreateTime(LocalDateTime.now());
        testAccount2.setUpdateTime(LocalDateTime.now());

        testAccount3 = new TradeAccount();
        testAccount3.setId(3L);
        testAccount3.setAccountCode("ACC003");
        testAccount3.setAccountName("产品交易账户1");
        testAccount3.setUserId(1003L);
        testAccount3.setAccountType("产品");
        testAccount3.setBroker("国泰君安");
        testAccount3.setAccountStatus("SUSPENDED");
        testAccount3.setTotalAssets(new BigDecimal("200000.00"));
        testAccount3.setAvailableCash(new BigDecimal("100000.00"));
        testAccount3.setFrozenCash(new BigDecimal("0.00"));
        testAccount3.setMarketValue(new BigDecimal("100000.00"));
        testAccount3.setUnrealizedPnl(new BigDecimal("-5000.00"));
        testAccount3.setRealizedPnl(new BigDecimal("8000.00"));
        testAccount3.setTotalPnl(new BigDecimal("3000.00"));
        testAccount3.setCreateTime(LocalDateTime.now());
        testAccount3.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testGetAllAccounts_Success() {
        // Given
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1, testAccount2, testAccount3);
        when(tradeAccountMapper.selectAll()).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAllAccounts();

        // Then
        assertNotNull(actualAccounts);
        assertEquals(3, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        verify(tradeAccountMapper, times(1)).selectAll();
    }

    @Test
    void testGetAllAccounts_EmptyList() {
        // Given
        when(tradeAccountMapper.selectAll()).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAllAccounts();

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).selectAll();
    }

    @Test
    void testGetAccountById_Success() {
        // Given
        Long accountId = 1L;
        when(tradeAccountMapper.selectById(accountId)).thenReturn(testAccount1);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountById(accountId);

        // Then
        assertNotNull(actualAccount);
        assertEquals(testAccount1, actualAccount);
        assertEquals(accountId, actualAccount.getId());
        verify(tradeAccountMapper, times(1)).selectById(accountId);
    }

    @Test
    void testGetAccountById_NotFound() {
        // Given
        Long accountId = 999L;
        when(tradeAccountMapper.selectById(accountId)).thenReturn(null);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountById(accountId);

        // Then
        assertNull(actualAccount);
        verify(tradeAccountMapper, times(1)).selectById(accountId);
    }

    @Test
    void testGetAccountById_NullId() {
        // Given
        when(tradeAccountMapper.selectById(null)).thenReturn(null);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountById(null);

        // Then
        assertNull(actualAccount);
        verify(tradeAccountMapper, times(1)).selectById(null);
    }

    @Test
    void testGetAccountByCode_Success() {
        // Given
        String accountCode = "ACC001";
        when(tradeAccountMapper.selectByAccountCode(accountCode)).thenReturn(testAccount1);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountByCode(accountCode);

        // Then
        assertNotNull(actualAccount);
        assertEquals(testAccount1, actualAccount);
        assertEquals(accountCode, actualAccount.getAccountCode());
        verify(tradeAccountMapper, times(1)).selectByAccountCode(accountCode);
    }

    @Test
    void testGetAccountByCode_NotFound() {
        // Given
        String accountCode = "INVALID_CODE";
        when(tradeAccountMapper.selectByAccountCode(accountCode)).thenReturn(null);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountByCode(accountCode);

        // Then
        assertNull(actualAccount);
        verify(tradeAccountMapper, times(1)).selectByAccountCode(accountCode);
    }

    @Test
    void testGetAccountByCode_NullCode() {
        // Given
        when(tradeAccountMapper.selectByAccountCode(null)).thenReturn(null);

        // When
        TradeAccount actualAccount = tradeAccountService.getAccountByCode(null);

        // Then
        assertNull(actualAccount);
        verify(tradeAccountMapper, times(1)).selectByAccountCode(null);
    }

    @Test
    void testGetAccountsByUserId_Success() {
        // Given
        Long userId = 1001L;
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1);
        when(tradeAccountMapper.selectByUserId(userId)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByUserId(userId);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(1, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        assertEquals(userId, actualAccounts.get(0).getUserId());
        verify(tradeAccountMapper, times(1)).selectByUserId(userId);
    }

    @Test
    void testGetAccountsByUserId_EmptyList() {
        // Given
        Long userId = 9999L;
        when(tradeAccountMapper.selectByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByUserId(userId);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).selectByUserId(userId);
    }

    @Test
    void testGetAccountsByUserId_NullUserId() {
        // Given
        when(tradeAccountMapper.selectByUserId(null)).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByUserId(null);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).selectByUserId(null);
    }

    @Test
    void testGetAccountsByStatus_Success() {
        // Given
        String status = "ACTIVE";
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1, testAccount2);
        when(tradeAccountMapper.selectByStatus(status)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByStatus(status);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(2, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        actualAccounts.forEach(account -> assertEquals(status, account.getAccountStatus()));
        verify(tradeAccountMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetAccountsByStatus_Suspended() {
        // Given
        String status = "SUSPENDED";
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount3);
        when(tradeAccountMapper.selectByStatus(status)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByStatus(status);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(1, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        assertEquals(status, actualAccounts.get(0).getAccountStatus());
        verify(tradeAccountMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetAccountsByStatus_EmptyList() {
        // Given
        String status = "CLOSED";
        when(tradeAccountMapper.selectByStatus(status)).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByStatus(status);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetAccountsByStatus_NullStatus() {
        // Given
        when(tradeAccountMapper.selectByStatus(null)).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.getAccountsByStatus(null);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).selectByStatus(null);
    }

    @Test
    void testCreateAccount_Success() {
        // Given
        TradeAccount newAccount = new TradeAccount();
        newAccount.setAccountCode("ACC004");
        newAccount.setAccountName("新交易账户");
        newAccount.setUserId(1004L);
        newAccount.setAccountType("个人");
        newAccount.setBroker("招商证券");
        newAccount.setAccountStatus("ACTIVE");
        newAccount.setTotalAssets(new BigDecimal("50000.00"));
        newAccount.setAvailableCash(new BigDecimal("50000.00"));
        newAccount.setFrozenCash(new BigDecimal("0.00"));
        newAccount.setMarketValue(new BigDecimal("0.00"));
        newAccount.setUnrealizedPnl(new BigDecimal("0.00"));
        newAccount.setRealizedPnl(new BigDecimal("0.00"));
        newAccount.setTotalPnl(new BigDecimal("0.00"));

        when(tradeAccountMapper.insert(newAccount)).thenReturn(1);

        // When
        TradeAccount createdAccount = tradeAccountService.createAccount(newAccount);

        // Then
        assertNotNull(createdAccount);
        assertEquals(newAccount, createdAccount);
        verify(tradeAccountMapper, times(1)).insert(newAccount);
    }

    @Test
    void testCreateAccount_WithNullValues() {
        // Given
        TradeAccount newAccount = new TradeAccount();
        newAccount.setAccountCode("ACC005");
        newAccount.setAccountName("测试账户");
        newAccount.setUserId(1005L);
        // 其他字段为null

        when(tradeAccountMapper.insert(newAccount)).thenReturn(1);

        // When
        TradeAccount createdAccount = tradeAccountService.createAccount(newAccount);

        // Then
        assertNotNull(createdAccount);
        assertEquals(newAccount, createdAccount);
        verify(tradeAccountMapper, times(1)).insert(newAccount);
    }

    @Test
    void testUpdateAccount_Success() {
        // Given
        TradeAccount updateAccount = new TradeAccount();
        updateAccount.setId(1L);
        updateAccount.setAccountCode("ACC001");
        updateAccount.setAccountName("更新后的账户名称");
        updateAccount.setUserId(1001L);
        updateAccount.setAccountType("个人");
        updateAccount.setBroker("中信证券");
        updateAccount.setAccountStatus("ACTIVE");
        updateAccount.setTotalAssets(new BigDecimal("120000.00"));
        updateAccount.setAvailableCash(new BigDecimal("60000.00"));
        updateAccount.setFrozenCash(new BigDecimal("0.00"));
        updateAccount.setMarketValue(new BigDecimal("60000.00"));
        updateAccount.setUnrealizedPnl(new BigDecimal("3000.00"));
        updateAccount.setRealizedPnl(new BigDecimal("4000.00"));
        updateAccount.setTotalPnl(new BigDecimal("7000.00"));

        when(tradeAccountMapper.update(updateAccount)).thenReturn(1);

        // When
        TradeAccount updatedAccount = tradeAccountService.updateAccount(updateAccount);

        // Then
        assertNotNull(updatedAccount);
        assertEquals(updateAccount, updatedAccount);
        assertEquals("更新后的账户名称", updatedAccount.getAccountName());
        assertEquals(new BigDecimal("120000.00"), updatedAccount.getTotalAssets());
        verify(tradeAccountMapper, times(1)).update(updateAccount);
    }

    @Test
    void testUpdateAccount_WithNullValues() {
        // Given
        TradeAccount updateAccount = new TradeAccount();
        updateAccount.setId(1L);
        updateAccount.setAccountCode("ACC001");
        updateAccount.setAccountName("部分更新账户");
        updateAccount.setUserId(1001L);
        // 其他字段为null

        when(tradeAccountMapper.update(updateAccount)).thenReturn(1);

        // When
        TradeAccount updatedAccount = tradeAccountService.updateAccount(updateAccount);

        // Then
        assertNotNull(updatedAccount);
        assertEquals(updateAccount, updatedAccount);
        verify(tradeAccountMapper, times(1)).update(updateAccount);
    }

    @Test
    void testDeleteAccount_Success() {
        // Given
        Long accountId = 1L;
        when(tradeAccountMapper.deleteById(accountId)).thenReturn(1);

        // When
        boolean result = tradeAccountService.deleteAccount(accountId);

        // Then
        assertTrue(result);
        verify(tradeAccountMapper, times(1)).deleteById(accountId);
    }

    @Test
    void testDeleteAccount_NotFound() {
        // Given
        Long accountId = 999L;
        when(tradeAccountMapper.deleteById(accountId)).thenReturn(0);

        // When
        boolean result = tradeAccountService.deleteAccount(accountId);

        // Then
        assertFalse(result);
        verify(tradeAccountMapper, times(1)).deleteById(accountId);
    }

    @Test
    void testDeleteAccount_NullId() {
        // Given
        when(tradeAccountMapper.deleteById(null)).thenReturn(0);

        // When
        boolean result = tradeAccountService.deleteAccount(null);

        // Then
        assertFalse(result);
        verify(tradeAccountMapper, times(1)).deleteById(null);
    }

    @Test
    void testUpdateAccountStatus_Success() {
        // Given
        Long accountId = 1L;
        String newStatus = "SUSPENDED";
        when(tradeAccountMapper.updateAccountStatus(accountId, newStatus)).thenReturn(1);

        // When
        boolean result = tradeAccountService.updateAccountStatus(accountId, newStatus);

        // Then
        assertTrue(result);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, newStatus);
    }

    @Test
    void testUpdateAccountStatus_NotFound() {
        // Given
        Long accountId = 999L;
        String newStatus = "ACTIVE";
        when(tradeAccountMapper.updateAccountStatus(accountId, newStatus)).thenReturn(0);

        // When
        boolean result = tradeAccountService.updateAccountStatus(accountId, newStatus);

        // Then
        assertFalse(result);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, newStatus);
    }

    @Test
    void testUpdateAccountStatus_NullId() {
        // Given
        String newStatus = "ACTIVE";
        when(tradeAccountMapper.updateAccountStatus(null, newStatus)).thenReturn(0);

        // When
        boolean result = tradeAccountService.updateAccountStatus(null, newStatus);

        // Then
        assertFalse(result);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(null, newStatus);
    }

    @Test
    void testUpdateAccountStatus_NullStatus() {
        // Given
        Long accountId = 1L;
        when(tradeAccountMapper.updateAccountStatus(accountId, null)).thenReturn(0);

        // When
        boolean result = tradeAccountService.updateAccountStatus(accountId, null);

        // Then
        assertFalse(result);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, null);
    }

    @Test
    void testSearchAccounts_ByStatusAndType() {
        // Given
        String accountStatus = "ACTIVE";
        String accountType = "个人";
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1);
        when(tradeAccountMapper.searchAccounts(accountStatus, accountType)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.searchAccounts(accountStatus, accountType);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(1, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        assertEquals(accountStatus, actualAccounts.get(0).getAccountStatus());
        assertEquals(accountType, actualAccounts.get(0).getAccountType());
        verify(tradeAccountMapper, times(1)).searchAccounts(accountStatus, accountType);
    }

    @Test
    void testSearchAccounts_ByStatusOnly() {
        // Given
        String accountStatus = "ACTIVE";
        String accountType = null;
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1, testAccount2);
        when(tradeAccountMapper.searchAccounts(accountStatus, accountType)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.searchAccounts(accountStatus, accountType);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(2, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        actualAccounts.forEach(account -> assertEquals(accountStatus, account.getAccountStatus()));
        verify(tradeAccountMapper, times(1)).searchAccounts(accountStatus, accountType);
    }

    @Test
    void testSearchAccounts_ByTypeOnly() {
        // Given
        String accountStatus = null;
        String accountType = "机构";
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount2);
        when(tradeAccountMapper.searchAccounts(accountStatus, accountType)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.searchAccounts(accountStatus, accountType);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(1, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        assertEquals(accountType, actualAccounts.get(0).getAccountType());
        verify(tradeAccountMapper, times(1)).searchAccounts(accountStatus, accountType);
    }

    @Test
    void testSearchAccounts_NoCriteria() {
        // Given
        String accountStatus = null;
        String accountType = null;
        List<TradeAccount> expectedAccounts = Arrays.asList(testAccount1, testAccount2, testAccount3);
        when(tradeAccountMapper.searchAccounts(accountStatus, accountType)).thenReturn(expectedAccounts);

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.searchAccounts(accountStatus, accountType);

        // Then
        assertNotNull(actualAccounts);
        assertEquals(3, actualAccounts.size());
        assertEquals(expectedAccounts, actualAccounts);
        verify(tradeAccountMapper, times(1)).searchAccounts(accountStatus, accountType);
    }

    @Test
    void testSearchAccounts_EmptyResult() {
        // Given
        String accountStatus = "CLOSED";
        String accountType = "个人";
        when(tradeAccountMapper.searchAccounts(accountStatus, accountType)).thenReturn(Collections.emptyList());

        // When
        List<TradeAccount> actualAccounts = tradeAccountService.searchAccounts(accountStatus, accountType);

        // Then
        assertNotNull(actualAccounts);
        assertTrue(actualAccounts.isEmpty());
        verify(tradeAccountMapper, times(1)).searchAccounts(accountStatus, accountType);
    }

    @Test
    void testAccountDataIntegrity() {
        // Given
        TradeAccount account = new TradeAccount();
        account.setId(1L);
        account.setAccountCode("TEST001");
        account.setAccountName("测试账户");
        account.setUserId(1001L);
        account.setAccountType("个人");
        account.setBroker("测试券商");
        account.setAccountStatus("ACTIVE");
        account.setTotalAssets(new BigDecimal("100000.00"));
        account.setAvailableCash(new BigDecimal("50000.00"));
        account.setFrozenCash(new BigDecimal("10000.00"));
        account.setMarketValue(new BigDecimal("40000.00"));
        account.setUnrealizedPnl(new BigDecimal("2000.00"));
        account.setRealizedPnl(new BigDecimal("3000.00"));
        account.setTotalPnl(new BigDecimal("5000.00"));

        // When & Then
        assertNotNull(account.getId());
        assertNotNull(account.getAccountCode());
        assertNotNull(account.getAccountName());
        assertNotNull(account.getUserId());
        assertNotNull(account.getAccountType());
        assertNotNull(account.getAccountStatus());
        assertNotNull(account.getTotalAssets());
        assertNotNull(account.getAvailableCash());
        assertNotNull(account.getFrozenCash());
        assertNotNull(account.getMarketValue());
        assertNotNull(account.getUnrealizedPnl());
        assertNotNull(account.getRealizedPnl());
        assertNotNull(account.getTotalPnl());

        // 验证数值逻辑
        BigDecimal expectedTotalAssets = account.getAvailableCash()
                .add(account.getFrozenCash())
                .add(account.getMarketValue());
        assertEquals(0, expectedTotalAssets.compareTo(account.getTotalAssets()));

        BigDecimal expectedTotalPnl = account.getUnrealizedPnl().add(account.getRealizedPnl());
        assertEquals(0, expectedTotalPnl.compareTo(account.getTotalPnl()));
    }

    @Test
    void testAccountStatusTransitions() {
        // Given
        Long accountId = 1L;
        
        // Test ACTIVE -> SUSPENDED
        when(tradeAccountMapper.updateAccountStatus(accountId, "SUSPENDED")).thenReturn(1);
        boolean suspendedResult = tradeAccountService.updateAccountStatus(accountId, "SUSPENDED");
        assertTrue(suspendedResult);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, "SUSPENDED");

        // Test SUSPENDED -> ACTIVE
        when(tradeAccountMapper.updateAccountStatus(accountId, "ACTIVE")).thenReturn(1);
        boolean activeResult = tradeAccountService.updateAccountStatus(accountId, "ACTIVE");
        assertTrue(activeResult);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, "ACTIVE");

        // Test ACTIVE -> CLOSED
        when(tradeAccountMapper.updateAccountStatus(accountId, "CLOSED")).thenReturn(1);
        boolean closedResult = tradeAccountService.updateAccountStatus(accountId, "CLOSED");
        assertTrue(closedResult);
        verify(tradeAccountMapper, times(1)).updateAccountStatus(accountId, "CLOSED");
    }
}

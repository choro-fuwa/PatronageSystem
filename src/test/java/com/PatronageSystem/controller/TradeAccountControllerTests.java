package com.PatronageSystem.controller;

import com.PatronageSystem.Patronage.controller.TradeAccountController;
import com.PatronageSystem.Patronage.entity.TradeAccount;
import com.PatronageSystem.Patronage.service.TradeAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeAccountControllerTests {

    @Mock
    private TradeAccountService tradeAccountService;

    @InjectMocks
    private TradeAccountController tradeAccountController;

    private TradeAccount mockAccount;

    @BeforeEach
    void setUp() {
        mockAccount = new TradeAccount();
        mockAccount.setId(1L);
        mockAccount.setAccountCode("ACC123");
        mockAccount.setAccountName("Test Account");
        mockAccount.setUserId(100L);
        mockAccount.setAccountType("TYPE_A");
        mockAccount.setBroker("Broker X");
        mockAccount.setAccountStatus("ACTIVE");
        mockAccount.setTotalAssets(BigDecimal.valueOf(10000));
        mockAccount.setAvailableCash(BigDecimal.valueOf(5000));
        mockAccount.setCreateTime(LocalDateTime.now());
        mockAccount.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void getAccountsByUserId_ShouldReturnAccounts() {
        when(tradeAccountService.getAccountsByUserId(100L))
                .thenReturn(Arrays.asList(mockAccount));

        ResponseEntity<List<TradeAccount>> response = tradeAccountController.getAccountsByUserId(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getUserId()); // 现在应该正确匹配
    }

    @Test
    void searchAccounts_WithParameters_ShouldReturnFilteredAccounts() {
        when(tradeAccountService.searchAccounts("ACTIVE", "TYPE_A"))
                .thenReturn(Arrays.asList(mockAccount));

        ResponseEntity<List<TradeAccount>> response = tradeAccountController.searchAccounts("ACTIVE", "TYPE_A");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ACTIVE", response.getBody().get(0).getAccountStatus());
        assertEquals("TYPE_A", response.getBody().get(0).getAccountType());
    }

    @Test
    void getAccountById_ExistingId_ShouldReturnAccount() {
        when(tradeAccountService.getAccountById(1L)).thenReturn(mockAccount);

        ResponseEntity<TradeAccount> response = tradeAccountController.getAccountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ACC123", response.getBody().getAccountCode());
    }

    @Test
    void getAccountById_NonExistingId_ShouldReturnNotFound() {
        when(tradeAccountService.getAccountById(999L)).thenReturn(null);

        ResponseEntity<TradeAccount> response = tradeAccountController.getAccountById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createAccount_ValidAccount_ShouldReturnCreatedAccount() {
        when(tradeAccountService.createAccount(any(TradeAccount.class))).thenReturn(mockAccount);

        ResponseEntity<TradeAccount> response = tradeAccountController.createAccount(mockAccount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Account", response.getBody().getAccountName());
    }

    @Test
    void updateAccount_ValidUpdate_ShouldReturnUpdatedAccount() {
        when(tradeAccountService.updateAccount(any(TradeAccount.class))).thenReturn(mockAccount);

        ResponseEntity<TradeAccount> response = tradeAccountController.updateAccount(1L, mockAccount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ACC123", response.getBody().getAccountCode());
    }

    @Test
    void deleteAccount_ExistingId_ShouldReturnOk() {
        when(tradeAccountService.deleteAccount(1L)).thenReturn(true);

        ResponseEntity<Void> response = tradeAccountController.deleteAccount(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateAccountStatus_ValidUpdate_ShouldReturnOk() {
        when(tradeAccountService.updateAccountStatus(1L, "FROZEN")).thenReturn(true);

        ResponseEntity<Void> response = tradeAccountController.updateAccountStatus(1L, "FROZEN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // 空结果集场景
    @Test
    void getAccountsByUserId_NoAccounts_ShouldReturnEmptyList() {
        when(tradeAccountService.getAccountsByUserId(200L))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<TradeAccount>> response = tradeAccountController.getAccountsByUserId(200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // 更新状态失败场景
    @Test
    void updateAccountStatus_NonExistingId_ShouldReturnNotFound() {
        when(tradeAccountService.updateAccountStatus(999L, "FROZEN")).thenReturn(false);

        ResponseEntity<Void> response = tradeAccountController.updateAccountStatus(999L, "FROZEN");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
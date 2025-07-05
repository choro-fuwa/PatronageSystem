package com.PatronageSystem.Patronage.service;

import com.PatronageSystem.Patronage.entity.TradeAccount;

import java.util.List;

public interface TradeAccountService {
    List<TradeAccount> getAllAccounts();

    TradeAccount getAccountById(Long id);

    TradeAccount getAccountByCode(String accountCode);

    List<TradeAccount> getAccountsByUserId(Long userId);

    List<TradeAccount> getAccountsByStatus(String status);

    TradeAccount createAccount(TradeAccount account);

    TradeAccount updateAccount(TradeAccount account);

    boolean deleteAccount(Long id);

    boolean updateAccountStatus(Long id, String status);

    List<TradeAccount> searchAccounts(String accountStatus, String accountType);
}

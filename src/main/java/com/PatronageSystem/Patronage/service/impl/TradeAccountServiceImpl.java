package com.PatronageSystem.Patronage.service.impl;

import com.PatronageSystem.Patronage.entity.TradeAccount;
import com.PatronageSystem.Patronage.mapper.TradeAccountMapper;
import com.PatronageSystem.Patronage.service.TradeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeAccountServiceImpl implements TradeAccountService {

    @Autowired
    private TradeAccountMapper tradeAccountMapper;

    @Override
    public List<TradeAccount> getAllAccounts() {
        return tradeAccountMapper.selectAll();
    }

    @Override
    public TradeAccount getAccountById(Long id) {
        return tradeAccountMapper.selectById(id);
    }

    @Override
    public TradeAccount getAccountByCode(String accountCode) {
        return tradeAccountMapper.selectByAccountCode(accountCode);
    }

    @Override
    public List<TradeAccount> getAccountsByUserId(Long userId) {
        return tradeAccountMapper.selectByUserId(userId);
    }

    @Override
    public List<TradeAccount> getAccountsByStatus(String status) {
        return tradeAccountMapper.selectByStatus(status);
    }

    @Override
    public TradeAccount createAccount(TradeAccount account) {
        tradeAccountMapper.insert(account);
        return account;
    }

    @Override
    public TradeAccount updateAccount(TradeAccount account) {
        tradeAccountMapper.update(account);
        return account;
    }

    @Override
    public boolean deleteAccount(Long id) {
        return tradeAccountMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateAccountStatus(Long id, String status) {
        return tradeAccountMapper.updateAccountStatus(id, status) > 0;
    }

    @Override
    public List<TradeAccount> searchAccounts(String accountStatus, String accountType) {
        return tradeAccountMapper.searchAccounts(accountStatus, accountType);
    }
}

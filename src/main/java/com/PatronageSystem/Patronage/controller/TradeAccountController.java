package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.TradeAccount;
import com.PatronageSystem.Patronage.service.TradeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade/accounts")
@CrossOrigin(origins = "*")
public class TradeAccountController {

    @Autowired
    private TradeAccountService tradeAccountService;

    @GetMapping
    public ResponseEntity<List<TradeAccount>> getAllAccounts() {
        List<TradeAccount> accounts = tradeAccountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TradeAccount>> searchAccounts(
        @RequestParam(required = false) String accountStatus,
        @RequestParam(required = false) String accountType
    ) {
        List<TradeAccount> accounts = tradeAccountService.searchAccounts(accountStatus, accountType);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<TradeAccount> getAccountById(@PathVariable Long id) {
        TradeAccount account = tradeAccountService.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{accountCode}")
    public ResponseEntity<TradeAccount> getAccountByCode(@PathVariable String accountCode) {
        TradeAccount account = tradeAccountService.getAccountByCode(accountCode);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TradeAccount>> getAccountsByUserId(@PathVariable Long userId) {
        List<TradeAccount> accounts = tradeAccountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TradeAccount>> getAccountsByStatus(@PathVariable String status) {
        List<TradeAccount> accounts = tradeAccountService.getAccountsByStatus(status);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<TradeAccount> createAccount(@RequestBody TradeAccount account) {
        TradeAccount createdAccount = tradeAccountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeAccount> updateAccount(@PathVariable Long id, @RequestBody TradeAccount account) {
        account.setId(id);
        TradeAccount updatedAccount = tradeAccountService.updateAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        boolean deleted = tradeAccountService.deleteAccount(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateAccountStatus(@PathVariable Long id, @RequestParam String status) {
        boolean updated = tradeAccountService.updateAccountStatus(id, status);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 
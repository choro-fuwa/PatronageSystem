package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.entity.FundManager;
import com.PatronageSystem.service.FundManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fund-manager")
@CrossOrigin(origins = "*")
public class FundManagerController {
    
    @Autowired
    private FundManagerService fundManagerService;
    
    @GetMapping("/list")
    public Result<List<FundManager>> findAll() {
        List<FundManager> managers = fundManagerService.findAll();
        return Result.success(managers);
    }
    
    @GetMapping("/{id}")
    public Result<FundManager> findById(@PathVariable Long id) {
        FundManager manager = fundManagerService.findById(id);
        if (manager != null) {
            return Result.success(manager);
        } else {
            return Result.error("基金经理不存在");
        }
    }
    
    @GetMapping("/code/{managerCode}")
    public Result<FundManager> findByCode(@PathVariable String managerCode) {
        FundManager manager = fundManagerService.findByCode(managerCode);
        if (manager != null) {
            return Result.success(manager);
        } else {
            return Result.error("基金经理不存在");
        }
    }
    
    @GetMapping("/company/{companyId}")
    public Result<List<FundManager>> findByCompanyId(@PathVariable Long companyId) {
        List<FundManager> managers = fundManagerService.findByCompanyId(companyId);
        return Result.success(managers);
    }
    
    @GetMapping("/search")
    public Result<List<FundManager>> search(@RequestParam(required = false) String keyword) {
        List<FundManager> managers = fundManagerService.search(keyword);
        return Result.success(managers);
    }
} 
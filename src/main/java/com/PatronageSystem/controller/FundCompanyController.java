package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.entity.FundCompany;
import com.PatronageSystem.service.FundCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fund-company")
@CrossOrigin(origins = "*")
public class FundCompanyController {
    
    @Autowired
    private FundCompanyService fundCompanyService;
    
    @GetMapping("/list")
    public Result<List<FundCompany>> findAll() {
        List<FundCompany> companies = fundCompanyService.findAll();
        return Result.success(companies);
    }
    
    @GetMapping("/{id}")
    public Result<FundCompany> findById(@PathVariable Long id) {
        FundCompany company = fundCompanyService.findById(id);
        if (company != null) {
            return Result.success(company);
        } else {
            return Result.error("基金公司不存在");
        }
    }
    
    @GetMapping("/code/{companyCode}")
    public Result<FundCompany> findByCode(@PathVariable String companyCode) {
        FundCompany company = fundCompanyService.findByCode(companyCode);
        if (company != null) {
            return Result.success(company);
        } else {
            return Result.error("基金公司不存在");
        }
    }
    
    @GetMapping("/search")
    public Result<List<FundCompany>> search(@RequestParam(required = false) String keyword) {
        List<FundCompany> companies = fundCompanyService.search(keyword);
        return Result.success(companies);
    }
} 
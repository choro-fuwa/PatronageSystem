package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.entity.Fund;
import com.PatronageSystem.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fund")
@CrossOrigin(origins = "*")
public class FundController {
    
    @Autowired
    private FundService fundService;
    
    @GetMapping("/list")
    public Result<List<Fund>> findAll() {
        List<Fund> funds = fundService.findAll();
        return Result.success(funds);
    }
    
    @GetMapping("/{id}")
    public Result<Fund> findById(@PathVariable Long id) {
        Fund fund = fundService.findById(id);
        if (fund != null) {
            return Result.success(fund);
        } else {
            return Result.error("基金不存在");
        }
    }
    
    @GetMapping("/code/{fundCode}")
    public Result<Fund> findByCode(@PathVariable String fundCode) {
        Fund fund = fundService.findByCode(fundCode);
        if (fund != null) {
            return Result.success(fund);
        } else {
            return Result.error("基金不存在");
        }
    }
    
    @GetMapping("/company/{companyId}")
    public Result<List<Fund>> findByCompanyId(@PathVariable Long companyId) {
        List<Fund> funds = fundService.findByCompanyId(companyId);
        return Result.success(funds);
    }
    
    @GetMapping("/manager/{managerId}")
    public Result<List<Fund>> findByManagerId(@PathVariable Long managerId) {
        List<Fund> funds = fundService.findByManagerId(managerId);
        return Result.success(funds);
    }
    
    @GetMapping("/search")
    public Result<List<Fund>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fundType,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) List<Long> tagIds) {
        List<Fund> funds = fundService.search(keyword, fundType, riskLevel, tagIds);
        return Result.success(funds);
    }
    
    @GetMapping("/{fundId}/tags")
    public Result<List<String>> getFundTags(@PathVariable Long fundId) {
        List<String> tags = fundService.getFundTags(fundId);
        return Result.success(tags);
    }
} 
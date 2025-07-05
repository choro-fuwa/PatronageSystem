package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.Fund;
import com.PatronageSystem.mapper.FundMapper;
import com.PatronageSystem.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FundServiceImpl implements FundService {
    
    @Autowired
    private FundMapper fundMapper;
    
    @Override
    public List<Fund> findAll() {
        List<Fund> funds = fundMapper.findAll();
        // 为每个基金加载标签
        for (Fund fund : funds) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return funds;
    }
    
    @Override
    public Fund findById(Long id) {
        Fund fund = fundMapper.findById(id);
        if (fund != null) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return fund;
    }
    
    @Override
    public Fund findByCode(String fundCode) {
        Fund fund = fundMapper.findByCode(fundCode);
        if (fund != null) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return fund;
    }
    
    @Override
    public List<Fund> findByCompanyId(Long companyId) {
        List<Fund> funds = fundMapper.findByCompanyId(companyId);
        for (Fund fund : funds) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return funds;
    }
    
    @Override
    public List<Fund> findByManagerId(Long managerId) {
        List<Fund> funds = fundMapper.findByManagerId(managerId);
        for (Fund fund : funds) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return funds;
    }
    
    @Override
    public List<Fund> search(String keyword, String fundType, String riskLevel, List<Long> tagIds) {
        List<Fund> funds = fundMapper.search(keyword, fundType, riskLevel, tagIds);
        for (Fund fund : funds) {
            fund.setTags(fundMapper.getFundTags(fund.getId()));
        }
        return funds;
    }
    
    @Override
    public List<String> getFundTags(Long fundId) {
        return fundMapper.getFundTags(fundId);
    }
} 
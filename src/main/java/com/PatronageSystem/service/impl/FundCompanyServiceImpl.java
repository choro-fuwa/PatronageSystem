package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.FundCompany;
import com.PatronageSystem.mapper.FundCompanyMapper;
import com.PatronageSystem.service.FundCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FundCompanyServiceImpl implements FundCompanyService {
    
    @Autowired
    private FundCompanyMapper fundCompanyMapper;
    
    @Override
    public List<FundCompany> findAll() {
        return fundCompanyMapper.findAll();
    }
    
    @Override
    public FundCompany findById(Long id) {
        return fundCompanyMapper.findById(id);
    }
    
    @Override
    public FundCompany findByCode(String companyCode) {
        return fundCompanyMapper.findByCode(companyCode);
    }
    
    @Override
    public List<FundCompany> search(String keyword) {
        return fundCompanyMapper.search(keyword);
    }
} 
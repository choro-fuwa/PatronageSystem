package com.PatronageSystem.service;

import com.PatronageSystem.entity.FundCompany;
import java.util.List;

public interface FundCompanyService {
    
    List<FundCompany> findAll();
    
    FundCompany findById(Long id);
    
    FundCompany findByCode(String companyCode);
    
    List<FundCompany> search(String keyword);
} 
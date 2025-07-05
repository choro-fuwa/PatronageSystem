package com.PatronageSystem.service;

import com.PatronageSystem.entity.FundManager;
import java.util.List;

public interface FundManagerService {
    
    List<FundManager> findAll();
    
    FundManager findById(Long id);
    
    FundManager findByCode(String managerCode);
    
    List<FundManager> findByCompanyId(Long companyId);
    
    List<FundManager> search(String keyword);
} 
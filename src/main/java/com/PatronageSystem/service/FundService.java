package com.PatronageSystem.service;

import com.PatronageSystem.entity.Fund;
import java.util.List;

public interface FundService {
    
    List<Fund> findAll();
    
    Fund findById(Long id);
    
    Fund findByCode(String fundCode);
    
    List<Fund> findByCompanyId(Long companyId);
    
    List<Fund> findByManagerId(Long managerId);
    
    List<Fund> search(String keyword, String fundType, String riskLevel, List<Long> tagIds);
    
    List<String> getFundTags(Long fundId);
} 
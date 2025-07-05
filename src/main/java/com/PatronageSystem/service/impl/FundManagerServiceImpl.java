package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.FundManager;
import com.PatronageSystem.mapper.FundManagerMapper;
import com.PatronageSystem.service.FundManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FundManagerServiceImpl implements FundManagerService {
    
    @Autowired
    private FundManagerMapper fundManagerMapper;
    
    @Override
    public List<FundManager> findAll() {
        return fundManagerMapper.findAll();
    }
    
    @Override
    public FundManager findById(Long id) {
        return fundManagerMapper.findById(id);
    }
    
    @Override
    public FundManager findByCode(String managerCode) {
        return fundManagerMapper.findByCode(managerCode);
    }
    
    @Override
    public List<FundManager> findByCompanyId(Long companyId) {
        return fundManagerMapper.findByCompanyId(companyId);
    }
    
    @Override
    public List<FundManager> search(String keyword) {
        return fundManagerMapper.search(keyword);
    }
} 
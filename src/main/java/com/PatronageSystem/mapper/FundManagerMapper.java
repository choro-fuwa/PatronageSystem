package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FundManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FundManagerMapper {
    
    List<FundManager> findAll();
    
    FundManager findById(Long id);
    
    FundManager findByCode(String managerCode);
    
    List<FundManager> findByCompanyId(Long companyId);
    
    List<FundManager> search(@Param("keyword") String keyword);
} 
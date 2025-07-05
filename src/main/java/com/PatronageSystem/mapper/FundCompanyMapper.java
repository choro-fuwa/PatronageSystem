package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FundCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FundCompanyMapper {
    
    List<FundCompany> findAll();
    
    FundCompany findById(Long id);
    
    FundCompany findByCode(String companyCode);
    
    List<FundCompany> search(@Param("keyword") String keyword);
} 
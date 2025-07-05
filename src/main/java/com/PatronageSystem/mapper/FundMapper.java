package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.Fund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FundMapper {
    
    List<Fund> findAll();
    
    Fund findById(Long id);
    
    Fund findByCode(String fundCode);
    
    List<Fund> findByCompanyId(Long companyId);
    
    List<Fund> findByManagerId(Long managerId);
    
    List<Fund> search(@Param("keyword") String keyword, @Param("fundType") String fundType, 
                      @Param("riskLevel") String riskLevel, @Param("tagIds") List<Long> tagIds);
    
    List<String> getFundTags(Long fundId);
} 
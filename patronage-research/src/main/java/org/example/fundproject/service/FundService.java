package org.example.fundproject.service;

import org.example.fundproject.entity.Fund;
import org.example.fundproject.entity.FundCompany;
import org.example.fundproject.entity.FundManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FundService {
    Page<Fund> searchFunds(String fundCode, Set<String> tags, Pageable pageable);
    Fund getFundByCode(String fundCode);
    List<Fund> getFundsByCodes(List<String> fundCodes);
    void saveFundCombination(String combinationName, List<String> fundCodes, String username);

    List<FundCompany> getFundCompanies(String companyName, List<String> tags);

    List<FundManager> getFundManagers(String managerName, List<String> tags);

    Map<String, Object> getFundProfile(String fundCode);
}
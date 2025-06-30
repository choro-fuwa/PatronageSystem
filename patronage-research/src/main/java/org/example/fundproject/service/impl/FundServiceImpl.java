package org.example.fundproject.service.impl;


import org.example.fundproject.entity.*;
import org.example.fundproject.repository.*;
import org.example.fundproject.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FundServiceImpl implements FundService {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private FundCompanyRepository fundCompanyRepository;

    @Autowired
    private FundManagerRepository fundManagerRepository;

    @Override
    public Page<Fund> searchFunds(String fundCode, Set<String> tags, Pageable pageable) {
        if (fundCode != null && !fundCode.isEmpty()) {
            return fundRepository.findByFundCodeContaining(fundCode, pageable);
        }

        if (tags != null && !tags.isEmpty()) {
            return fundRepository.findByTagsIn(tags, pageable);
        }

        return (Page<Fund>) fundRepository.findAll();
    }

    @Override
    public Fund getFundByCode(String fundCode) {
        return fundRepository.findById(fundCode)
                .orElseThrow(() -> new RuntimeException("基金不存在"));
    }

    @Override
    public List<Fund> getFundsByCodes(List<String> fundCodes) {
        return fundRepository.findAllById(fundCodes);
    }

    @Override
    public void saveFundCombination(String combinationName, List<String> fundCodes, String creator) {
        FundCombination combination = new FundCombination();
        combination.setCombinationName(combinationName);
        combination.setFundCodes(fundCodes);
        combination.setCreator(creator);
        combination.setCreateTime(new Date());

        // 保存组合到数据库
        // combinationRepository.save(combination);
    }

    @Override
    public List<FundCompany> getFundCompanies(String companyName, List<String> tags) {
        if (companyName != null && !companyName.isEmpty()) {
            return Collections.singletonList(fundCompanyRepository.findByName(companyName));
        }
        if (tags != null && !tags.isEmpty()) {
            return fundCompanyRepository.findByTags(tags);
        }
        return fundCompanyRepository.findAll();
    }

    @Override
    public List<FundManager> getFundManagers(String managerName, List<String> tags) {
        if (managerName != null && !managerName.isEmpty()) {
            return Collections.singletonList(fundManagerRepository.findByName(managerName));
        }
        if (tags != null && !tags.isEmpty()) {
            return fundManagerRepository.findByTags(tags);
        }
        return fundManagerRepository.findAll();
    }

    @Override
    public Map<String, Object> getFundProfile(String fundCode) {
        Fund fund = fundRepository.findByFundCode(fundCode);
        Map<String, Object> profile = new HashMap<>();
        profile.put("basicInfo", fund);

        // 获取基金公司信息
        FundCompany company = fundCompanyRepository.findByName(fund.getCompanyName());
        profile.put("companyInfo", company);

        // 获取基金经理信息
        FundManager manager = fundManagerRepository.findByName(fund.getManagerName());
        profile.put("managerInfo", manager);

        // 获取基金业绩
        profile.put("performance", fundRepository.getPerformance(fundCode));

        // 获取基金持仓
        profile.put("positions", fundRepository.getPositions(fundCode));

        // 获取基金归因
        profile.put("attribution", fundRepository.getAttribution(fundCode));

        // 获取基金公告
        profile.put("announcements", fundRepository.getAnnouncements(fundCode));

        return profile;
    }

}
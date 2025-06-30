package org.example.fundproject.repository;

import org.example.fundproject.entity.FundCompany;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FundCompanyRepository {

    // 模拟数据库中的基金公司数据
    private static final List<FundCompany> ALL_COMPANIES = new ArrayList<>();

    static {
        // 初始化一些示例基金公司数据
        FundCompany company1 = new FundCompany();
        company1.setCompanyName("华夏基金管理有限公司");
        company1.setEstablishedDate("1998-04-09");
        company1.setRegisteredCapital("2.38亿元");
        company1.setManagementScale("1.2万亿元");
        company1.setTags((Set<String>) Arrays.asList("大型基金公司", "老牌基金公司", "综合实力强"));
        ALL_COMPANIES.add(company1);

        FundCompany company2 = new FundCompany();
        company2.setCompanyName("易方达基金管理有限公司");
        company2.setEstablishedDate("2001-04-17");
        company2.setRegisteredCapital("1.32亿元");
        company2.setManagementScale("1.5万亿元");
        company2.setTags((Set<String>) Arrays.asList("大型基金公司", "权益投资能力强", "创新能力突出"));
        ALL_COMPANIES.add(company2);

        // 添加更多示例基金公司...
    }

    public FundCompany findByName(String name) {
        return ALL_COMPANIES.stream()
                .filter(company -> company.getCompanyName().equals(name))
                .findFirst()
                .orElse(null);
    }



    public List<FundCompany> findAll() {
        return ALL_COMPANIES;
    }

    public List<FundCompany> findByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return ALL_COMPANIES;
        }
        return ALL_COMPANIES.stream()
                .filter(fund -> fund.getTags() != null &&
                        fund.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
    }
}

package org.example.fundproject.repository;


import org.example.fundproject.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FundRepository {

    // 模拟数据库中的基金数据
    private static final List<Fund> ALL_FUNDS = new ArrayList<>();

    static {
        // 初始化一些示例基金数据
        Fund fund1 = new Fund();
        fund1.setFundCode("000001");
        fund1.setFundName("华夏成长混合");
        fund1.setCompanyName("华夏基金管理有限公司");
        fund1.setManagerName("阳琨");
        fund1.setEstablishDate(LocalDate.parse("1998-04-09"));
        fund1.setFundType("混合型");
        fund1.setTags((Set<String>) Arrays.asList("大盘成长", "价值投资", "长期表现优秀"));
        ALL_FUNDS.add(fund1);

        Fund fund2 = new Fund();
        fund2.setFundCode("000002");
        fund2.setFundName("华夏债券A/B");
        fund2.setCompanyName("华夏基金管理有限公司");
        fund2.setManagerName("何家琪");
        fund2.setEstablishDate(LocalDate.parse("2002-10-23"));
        fund2.setFundType("债券型");
        fund2.setTags((Set<String>) Arrays.asList("纯债", "低风险", "收益稳定"));
        ALL_FUNDS.add(fund2);

        // 添加更多示例基金...
    }

    public Fund findByFundCode(String fundCode) {
        return ALL_FUNDS.stream()
                .filter(fund -> fund.getFundCode().equals(fundCode))
                .findFirst()
                .orElse(null);
    }

    public List<Fund> findByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return ALL_FUNDS;
        }
        return ALL_FUNDS.stream()
                .filter(fund -> fund.getTags() != null &&
                        fund.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
    }

    public List<Fund> findAll() {
        return ALL_FUNDS;
    }

    public Map<String, Object> getPerformance(String fundCode) {
        // 模拟获取基金业绩数据
        Map<String, Object> performance = new HashMap<>();
        performance.put("timePeriod", Arrays.asList("近1月", "近3月", "近6月", "近1年", "近3年"));
        performance.put("returnRate", Arrays.asList("5.2%", "8.7%", "12.3%", "21.5%", "68.9%"));
        performance.put("benchmarkReturn", Arrays.asList("3.1%", "6.2%", "9.5%", "18.7%", "56.3%"));
        performance.put("rankPercent", Arrays.asList("12%", "8%", "5%", "3%", "2%"));
        return performance;
    }

    public List<Map<String, Object>> getPositions(String fundCode) {
        // 模拟获取基金持仓数据
        List<Map<String, Object>> positions = new ArrayList<>();

        Map<String, Object> position1 = new HashMap<>();
        position1.put("stockCode", "600519");
        position1.put("stockName", "贵州茅台");
        position1.put("positionPercent", "8.5%");
        position1.put("industry", "食品饮料");
        positions.add(position1);

        Map<String, Object> position2 = new HashMap<>();
        position2.put("stockCode", "000858");
        position2.put("stockName", "五粮液");
        position2.put("positionPercent", "7.2%");
        position2.put("industry", "食品饮料");
        positions.add(position2);

        // 添加更多持仓...

        return positions;
    }

    public Map<String, Object> getAttribution(String fundCode) {
        // 模拟获取业绩归因数据
        Map<String, Object> attribution = new HashMap<>();
        attribution.put("stockSelection", "45%");
        attribution.put("sectorAllocation", "30%");
        attribution.put("styleTiming", "15%");
        attribution.put("otherFactors", "10%");
        return attribution;
    }

    public List<Map<String, Object>> getAnnouncements(String fundCode) {
        // 模拟获取基金公告数据
        List<Map<String, Object>> announcements = new ArrayList<>();

        Map<String, Object> announcement1 = new HashMap<>();
        announcement1.put("announcementDate", "2023-06-30");
        announcement1.put("title", "华夏成长混合基金2023年第二季度报告");
        announcement1.put("url", "#");
        announcements.add(announcement1);

        Map<String, Object> announcement2 = new HashMap<>();
        announcement2.put("announcementDate", "2023-06-15");
        announcement2.put("title", "关于华夏成长混合基金分红的公告");
        announcement2.put("url", "#");
        announcements.add(announcement2);

        // 添加更多公告...

        return announcements;
    }

    public Page<Fund> findByFundCodeContaining(String fundCode, Pageable pageable) {
        List<Fund> filteredFunds = ALL_FUNDS.stream()
                .filter(fund -> fund.getFundCode().contains(fundCode))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(filteredFunds, pageable, filteredFunds.size());
    }

    public Page<Fund> findByTagsIn(Set<String> tags, Pageable pageable) {
        List<Fund> filteredFunds = ALL_FUNDS.stream()
                .filter(fund -> fund.getTags() != null &&
                        !Collections.disjoint(fund.getTags(), tags))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(filteredFunds, pageable, filteredFunds.size());
    }

    public Optional<Fund> findById(String fundCode) {
        return ALL_FUNDS.stream()
                .filter(fund -> fund.getFundCode().equals(fundCode))
                .findFirst();
    }

    public List<Fund> findAllById(List<String> fundCodes) {
        return ALL_FUNDS.stream()
                .filter(fund -> fundCodes.contains(fund.getFundCode()))
                .collect(Collectors.toList());
    }
}
package org.example.fundproject.repository;

import org.example.fundproject.entity.FundManager;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FundManagerRepository {

    // 模拟数据库中的基金经理数据
    private static final List<FundManager> ALL_MANAGERS = new ArrayList<>();

    static {
        // 初始化一些示例基金经理数据
        FundManager manager1 = new FundManager();
        manager1.setManagerName("阳琨");
        manager1.setWorkingCompany("华夏基金管理有限公司");
        manager1.setYearsOfExperience(15);
        manager1.setManagementScale("350亿元");
        manager1.setBestReturn("356%");
        manager1.setTags((Set<String>) Arrays.asList("价值投资", "长期投资", "大盘成长"));
        manager1.setManagedFunds(Arrays.asList("000001", "000003", "000005").toString());
        ALL_MANAGERS.add(manager1);

        FundManager manager2 = new FundManager();
        manager2.setManagerName("何家琪");
        manager2.setWorkingCompany("华夏基金管理有限公司");
        manager2.setYearsOfExperience(10);
        manager2.setManagementScale("280亿元");
        manager2.setBestReturn("128%");
        manager2.setTags((Set<String>) Arrays.asList("债券投资", "稳健风格", "低风险"));
        manager2.setManagedFunds(Arrays.asList("000002", "000004", "000006").toString());
        ALL_MANAGERS.add(manager2);

        // 添加更多示例基金经理...
    }

    public FundManager findByName(String name) {
        return ALL_MANAGERS.stream()
                .filter(manager -> manager.getManagerName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<FundManager> findByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return ALL_MANAGERS;
        }
        return ALL_MANAGERS.stream()
                .filter(manager -> manager.getTags() != null &&
                        manager.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
    }

    public List<FundManager> findAll() {
        return ALL_MANAGERS;
    }
}
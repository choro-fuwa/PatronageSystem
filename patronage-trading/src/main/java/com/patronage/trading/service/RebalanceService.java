package com.patronage.trading.service;

import com.patronage.trading.entity.RebalanceOrder;

public interface RebalanceService {
    // 创建调仓指令
    void createRebalanceOrder(String portfolioId);

    // 提交审核
    void submitForReview(Long orderId);
}

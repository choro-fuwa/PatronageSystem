package com.patronage.trading.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.util.Map;

@Data
@TableName(value = "trade_orders", autoResultMap = true)
public class TradeOrder {
    private Long tradeId;          // 交易单ID
    private Long orderId;          // 关联指令ID
    private String type;           // 类型：normal/error_fix/account_rebalance

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> details; // JSON格式交易细节

    private String status;         // 状态：pending/executed/rejected
}
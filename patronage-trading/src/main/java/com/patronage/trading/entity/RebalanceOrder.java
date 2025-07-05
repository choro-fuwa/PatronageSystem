package com.patronage.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("rebalance_orders") // 指定对应的数据库表
public class RebalanceOrder {
    @TableId(type = IdType.AUTO) // 主键自动增长
    private Long orderId;          // 调仓指令ID
    private String portfolioId;    // 关联组合ID
    private String status;         // 状态：草稿/待审核/已确认/已完成
    private LocalDateTime createdAt; // 创建时间
    private String operatorId;      // 操作人ID
}
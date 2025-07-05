package com.patronage.trading.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.patronage.trading.entity.TradeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeOrderMapper {
    // 查询待处理交易单
    List<TradeOrder> selectPendingTrades();

    // 更新交易单状态
    int updateTradeStatus(@Param("tradeId") Long tradeId,
                          @Param("newStatus") String newStatus);
}

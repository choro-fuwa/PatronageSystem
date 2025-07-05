package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.TradeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TradeOrderMapper {
    List<TradeOrder> selectAll();

    TradeOrder selectById(@Param("id") Long id);

    TradeOrder selectByOrderCode(@Param("orderCode") String orderCode);

    List<TradeOrder> selectByAccountId(@Param("accountId") Long accountId);

    List<TradeOrder> selectByStrategyId(@Param("strategyId") Long strategyId);

    List<TradeOrder> selectByStatus(@Param("orderStatus") String orderStatus);

    List<TradeOrder> selectByBizType(@Param("orderBizType") String orderBizType);

    List<TradeOrder> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    int insert(TradeOrder tradeOrder);

    int update(TradeOrder tradeOrder);

    int deleteById(@Param("id") Long id);

    int updateOrderStatus(@Param("id") Long id, @Param("orderStatus") String orderStatus);

    int executeOrder(@Param("id") Long id, @Param("orderStatus") String orderStatus);

    int cancelOrder(@Param("id") Long id, @Param("orderStatus") String orderStatus, @Param("cancelReason") String cancelReason);

    List<TradeOrder> searchOrders(
        @Param("orderStatus") String orderStatus,
        @Param("orderType") String orderType,
        @Param("orderBizType") String orderBizType,
        @Param("accountId") Long accountId,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime
    );
}

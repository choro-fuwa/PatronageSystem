package com.patronage.trading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.patronage.trading.entity.RebalanceOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RebalanceOrderMapper extends BaseMapper<RebalanceOrder> {
    // 自定义查询：按状态筛选指令
    @Select("SELECT * FROM rebalance_orders WHERE status = #{status}")
    List<RebalanceOrder> selectByStatus(@Param("status") String status);
}
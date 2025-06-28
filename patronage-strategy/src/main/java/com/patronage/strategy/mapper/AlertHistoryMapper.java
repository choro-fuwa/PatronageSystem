package com.patronage.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.patronage.strategy.entity.AlertHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预警历史Mapper接口
 */
@Mapper
public interface AlertHistoryMapper extends BaseMapper<AlertHistory> {
} 
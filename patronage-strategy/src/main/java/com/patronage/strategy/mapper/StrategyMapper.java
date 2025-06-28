package com.patronage.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.patronage.strategy.entity.Strategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * 策略Mapper接口
 */
@Mapper
public interface StrategyMapper extends BaseMapper<Strategy> {
} 
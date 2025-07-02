package com.patronage.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.patronage.strategy.entity.BacktestResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回测结果Mapper接口
 */
@Mapper
public interface BacktestResultMapper extends BaseMapper<BacktestResult> {
} 
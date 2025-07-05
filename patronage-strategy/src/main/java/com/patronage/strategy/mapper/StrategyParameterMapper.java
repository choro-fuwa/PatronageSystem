package com.patronage.strategy.mapper;

import com.patronage.strategy.entity.StrategyParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略参数Mapper接口
 */
@Mapper
public interface StrategyParameterMapper {

    /**
     * 根据策略ID查询参数
     */
    List<StrategyParameter> selectByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据策略ID和参数名称查询
     */
    StrategyParameter selectByStrategyIdAndParamName(@Param("strategyId") Long strategyId,
                                                     @Param("paramName") String paramName);

    /**
     * 新增参数
     */
    int insert(StrategyParameter parameter);

    /**
     * 批量新增参数
     */
    int batchInsert(@Param("parameters") List<StrategyParameter> parameters);

    /**
     * 更新参数
     */
    int update(StrategyParameter parameter);

    /**
     * 删除参数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据策略ID删除参数
     */
    int deleteByStrategyId(@Param("strategyId") Long strategyId);
}

package com.patronage.strategy.mapper;

import com.patronage.strategy.entity.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 策略Mapper接口
 */
@Mapper
public interface StrategyMapper {

    /**
     * 查询所有策略
     */
    List<Strategy> selectAll();

    /**
     * 根据ID查询策略
     */
    Strategy selectById(@Param("id") Long id);

    /**
     * 根据策略代码查询
     */
    Strategy selectByStrategyCode(@Param("strategyCode") String strategyCode);

    /**
     * 根据创建者查询策略
     */
    List<Strategy> selectByCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 条件查询策略
     */
    List<Strategy> selectByCondition(@Param("strategyType") String strategyType,
                                     @Param("riskLevel") String riskLevel,
                                     @Param("isPublic") Integer isPublic);

    /**
     * 关键词搜索策略
     */
    List<Strategy> selectByKeyword(@Param("keyword") String keyword);

    /**
     * 新增策略
     */
    int insert(Strategy strategy);

    /**
     * 更新策略
     */
    int update(Strategy strategy);

    /**
     * 删除策略
     */
    int deleteById(@Param("id") Long id);

    /**
     * 检查策略代码是否存在
     */
    int checkStrategyCodeExists(@Param("strategyCode") String strategyCode);

    /**
     * 获取策略统计信息
     */
    Map<String, Object> selectStrategyStats();

    /**
     * 按策略类型统计
     */
    List<Map<String, Object>> selectStrategyCountByType();

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> selectStrategyCountByRiskLevel();
}
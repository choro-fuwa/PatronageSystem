package src.java.com.patronage.strategy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import src.java.com.patronage.strategy.entity.Strategy;

import java.util.List;
import java.util.Map;

/**
 * 策略服务接口
 */
public interface StrategyService extends IService<Strategy> {

    /**
     * 分页查询策略列表
     */
    IPage<Strategy> getStrategyPage(Integer pageNum, Integer pageSize, String strategyName, Integer strategyType, Integer status);

    /**
     * 创建策略
     */
    boolean createStrategy(Strategy strategy);

    /**
     * 更新策略
     */
    boolean updateStrategy(Strategy strategy);

    /**
     * 删除策略
     */
    boolean deleteStrategy(Long id);

    /**
     * 启动策略
     */
    boolean startStrategy(Long id);

    /**
     * 停止策略
     */
    boolean stopStrategy(Long id);

    /**
     * 暂停策略
     */
    boolean pauseStrategy(Long id);

    /**
     * 获取策略详情
     */
    Strategy getStrategyDetail(Long id);

    /**
     * 获取所有运行中的策略
     */
    List<Strategy> getRunningStrategies();

    /**
     * 获取策略总数
     */
    Integer getTotalCount();

    /**
     * 获取运行中的策略数量
     */
    Integer getRunningCount();

    /**
     * 获取暂停的策略数量
     */
    Integer getPausedCount();

    /**
     * 获取停止的策略数量
     */
    Integer getStoppedCount();

    /**
     * 获取策略持仓
     */
    Map<String, Object> getStrategyPositions(Long id);

    /**
     * 获取策略风险指标
     */
    Map<String, Object> getStrategyRiskMetrics(Long id);

    /**
     * 执行再平衡
     */
    boolean executeRebalance(Long id, String adjustmentType);

    /**
     * 手动调整持仓
     */
    boolean adjustPositions(Long id, Map<String, Object> adjustmentData);

    /**
     * 获取调仓历史
     */
    List<Map<String, Object>> getAdjustmentHistory(Long id);

    /**
     * 启动模拟运行
     */
    boolean startSimulation(Long id);

    /**
     * 复制策略
     */
    boolean copyStrategy(Long id, String name);

    /**
     * 导出策略配置
     */
    Map<String, Object> exportStrategyConfig(Long id);

    /**
     * 导入策略配置
     */
    boolean importStrategyConfig(Map<String, Object> config);

    /**
     * 策略审核
     */
    boolean approveStrategy(Long id, Map<String, Object> approvalData);

    /**
     * 策略拒绝
     */
    boolean rejectStrategy(Long id, Map<String, Object> rejectionData);

    /**
     * 调整策略
     */
    boolean adjustStrategy(Map<String, Object> adjustmentData);
} 
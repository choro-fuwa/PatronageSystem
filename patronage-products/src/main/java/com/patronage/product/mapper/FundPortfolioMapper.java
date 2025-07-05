package main.java.com.patronage.product.mapper;

import com.advisorboot.entity.FundPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基金投资组合Mapper接口
 */
@Mapper
public interface FundPortfolioMapper {
    
    /**
     * 根据用户ID查询投资组合列表
     */
    List<FundPortfolio> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据ID查询投资组合详情
     */
    FundPortfolio findById(@Param("id") Long id);
    
    /**
     * 根据组合名称查询
     */
    FundPortfolio findByPortfolioName(@Param("portfolioName") String portfolioName);
    
    /**
     * 根据用户ID和组合名称查询
     */
    FundPortfolio findByUserIdAndName(@Param("userId") Long userId, @Param("portfolioName") String portfolioName);
    
    /**
     * 根据组合类型查询
     */
    List<FundPortfolio> findByPortfolioType(@Param("portfolioType") String portfolioType);
    
    /**
     * 根据状态查询
     */
    List<FundPortfolio> findByStatus(@Param("status") Integer status);
    
    /**
     * 根据用户ID和状态查询
     */
    List<FundPortfolio> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 条件查询
     */
    List<FundPortfolio> findByCondition(@Param("userId") Long userId, 
                                       @Param("portfolioType") String portfolioType,
                                       @Param("status") Integer status,
                                       @Param("keyword") String keyword);
    
    /**
     * 模糊搜索
     */
    List<FundPortfolio> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 查询所有投资组合
     */
    List<FundPortfolio> selectAll();
    
    /**
     * 分页查询
     */
    List<FundPortfolio> selectByPage(@Param("offset") Integer offset, 
                                    @Param("limit") Integer limit,
                                    @Param("userId") Long userId,
                                    @Param("portfolioType") String portfolioType,
                                    @Param("status") Integer status);
    
    /**
     * 统计用户投资组合数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计投资组合总数
     */
    int countAll();
    
    /**
     * 条件统计
     */
    int countByCondition(@Param("userId") Long userId, 
                        @Param("portfolioType") String portfolioType,
                        @Param("status") Integer status);
    
    /**
     * 获取投资组合统计信息
     */
    Map<String, Object> selectPortfolioStats(@Param("userId") Long userId);
    
    /**
     * 按类型统计投资组合数量
     */
    List<Map<String, Object>> selectPortfolioCountByType(@Param("userId") Long userId);
    
    /**
     * 插入投资组合
     */
    int insert(FundPortfolio portfolio);
    
    /**
     * 更新投资组合
     */
    int update(FundPortfolio portfolio);
    
    /**
     * 更新状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 删除投资组合
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量删除
     */
    int batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 根据用户ID删除所有投资组合
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 检查组合名称是否存在
     */
    int checkPortfolioNameExists(@Param("userId") Long userId, @Param("portfolioName") String portfolioName);
    
    /**
     * 获取最近创建的投资组合
     */
    List<FundPortfolio> selectRecentPortfolios(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 获取活跃的投资组合（有持仓的）
     */
    List<FundPortfolio> selectActivePortfolios(@Param("userId") Long userId);
} 
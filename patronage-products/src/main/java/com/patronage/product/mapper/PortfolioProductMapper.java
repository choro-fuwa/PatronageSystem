package main.java.com.patronage.product.mapper;

import com.patronage.product.entity.PortfolioProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合产品Mapper接口
 */
@Mapper
public interface PortfolioProductMapper {
    
    /**
     * 查询所有产品
     */
    List<PortfolioProduct> selectAll();
    
    /**
     * 根据ID查询产品
     */
    PortfolioProduct selectById(@Param("id") Long id);
    
    /**
     * 根据产品代码查询产品
     */
    PortfolioProduct selectByProductCode(@Param("productCode") String productCode);
    
    /**
     * 根据状态查询产品
     */
    List<PortfolioProduct> selectByStatus(@Param("productStatus") String productStatus);
    
    /**
     * 根据产品类型查询产品
     */
    List<PortfolioProduct> selectByProductType(@Param("productType") String productType);
    
    /**
     * 根据风险等级查询产品
     */
    List<PortfolioProduct> selectByRiskLevel(@Param("riskLevel") String riskLevel);
    
    /**
     * 根据创建人查询产品
     */
    List<PortfolioProduct> selectByCreator(@Param("creatorId") Long creatorId);
    
    /**
     * 搜索产品
     */
    List<PortfolioProduct> searchProducts(@Param("keyword") String keyword);
    
    /**
     * 插入产品
     */
    int insert(PortfolioProduct product);
    
    /**
     * 更新产品
     */
    int update(PortfolioProduct product);
    
    /**
     * 删除产品
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新产品状态
     */
    int updateStatus(@Param("id") Long id, @Param("productStatus") String productStatus);
    
    /**
     * 更新审核状态
     */
    int updateReviewStatus(@Param("id") Long id, @Param("reviewStatus") String reviewStatus, 
                          @Param("reviewerId") Long reviewerId, @Param("reviewComment") String reviewComment);
} 
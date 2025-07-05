package main.java.com.patronage.product.mapper;

import com.patronage.product.entity.ProductPerformance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 产品业绩Mapper接口
 */
@Mapper
public interface ProductPerformanceMapper {
    
    /**
     * 根据产品ID查询业绩数据
     */
    List<ProductPerformance> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据产品ID和日期范围查询业绩数据
     */
    List<ProductPerformance> selectByProductIdAndDateRange(@Param("productId") Long productId, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
    
    /**
     * 查询产品最新业绩数据
     */
    ProductPerformance selectLatestByProductId(@Param("productId") Long productId);
    
    /**
     * 查询产品历史最高净值
     */
    ProductPerformance selectMaxNavByProductId(@Param("productId") Long productId);
    
    /**
     * 查询产品历史最低净值
     */
    ProductPerformance selectMinNavByProductId(@Param("productId") Long productId);
    
    /**
     * 插入业绩数据
     */
    int insert(ProductPerformance performance);
    
    /**
     * 更新业绩数据
     */
    int update(ProductPerformance performance);
    
    /**
     * 删除业绩数据
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量插入业绩数据
     */
    int batchInsert(@Param("performances") List<ProductPerformance> performances);
} 
package main.java.com.patronage.product.service;

import com.patronage.product.entity.PortfolioProduct;
import com.patronage.product.entity.ProductDocument;
import com.patronage.product.entity.ProductPerformance;

import java.time.LocalDate;
import java.util.List;

/**
 * 组合产品Service接口
 */
public interface PortfolioProductService {
    
    /**
     * 查询所有产品
     */
    List<PortfolioProduct> getAllProducts();
    
    /**
     * 根据ID查询产品
     */
    PortfolioProduct getProductById(Long id);
    
    /**
     * 根据产品代码查询产品
     */
    PortfolioProduct getProductByCode(String productCode);
    
    /**
     * 根据状态查询产品
     */
    List<PortfolioProduct> getProductsByStatus(String productStatus);
    
    /**
     * 根据产品类型查询产品
     */
    List<PortfolioProduct> getProductsByType(String productType);
    
    /**
     * 根据风险等级查询产品
     */
    List<PortfolioProduct> getProductsByRiskLevel(String riskLevel);
    
    /**
     * 根据创建人查询产品
     */
    List<PortfolioProduct> getProductsByCreator(Long creatorId);
    
    /**
     * 搜索产品
     */
    List<PortfolioProduct> searchProducts(String keyword);
    
    /**
     * 创建产品
     */
    boolean createProduct(PortfolioProduct product);
    
    /**
     * 更新产品
     */
    boolean updateProduct(PortfolioProduct product);
    
    /**
     * 删除产品
     */
    boolean deleteProduct(Long id);
    
    /**
     * 更新产品状态
     */
    boolean updateProductStatus(Long id, String productStatus);
    
    /**
     * 审核产品
     */
    boolean reviewProduct(Long id, String reviewStatus, Long reviewerId, String reviewComment);
    
    /**
     * 获取产品业绩数据
     */
    List<ProductPerformance> getProductPerformance(Long productId);
    
    /**
     * 获取产品业绩数据（日期范围）
     */
    List<ProductPerformance> getProductPerformanceByDateRange(Long productId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取产品最新业绩
     */
    ProductPerformance getLatestPerformance(Long productId);
    
    /**
     * 添加业绩数据
     */
    boolean addPerformance(ProductPerformance performance);
    
    /**
     * 批量添加业绩数据
     */
    boolean batchAddPerformance(List<ProductPerformance> performances);
    
    /**
     * 获取产品文档
     */
    List<ProductDocument> getProductDocuments(Long productId);
    
    /**
     * 根据类型获取产品文档
     */
    List<ProductDocument> getProductDocumentsByType(Long productId, String documentType);
    
    /**
     * 添加产品文档
     */
    boolean addDocument(ProductDocument document);
    
    /**
     * 删除产品文档
     */
    boolean deleteDocument(Long documentId);
    
    /**
     * 更新文档状态
     */
    boolean updateDocumentStatus(Long documentId, Integer isActive);
} 
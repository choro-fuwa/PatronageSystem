package com.PatronageSystem.service;

import com.PatronageSystem.entity.PortfolioProduct;
import com.PatronageSystem.entity.ProductPerformance;
import com.PatronageSystem.entity.ProductDocument;
import com.PatronageSystem.mapper.PortfolioProductMapper;
import com.PatronageSystem.mapper.ProductPerformanceMapper;
import com.PatronageSystem.mapper.ProductDocumentMapper;
import com.PatronageSystem.service.impl.PortfolioProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioProductServiceTest {

    @Mock
    private PortfolioProductMapper portfolioProductMapper;

    @Mock
    private ProductPerformanceMapper productPerformanceMapper;

    @Mock
    private ProductDocumentMapper productDocumentMapper;

    @InjectMocks
    private PortfolioProductServiceImpl portfolioProductService;

    // 测试数据
    private final PortfolioProduct product1 = createPortfolioProduct(1L, "PP001", "稳健配置组合产品", "稳健配置", 
            "FOF", "中", new BigDecimal("0.0800"), new BigDecimal("100000.00"), new BigDecimal("10000000.00"),
            new BigDecimal("0.0150"), new BigDecimal("0.0020"), new BigDecimal("0.0000"), new BigDecimal("0.0050"),
            "中证500指数*50%+中债总财富指数*50%", "采用稳健配置策略，平衡股票和债券投资比例。", 
            "主要投资于股票型基金、债券型基金，适当配置货币市场基金。", "ACTIVE", 
            LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), 1L, 2L, "APPROVED", "产品审核通过");

    private final PortfolioProduct product2 = createPortfolioProduct(2L, "PP002", "成长进取组合产品", "成长进取", 
            "组合", "高", new BigDecimal("0.1200"), new BigDecimal("200000.00"), new BigDecimal("20000000.00"),
            new BigDecimal("0.0200"), new BigDecimal("0.0020"), new BigDecimal("0.0000"), new BigDecimal("0.0080"),
            "沪深300指数*70%+中债总财富指数*30%", "采用成长进取策略，重点配置股票型基金。", 
            "主要投资于股票型基金，适当配置债券型基金和货币市场基金。", "PENDING", 
            LocalDate.of(2024, 2, 1), LocalDate.of(2025, 2, 1), 1L, null, "PENDING", null);

    private final PortfolioProduct product3 = createPortfolioProduct(3L, "PP003", "价值投资组合产品", "价值投资", 
            "定制", "中", new BigDecimal("0.1000"), new BigDecimal("500000.00"), new BigDecimal("50000000.00"),
            new BigDecimal("0.0180"), new BigDecimal("0.0020"), new BigDecimal("0.0000"), new BigDecimal("0.0060"),
            "中证500指数*60%+中债总财富指数*40%", "采用价值投资策略，精选具有投资价值的基金产品。", 
            "主要投资于价值型股票基金、债券型基金，适当配置货币市场基金。", "DRAFT", 
            LocalDate.of(2024, 3, 1), LocalDate.of(2025, 3, 1), 1L, null, "PENDING", null);

    private PortfolioProduct createPortfolioProduct(Long id, String productCode, String productName, String productShortName,
                                                   String productType, String riskLevel, BigDecimal targetReturn, 
                                                   BigDecimal minInvestment, BigDecimal maxInvestment, BigDecimal managementFee,
                                                   BigDecimal custodianFee, BigDecimal subscriptionFee, BigDecimal redemptionFee,
                                                   String benchmark, String investmentStrategy, String investmentScope,
                                                   String productStatus, LocalDate launchDate, LocalDate maturityDate,
                                                   Long creatorId, Long reviewerId, String reviewStatus, String reviewComment) {
        PortfolioProduct product = new PortfolioProduct();
        product.setId(id);
        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setProductShortName(productShortName);
        product.setProductType(productType);
        product.setRiskLevel(riskLevel);
        product.setTargetReturn(targetReturn);
        product.setMinInvestment(minInvestment);
        product.setMaxInvestment(maxInvestment);
        product.setManagementFee(managementFee);
        product.setCustodianFee(custodianFee);
        product.setSubscriptionFee(subscriptionFee);
        product.setRedemptionFee(redemptionFee);
        product.setBenchmark(benchmark);
        product.setInvestmentStrategy(investmentStrategy);
        product.setInvestmentScope(investmentScope);
        product.setProductStatus(productStatus);
        product.setLaunchDate(launchDate);
        product.setMaturityDate(maturityDate);
        product.setCreatorId(creatorId);
        product.setReviewerId(reviewerId);
        product.setReviewStatus(reviewStatus);
        product.setReviewComment(reviewComment);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        return product;
    }

    private ProductPerformance createProductPerformance(Long id, Long productId, LocalDate navDate, BigDecimal nav,
                                                       BigDecimal accumulatedNav, BigDecimal dailyReturn, BigDecimal totalReturn) {
        ProductPerformance performance = new ProductPerformance();
        performance.setId(id);
        performance.setProductId(productId);
        performance.setNavDate(navDate);
        performance.setNav(nav);
        performance.setAccumulatedNav(accumulatedNav);
        performance.setDailyReturn(dailyReturn);
        performance.setTotalReturn(totalReturn);
        performance.setCreateTime(LocalDateTime.now());
        return performance;
    }

    private ProductDocument createProductDocument(Long id, Long productId, String documentType, String documentName,
                                                 String documentUrl, Long documentSize, Long uploaderId) {
        ProductDocument document = new ProductDocument();
        document.setId(id);
        document.setProductId(productId);
        document.setDocumentType(documentType);
        document.setDocumentName(documentName);
        document.setDocumentUrl(documentUrl);
        document.setDocumentSize(documentSize);
        document.setVersion("1.0");
        document.setIsActive(1);
        document.setUploaderId(uploaderId);
        document.setUploadTime(LocalDateTime.now());
        return document;
    }

    // ==================== 产品管理测试 ====================

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectAll()).thenReturn(Arrays.asList(product1, product2, product3));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getAllProducts();

        // 验证结果
        assertEquals(3, result.size());
        assertEquals("PP001", result.get(0).getProductCode());
        assertEquals("稳健配置组合产品", result.get(0).getProductName());
        assertEquals("PP002", result.get(1).getProductCode());
        assertEquals("成长进取组合产品", result.get(1).getProductName());
        verify(portfolioProductMapper, times(1)).selectAll();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getAllProducts();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).selectAll();
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectById(1L)).thenReturn(product1);

        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("PP001", result.getProductCode());
        assertEquals("稳健配置组合产品", result.getProductName());
        assertEquals("FOF", result.getProductType());
        assertEquals("中", result.getRiskLevel());
        assertEquals("ACTIVE", result.getProductStatus());
        verify(portfolioProductMapper, times(1)).selectById(1L);
    }

    @Test
    void getProductById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectById(999L)).thenReturn(null);

        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductById(999L);

        // 验证结果
        assertNull(result);
        verify(portfolioProductMapper, times(1)).selectById(999L);
    }

    @Test
    void getProductByCode_ShouldReturnProduct() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByProductCode("PP001")).thenReturn(product1);

        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductByCode("PP001");

        // 验证结果
        assertNotNull(result);
        assertEquals("PP001", result.getProductCode());
        assertEquals("稳健配置组合产品", result.getProductName());
        verify(portfolioProductMapper, times(1)).selectByProductCode("PP001");
    }

    @Test
    void getProductByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByProductCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(portfolioProductMapper, times(1)).selectByProductCode("NONEXISTENT");
    }

    @Test
    void getProductsByStatus_ShouldReturnProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByStatus("ACTIVE")).thenReturn(Arrays.asList(product1));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByStatus("ACTIVE");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("PP001", result.get(0).getProductCode());
        assertEquals("ACTIVE", result.get(0).getProductStatus());
        verify(portfolioProductMapper, times(1)).selectByStatus("ACTIVE");
    }

    @Test
    void getProductsByType_ShouldReturnProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByProductType("FOF")).thenReturn(Arrays.asList(product1));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByType("FOF");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("PP001", result.get(0).getProductCode());
        assertEquals("FOF", result.get(0).getProductType());
        verify(portfolioProductMapper, times(1)).selectByProductType("FOF");
    }

    @Test
    void getProductsByRiskLevel_ShouldReturnProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByRiskLevel("中")).thenReturn(Arrays.asList(product1, product3));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByRiskLevel("中");

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "中".equals(p.getRiskLevel())));
        verify(portfolioProductMapper, times(1)).selectByRiskLevel("中");
    }

    @Test
    void getProductsByCreator_ShouldReturnProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByCreator(1L)).thenReturn(Arrays.asList(product1, product2, product3));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByCreator(1L);

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(p -> p.getCreatorId().equals(1L)));
        verify(portfolioProductMapper, times(1)).selectByCreator(1L);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.searchProducts("稳健")).thenReturn(Arrays.asList(product1));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.searchProducts("稳健");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("PP001", result.get(0).getProductCode());
        assertTrue(result.get(0).getProductName().contains("稳健"));
        verify(portfolioProductMapper, times(1)).searchProducts("稳健");
    }

    @Test
    void createProduct_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(portfolioProductMapper.insert(any(PortfolioProduct.class))).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.createProduct(product1);

        // 验证结果
        assertTrue(result);
        verify(portfolioProductMapper, times(1)).insert(product1);
    }

    @Test
    void createProduct_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(portfolioProductMapper.insert(any(PortfolioProduct.class))).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.createProduct(product1);

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).insert(product1);
    }

    @Test
    void updateProduct_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(portfolioProductMapper.update(any(PortfolioProduct.class))).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.updateProduct(product1);

        // 验证结果
        assertTrue(result);
        verify(portfolioProductMapper, times(1)).update(product1);
    }

    @Test
    void deleteProduct_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(portfolioProductMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.deleteProduct(1L);

        // 验证结果
        assertTrue(result);
        verify(portfolioProductMapper, times(1)).deleteById(1L);
    }

    @Test
    void updateProductStatus_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(portfolioProductMapper.updateStatus(1L, "ACTIVE")).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.updateProductStatus(1L, "ACTIVE");

        // 验证结果
        assertTrue(result);
        verify(portfolioProductMapper, times(1)).updateStatus(1L, "ACTIVE");
    }

    @Test
    void reviewProduct_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(portfolioProductMapper.updateReviewStatus(1L, "APPROVED", 2L, "审核通过")).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.reviewProduct(1L, "APPROVED", 2L, "审核通过");

        // 验证结果
        assertTrue(result);
        verify(portfolioProductMapper, times(1)).updateReviewStatus(1L, "APPROVED", 2L, "审核通过");
    }

    // ==================== 业绩管理测试 ====================

    @Test
    void getProductPerformance_ShouldReturnPerformanceList() {
        // 准备测试数据
        ProductPerformance performance1 = createProductPerformance(1L, 1L, LocalDate.now().minusDays(1), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));
        ProductPerformance performance2 = createProductPerformance(2L, 1L, LocalDate.now(), 
                new BigDecimal("1.0879"), new BigDecimal("1.0879"), new BigDecimal("0.0021"), new BigDecimal("0.0879"));

        // 模拟Mapper行为
        when(productPerformanceMapper.selectByProductId(1L)).thenReturn(Arrays.asList(performance1, performance2));

        // 调用Service方法
        List<ProductPerformance> result = portfolioProductService.getProductPerformance(1L);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(1L, result.get(1).getProductId());
        verify(productPerformanceMapper, times(1)).selectByProductId(1L);
    }

    @Test
    void getProductPerformanceByDateRange_ShouldReturnPerformanceList() {
        // 准备测试数据
        ProductPerformance performance = createProductPerformance(1L, 1L, LocalDate.now(), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        // 模拟Mapper行为
        when(productPerformanceMapper.selectByProductIdAndDateRange(1L, startDate, endDate))
                .thenReturn(Arrays.asList(performance));

        // 调用Service方法
        List<ProductPerformance> result = portfolioProductService.getProductPerformanceByDateRange(1L, startDate, endDate);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProductId());
        verify(productPerformanceMapper, times(1)).selectByProductIdAndDateRange(1L, startDate, endDate);
    }

    @Test
    void getLatestPerformance_ShouldReturnLatestPerformance() {
        // 准备测试数据
        ProductPerformance performance = createProductPerformance(1L, 1L, LocalDate.now(), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));

        // 模拟Mapper行为
        when(productPerformanceMapper.selectLatestByProductId(1L)).thenReturn(performance);

        // 调用Service方法
        ProductPerformance result = portfolioProductService.getLatestPerformance(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals(LocalDate.now(), result.getNavDate());
        verify(productPerformanceMapper, times(1)).selectLatestByProductId(1L);
    }

    @Test
    void addPerformance_ShouldReturnTrue() {
        // 准备测试数据
        ProductPerformance performance = createProductPerformance(1L, 1L, LocalDate.now(), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));

        // 模拟Mapper行为
        when(productPerformanceMapper.insert(any(ProductPerformance.class))).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.addPerformance(performance);

        // 验证结果
        assertTrue(result);
        verify(productPerformanceMapper, times(1)).insert(performance);
    }

    @Test
    void batchAddPerformance_ShouldReturnTrue() {
        // 准备测试数据
        ProductPerformance performance1 = createProductPerformance(1L, 1L, LocalDate.now().minusDays(1), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));
        ProductPerformance performance2 = createProductPerformance(2L, 1L, LocalDate.now(), 
                new BigDecimal("1.0879"), new BigDecimal("1.0879"), new BigDecimal("0.0021"), new BigDecimal("0.0879"));
        List<ProductPerformance> performances = Arrays.asList(performance1, performance2);

        // 模拟Mapper行为
        when(productPerformanceMapper.batchInsert(performances)).thenReturn(2);

        // 调用Service方法
        boolean result = portfolioProductService.batchAddPerformance(performances);

        // 验证结果
        assertTrue(result);
        verify(productPerformanceMapper, times(1)).batchInsert(performances);
    }

    // ==================== 文档管理测试 ====================

    @Test
    void getProductDocuments_ShouldReturnDocumentList() {
        // 准备测试数据
        ProductDocument document1 = createProductDocument(1L, 1L, "PROSPECTUS", "招募说明书.pdf", 
                "/documents/PP001_prospectus.pdf", 1024000L, 1L);
        ProductDocument document2 = createProductDocument(2L, 1L, "INVESTMENT_GUIDE", "投资指南.pdf", 
                "/documents/PP001_guide.pdf", 512000L, 1L);

        // 模拟Mapper行为
        when(productDocumentMapper.selectByProductId(1L)).thenReturn(Arrays.asList(document1, document2));

        // 调用Service方法
        List<ProductDocument> result = portfolioProductService.getProductDocuments(1L);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(1L, result.get(1).getProductId());
        verify(productDocumentMapper, times(1)).selectByProductId(1L);
    }

    @Test
    void getProductDocumentsByType_ShouldReturnDocumentList() {
        // 准备测试数据
        ProductDocument document = createProductDocument(1L, 1L, "PROSPECTUS", "招募说明书.pdf", 
                "/documents/PP001_prospectus.pdf", 1024000L, 1L);

        // 模拟Mapper行为
        when(productDocumentMapper.selectByProductIdAndType(1L, "PROSPECTUS")).thenReturn(Arrays.asList(document));

        // 调用Service方法
        List<ProductDocument> result = portfolioProductService.getProductDocumentsByType(1L, "PROSPECTUS");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals("PROSPECTUS", result.get(0).getDocumentType());
        verify(productDocumentMapper, times(1)).selectByProductIdAndType(1L, "PROSPECTUS");
    }

    @Test
    void addDocument_ShouldReturnTrue() {
        // 准备测试数据
        ProductDocument document = createProductDocument(1L, 1L, "PROSPECTUS", "招募说明书.pdf", 
                "/documents/PP001_prospectus.pdf", 1024000L, 1L);

        // 模拟Mapper行为
        when(productDocumentMapper.insert(any(ProductDocument.class))).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.addDocument(document);

        // 验证结果
        assertTrue(result);
        verify(productDocumentMapper, times(1)).insert(document);
    }

    @Test
    void deleteDocument_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(productDocumentMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.deleteDocument(1L);

        // 验证结果
        assertTrue(result);
        verify(productDocumentMapper, times(1)).deleteById(1L);
    }

    @Test
    void updateDocumentStatus_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(productDocumentMapper.updateStatus(1L, 0)).thenReturn(1);

        // 调用Service方法
        boolean result = portfolioProductService.updateDocumentStatus(1L, 0);

        // 验证结果
        assertTrue(result);
        verify(productDocumentMapper, times(1)).updateStatus(1L, 0);
    }

    // ==================== 边界情况测试 ====================

    @Test
    void getProductById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductById(null);

        // 验证结果
        assertNull(result);
        verify(portfolioProductMapper, times(1)).selectById(null);
    }

    @Test
    void getProductByCode_WithNullCode_ShouldReturnNull() {
        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductByCode(null);

        // 验证结果
        assertNull(result);
        verify(portfolioProductMapper, times(1)).selectByProductCode(null);
    }

    @Test
    void getProductByCode_WithEmptyCode_ShouldReturnNull() {
        // 调用Service方法
        PortfolioProduct result = portfolioProductService.getProductByCode("");

        // 验证结果
        assertNull(result);
        verify(portfolioProductMapper, times(1)).selectByProductCode("");
    }

    @Test
    void getProductsByStatus_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByStatus("CLOSED")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByStatus("CLOSED");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).selectByStatus("CLOSED");
    }

    @Test
    void getProductsByType_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByProductType("UNKNOWN")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByType("UNKNOWN");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).selectByProductType("UNKNOWN");
    }

    @Test
    void getProductsByRiskLevel_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByRiskLevel("未知")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByRiskLevel("未知");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).selectByRiskLevel("未知");
    }

    @Test
    void getProductsByCreator_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.selectByCreator(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.getProductsByCreator(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).selectByCreator(999L);
    }

    @Test
    void searchProducts_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(portfolioProductMapper.searchProducts("不存在的产品")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.searchProducts("不存在的产品");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(portfolioProductMapper, times(1)).searchProducts("不存在的产品");
    }

    @Test
    void searchProducts_WithNullKeyword_ShouldReturnAllProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.searchProducts(null)).thenReturn(Arrays.asList(product1, product2, product3));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.searchProducts(null);

        // 验证结果
        assertEquals(3, result.size());
        verify(portfolioProductMapper, times(1)).searchProducts(null);
    }

    @Test
    void searchProducts_WithEmptyKeyword_ShouldReturnAllProducts() {
        // 模拟Mapper行为
        when(portfolioProductMapper.searchProducts("")).thenReturn(Arrays.asList(product1, product2, product3));

        // 调用Service方法
        List<PortfolioProduct> result = portfolioProductService.searchProducts("");

        // 验证结果
        assertEquals(3, result.size());
        verify(portfolioProductMapper, times(1)).searchProducts("");
    }

    @Test
    void getProductPerformance_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(productPerformanceMapper.selectByProductId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<ProductPerformance> result = portfolioProductService.getProductPerformance(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(productPerformanceMapper, times(1)).selectByProductId(999L);
    }

    @Test
    void getLatestPerformance_ShouldReturnNull() {
        // 模拟Mapper行为
        when(productPerformanceMapper.selectLatestByProductId(999L)).thenReturn(null);

        // 调用Service方法
        ProductPerformance result = portfolioProductService.getLatestPerformance(999L);

        // 验证结果
        assertNull(result);
        verify(productPerformanceMapper, times(1)).selectLatestByProductId(999L);
    }

    @Test
    void getProductDocuments_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(productDocumentMapper.selectByProductId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<ProductDocument> result = portfolioProductService.getProductDocuments(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(productDocumentMapper, times(1)).selectByProductId(999L);
    }

    @Test
    void getProductDocumentsByType_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(productDocumentMapper.selectByProductIdAndType(1L, "UNKNOWN")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<ProductDocument> result = portfolioProductService.getProductDocumentsByType(1L, "UNKNOWN");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(productDocumentMapper, times(1)).selectByProductIdAndType(1L, "UNKNOWN");
    }

    @Test
    void createProduct_ShouldReturnFalseWhenInsertFails() {
        // 模拟Mapper行为
        when(portfolioProductMapper.insert(any(PortfolioProduct.class))).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.createProduct(product1);

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).insert(product1);
    }

    @Test
    void updateProduct_ShouldReturnFalseWhenUpdateFails() {
        // 模拟Mapper行为
        when(portfolioProductMapper.update(any(PortfolioProduct.class))).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.updateProduct(product1);

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).update(product1);
    }

    @Test
    void deleteProduct_ShouldReturnFalseWhenDeleteFails() {
        // 模拟Mapper行为
        when(portfolioProductMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.deleteProduct(999L);

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).deleteById(999L);
    }

    @Test
    void updateProductStatus_ShouldReturnFalseWhenUpdateFails() {
        // 模拟Mapper行为
        when(portfolioProductMapper.updateStatus(999L, "ACTIVE")).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.updateProductStatus(999L, "ACTIVE");

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).updateStatus(999L, "ACTIVE");
    }

    @Test
    void reviewProduct_ShouldReturnFalseWhenUpdateFails() {
        // 模拟Mapper行为
        when(portfolioProductMapper.updateReviewStatus(999L, "APPROVED", 2L, "审核通过")).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.reviewProduct(999L, "APPROVED", 2L, "审核通过");

        // 验证结果
        assertFalse(result);
        verify(portfolioProductMapper, times(1)).updateReviewStatus(999L, "APPROVED", 2L, "审核通过");
    }

    @Test
    void addPerformance_ShouldReturnFalseWhenInsertFails() {
        // 准备测试数据
        ProductPerformance performance = createProductPerformance(1L, 1L, LocalDate.now(), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));

        // 模拟Mapper行为
        when(productPerformanceMapper.insert(any(ProductPerformance.class))).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.addPerformance(performance);

        // 验证结果
        assertFalse(result);
        verify(productPerformanceMapper, times(1)).insert(performance);
    }

    @Test
    void batchAddPerformance_ShouldReturnFalseWhenInsertFails() {
        // 准备测试数据
        ProductPerformance performance = createProductPerformance(1L, 1L, LocalDate.now(), 
                new BigDecimal("1.0856"), new BigDecimal("1.0856"), new BigDecimal("0.0023"), new BigDecimal("0.0856"));
        List<ProductPerformance> performances = Arrays.asList(performance);

        // 模拟Mapper行为
        when(productPerformanceMapper.batchInsert(performances)).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.batchAddPerformance(performances);

        // 验证结果
        assertFalse(result);
        verify(productPerformanceMapper, times(1)).batchInsert(performances);
    }

    @Test
    void addDocument_ShouldReturnFalseWhenInsertFails() {
        // 准备测试数据
        ProductDocument document = createProductDocument(1L, 1L, "PROSPECTUS", "招募说明书.pdf", 
                "/documents/PP001_prospectus.pdf", 1024000L, 1L);

        // 模拟Mapper行为
        when(productDocumentMapper.insert(any(ProductDocument.class))).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.addDocument(document);

        // 验证结果
        assertFalse(result);
        verify(productDocumentMapper, times(1)).insert(document);
    }

    @Test
    void deleteDocument_ShouldReturnFalseWhenDeleteFails() {
        // 模拟Mapper行为
        when(productDocumentMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.deleteDocument(999L);

        // 验证结果
        assertFalse(result);
        verify(productDocumentMapper, times(1)).deleteById(999L);
    }

    @Test
    void updateDocumentStatus_ShouldReturnFalseWhenUpdateFails() {
        // 模拟Mapper行为
        when(productDocumentMapper.updateStatus(999L, 0)).thenReturn(0);

        // 调用Service方法
        boolean result = portfolioProductService.updateDocumentStatus(999L, 0);

        // 验证结果
        assertFalse(result);
        verify(productDocumentMapper, times(1)).updateStatus(999L, 0);
    }
}

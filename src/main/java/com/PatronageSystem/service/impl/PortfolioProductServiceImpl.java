package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.PortfolioProduct;
import com.PatronageSystem.entity.ProductPerformance;
import com.PatronageSystem.entity.ProductDocument;
import com.PatronageSystem.mapper.PortfolioProductMapper;
import com.PatronageSystem.mapper.ProductPerformanceMapper;
import com.PatronageSystem.mapper.ProductDocumentMapper;
import com.PatronageSystem.service.PortfolioProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 组合产品Service实现类
 */
@Service
@Transactional
public class PortfolioProductServiceImpl implements PortfolioProductService {
    
    @Autowired
    private PortfolioProductMapper portfolioProductMapper;
    
    @Autowired
    private ProductPerformanceMapper productPerformanceMapper;
    
    @Autowired
    private ProductDocumentMapper productDocumentMapper;
    
    @Override
    public List<PortfolioProduct> getAllProducts() {
        return portfolioProductMapper.selectAll();
    }
    
    @Override
    public PortfolioProduct getProductById(Long id) {
        return portfolioProductMapper.selectById(id);
    }
    
    @Override
    public PortfolioProduct getProductByCode(String productCode) {
        return portfolioProductMapper.selectByProductCode(productCode);
    }
    
    @Override
    public List<PortfolioProduct> getProductsByStatus(String productStatus) {
        return portfolioProductMapper.selectByStatus(productStatus);
    }
    
    @Override
    public List<PortfolioProduct> getProductsByType(String productType) {
        return portfolioProductMapper.selectByProductType(productType);
    }
    
    @Override
    public List<PortfolioProduct> getProductsByRiskLevel(String riskLevel) {
        return portfolioProductMapper.selectByRiskLevel(riskLevel);
    }
    
    @Override
    public List<PortfolioProduct> getProductsByCreator(Long creatorId) {
        return portfolioProductMapper.selectByCreator(creatorId);
    }
    
    @Override
    public List<PortfolioProduct> searchProducts(String keyword) {
        return portfolioProductMapper.searchProducts(keyword);
    }
    
    @Override
    public boolean createProduct(PortfolioProduct product) {
        return portfolioProductMapper.insert(product) > 0;
    }
    
    @Override
    public boolean updateProduct(PortfolioProduct product) {
        return portfolioProductMapper.update(product) > 0;
    }
    
    @Override
    public boolean deleteProduct(Long id) {
        return portfolioProductMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean updateProductStatus(Long id, String productStatus) {
        return portfolioProductMapper.updateStatus(id, productStatus) > 0;
    }
    
    @Override
    public boolean reviewProduct(Long id, String reviewStatus, Long reviewerId, String reviewComment) {
        return portfolioProductMapper.updateReviewStatus(id, reviewStatus, reviewerId, reviewComment) > 0;
    }
    
    @Override
    public List<ProductPerformance> getProductPerformance(Long productId) {
        return productPerformanceMapper.selectByProductId(productId);
    }
    
    @Override
    public List<ProductPerformance> getProductPerformanceByDateRange(Long productId, LocalDate startDate, LocalDate endDate) {
        return productPerformanceMapper.selectByProductIdAndDateRange(productId, startDate, endDate);
    }
    
    @Override
    public ProductPerformance getLatestPerformance(Long productId) {
        return productPerformanceMapper.selectLatestByProductId(productId);
    }
    
    @Override
    public boolean addPerformance(ProductPerformance performance) {
        return productPerformanceMapper.insert(performance) > 0;
    }
    
    @Override
    public boolean batchAddPerformance(List<ProductPerformance> performances) {
        return productPerformanceMapper.batchInsert(performances) > 0;
    }
    
    @Override
    public List<ProductDocument> getProductDocuments(Long productId) {
        return productDocumentMapper.selectByProductId(productId);
    }
    
    @Override
    public List<ProductDocument> getProductDocumentsByType(Long productId, String documentType) {
        return productDocumentMapper.selectByProductIdAndType(productId, documentType);
    }
    
    @Override
    public boolean addDocument(ProductDocument document) {
        return productDocumentMapper.insert(document) > 0;
    }
    
    @Override
    public boolean deleteDocument(Long documentId) {
        return productDocumentMapper.deleteById(documentId) > 0;
    }
    
    @Override
    public boolean updateDocumentStatus(Long documentId, Integer isActive) {
        return productDocumentMapper.updateStatus(documentId, isActive) > 0;
    }
} 
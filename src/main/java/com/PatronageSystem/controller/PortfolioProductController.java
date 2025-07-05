package com.PatronageSystem.controller;

import com.PatronageSystem.entity.PortfolioProduct;
import com.PatronageSystem.entity.ProductPerformance;
import com.PatronageSystem.entity.ProductDocument;
import com.PatronageSystem.service.PortfolioProductService;
import com.PatronageSystem.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 组合产品Controller
 */
@RestController
@RequestMapping("/api/portfolio-product")
@CrossOrigin(origins = "*")
public class PortfolioProductController {

    @Autowired
    private PortfolioProductService portfolioProductService;

    /**
     * 获取所有产品
     */
    @GetMapping("/list")
    public Result<List<PortfolioProduct>> getAllProducts() {
        try {
            List<PortfolioProduct> products = portfolioProductService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("获取产品列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/{id}")
    public Result<PortfolioProduct> getProductById(@PathVariable Long id) {
        try {
            PortfolioProduct product = portfolioProductService.getProductById(id);
            if (product != null) {
                return Result.success(product);
            } else {
                return Result.error("产品不存在");
            }
        } catch (Exception e) {
            return Result.error("获取产品详情失败：" + e.getMessage());
        }
    }

    /**
     * 根据产品代码获取产品
     */
    @GetMapping("/code/{productCode}")
    public Result<PortfolioProduct> getProductByCode(@PathVariable String productCode) {
        try {
            PortfolioProduct product = portfolioProductService.getProductByCode(productCode);
            if (product != null) {
                return Result.success(product);
            } else {
                return Result.error("产品不存在");
            }
        } catch (Exception e) {
            return Result.error("获取产品详情失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态获取产品
     */
    @GetMapping("/status/{productStatus}")
    public Result<List<PortfolioProduct>> getProductsByStatus(@PathVariable String productStatus) {
        try {
            List<PortfolioProduct> products = portfolioProductService.getProductsByStatus(productStatus);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("获取产品列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据产品类型获取产品
     */
    @GetMapping("/type/{productType}")
    public Result<List<PortfolioProduct>> getProductsByType(@PathVariable String productType) {
        try {
            List<PortfolioProduct> products = portfolioProductService.getProductsByType(productType);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("获取产品列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据风险等级获取产品
     */
    @GetMapping("/risk/{riskLevel}")
    public Result<List<PortfolioProduct>> getProductsByRiskLevel(@PathVariable String riskLevel) {
        try {
            List<PortfolioProduct> products = portfolioProductService.getProductsByRiskLevel(riskLevel);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("获取产品列表失败：" + e.getMessage());
        }
    }

    /**
     * 搜索产品
     */
    @GetMapping("/search")
    public Result<List<PortfolioProduct>> searchProducts(@RequestParam String keyword) {
        try {
            List<PortfolioProduct> products = portfolioProductService.searchProducts(keyword);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("搜索产品失败：" + e.getMessage());
        }
    }

    /**
     * 创建产品
     */
    @PostMapping("/create")
    public Result<Boolean> createProduct(@RequestBody PortfolioProduct product) {
        try {
            boolean success = portfolioProductService.createProduct(product);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("产品创建失败");
            }
        } catch (Exception e) {
            return Result.error("产品创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新产品
     */
    @PutMapping("/update")
    public Result<Boolean> updateProduct(@RequestBody PortfolioProduct product) {
        try {
            boolean success = portfolioProductService.updateProduct(product);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("产品更新失败");
            }
        } catch (Exception e) {
            return Result.error("产品更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        try {
            boolean success = portfolioProductService.deleteProduct(id);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("产品删除失败");
            }
        } catch (Exception e) {
            return Result.error("产品删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新产品状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateProductStatus(@PathVariable Long id, @RequestParam String productStatus) {
        try {
            boolean success = portfolioProductService.updateProductStatus(id, productStatus);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("产品状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("产品状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 审核产品
     */
    @PutMapping("/{id}/review")
    public Result<Boolean> reviewProduct(@PathVariable Long id,
                                       @RequestParam String reviewStatus,
                                       @RequestParam Long reviewerId,
                                       @RequestParam(required = false) String reviewComment) {
        try {
            boolean success = portfolioProductService.reviewProduct(id, reviewStatus, reviewerId, reviewComment);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("产品审核失败");
            }
        } catch (Exception e) {
            return Result.error("产品审核失败：" + e.getMessage());
        }
    }

    /**
     * 获取产品业绩数据
     */
    @GetMapping("/{id}/performance")
    public Result<List<ProductPerformance>> getProductPerformance(@PathVariable Long id) {
        try {
            List<ProductPerformance> performances = portfolioProductService.getProductPerformance(id);
            return Result.success(performances);
        } catch (Exception e) {
            return Result.error("获取产品业绩失败：" + e.getMessage());
        }
    }

    /**
     * 根据日期范围获取产品业绩数据
     */
    @GetMapping("/{id}/performance/range")
    public Result<List<ProductPerformance>> getProductPerformanceByDateRange(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ProductPerformance> performances = portfolioProductService.getProductPerformanceByDateRange(id, startDate, endDate);
            return Result.success(performances);
        } catch (Exception e) {
            return Result.error("获取产品业绩失败：" + e.getMessage());
        }
    }

    /**
     * 获取产品最新业绩
     */
    @GetMapping("/{id}/performance/latest")
    public Result<ProductPerformance> getLatestPerformance(@PathVariable Long id) {
        try {
            ProductPerformance performance = portfolioProductService.getLatestPerformance(id);
            if (performance != null) {
                return Result.success(performance);
            } else {
                return Result.error("暂无业绩数据");
            }
        } catch (Exception e) {
            return Result.error("获取最新业绩失败：" + e.getMessage());
        }
    }

    /**
     * 添加业绩数据
     */
    @PostMapping("/{id}/performance")
    public Result<Boolean> addPerformance(@PathVariable Long id, @RequestBody ProductPerformance performance) {
        try {
            performance.setProductId(id);
            boolean success = portfolioProductService.addPerformance(performance);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("业绩数据添加失败");
            }
        } catch (Exception e) {
            return Result.error("业绩数据添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加业绩数据
     */
    @PostMapping("/{id}/performance/batch")
    public Result<Boolean> batchAddPerformance(@PathVariable Long id, @RequestBody List<ProductPerformance> performances) {
        try {
            for (ProductPerformance performance : performances) {
                performance.setProductId(id);
            }
            boolean success = portfolioProductService.batchAddPerformance(performances);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("业绩数据批量添加失败");
            }
        } catch (Exception e) {
            return Result.error("业绩数据批量添加失败：" + e.getMessage());
        }
    }

    /**
     * 获取产品文档
     */
    @GetMapping("/{id}/documents")
    public Result<List<ProductDocument>> getProductDocuments(@PathVariable Long id) {
        try {
            List<ProductDocument> documents = portfolioProductService.getProductDocuments(id);
            return Result.success(documents);
        } catch (Exception e) {
            return Result.error("获取产品文档失败：" + e.getMessage());
        }
    }

    /**
     * 根据类型获取产品文档
     */
    @GetMapping("/{id}/documents/{documentType}")
    public Result<List<ProductDocument>> getProductDocumentsByType(@PathVariable Long id, @PathVariable String documentType) {
        try {
            List<ProductDocument> documents = portfolioProductService.getProductDocumentsByType(id, documentType);
            return Result.success(documents);
        } catch (Exception e) {
            return Result.error("获取产品文档失败：" + e.getMessage());
        }
    }

    /**
     * 添加产品文档
     */
    @PostMapping("/{id}/documents")
    public Result<Boolean> addDocument(@PathVariable Long id, @RequestBody ProductDocument document) {
        try {
            document.setProductId(id);
            boolean success = portfolioProductService.addDocument(document);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("文档添加失败");
            }
        } catch (Exception e) {
            return Result.error("文档添加失败：" + e.getMessage());
        }
    }

    /**
     * 删除产品文档
     */
    @DeleteMapping("/documents/{documentId}")
    public Result<Boolean> deleteDocument(@PathVariable Long documentId) {
        try {
            boolean success = portfolioProductService.deleteDocument(documentId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("文档删除失败");
            }
        } catch (Exception e) {
            return Result.error("文档删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新文档状态
     */
    @PutMapping("/documents/{documentId}/status")
    public Result<Boolean> updateDocumentStatus(@PathVariable Long documentId, @RequestParam Integer isActive) {
        try {
            boolean success = portfolioProductService.updateDocumentStatus(documentId, isActive);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("文档状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("文档状态更新失败：" + e.getMessage());
        }
    }
}

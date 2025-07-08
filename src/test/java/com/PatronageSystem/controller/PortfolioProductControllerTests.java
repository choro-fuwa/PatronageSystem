package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.controller.PortfolioProductController;
import com.PatronageSystem.entity.*;
import com.PatronageSystem.service.PortfolioProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PortfolioProductControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PortfolioProductService portfolioProductService;

    @InjectMocks
    private PortfolioProductController portfolioProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 配置支持Java 8日期类型的ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(portfolioProductController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    // 测试数据准备
    private PortfolioProduct createTestProduct() {
        PortfolioProduct product = new PortfolioProduct();
        product.setId(1L);
        product.setProductCode("P001");
        product.setProductName("Test Product");
        product.setProductType("Equity");
        return product;
    }

    private ProductPerformance createTestPerformance() {
        ProductPerformance performance = new ProductPerformance();
        performance.setId(1L);
        performance.setProductId(1L);
        performance.setNavDate(LocalDate.now());
        performance.setNav(BigDecimal.valueOf(1.25));
        performance.setAccumulatedNav(BigDecimal.valueOf(1.30));
        performance.setDailyReturn(BigDecimal.valueOf(0.01));
        return performance;
    }

    private ProductDocument createTestDocument() {
        ProductDocument document = new ProductDocument();
        document.setId(1L);
        document.setProductId(1L);
        document.setDocumentType("Prospectus");
        document.setDocumentName("Test Document");
        return document;
    }

    @Test
    void getAllProducts() throws Exception {
        PortfolioProduct product = createTestProduct();
        List<PortfolioProduct> products = Collections.singletonList(product);

        when(portfolioProductService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/portfolio-product/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andDo(print());
    }

    @Test
    void getProductById_found() throws Exception {
        PortfolioProduct product = createTestProduct();
        when(portfolioProductService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/portfolio-product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getProductById_notFound() throws Exception {
        when(portfolioProductService.getProductById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/portfolio-product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("产品不存在"));
    }

    @Test
    void createProduct_success() throws Exception {
        PortfolioProduct product = createTestProduct();
        when(portfolioProductService.createProduct(any())).thenReturn(true);

        mockMvc.perform(post("/api/portfolio-product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void updateProductStatus_success() throws Exception {
        when(portfolioProductService.updateProductStatus(1L, "ACTIVE")).thenReturn(true);

        mockMvc.perform(put("/api/portfolio-product/1/status?productStatus=ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void getProductPerformanceByDateRange() throws Exception {
        ProductPerformance performance = createTestPerformance();
        List<ProductPerformance> performances = Collections.singletonList(performance);
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        when(portfolioProductService.getProductPerformanceByDateRange(
                eq(1L), eq(startDate), eq(endDate))).thenReturn(performances);

        mockMvc.perform(get("/api/portfolio-product/1/performance/range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].navDate").exists());
    }

    @Test
    void addPerformance_success() throws Exception {
        ProductPerformance performance = createTestPerformance();
        when(portfolioProductService.addPerformance(any())).thenReturn(true);

        mockMvc.perform(post("/api/portfolio-product/1/performance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(performance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void reviewProduct_success() throws Exception {
        when(portfolioProductService.reviewProduct(eq(1L), eq("APPROVED"), eq(100L), any()))
                .thenReturn(true);

        mockMvc.perform(put("/api/portfolio-product/1/review")
                        .param("reviewStatus", "APPROVED")
                        .param("reviewerId", "100")
                        .param("reviewComment", "Approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void deleteDocument_success() throws Exception {
        when(portfolioProductService.deleteDocument(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/portfolio-product/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    // 新增测试：批量添加业绩数据
    @Test
    void batchAddPerformance_success() throws Exception {
        List<ProductPerformance> performances = List.of(
                createTestPerformance(),
                createTestPerformance()
        );
        when(portfolioProductService.batchAddPerformance(any())).thenReturn(true);

        mockMvc.perform(post("/api/portfolio-product/1/performance/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(performances)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    // 新增测试：获取产品文档
    @Test
    void getProductDocuments_success() throws Exception {
        ProductDocument document = createTestDocument();
        List<ProductDocument> documents = Collections.singletonList(document);
        when(portfolioProductService.getProductDocuments(1L)).thenReturn(documents);

        mockMvc.perform(get("/api/portfolio-product/1/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }
}
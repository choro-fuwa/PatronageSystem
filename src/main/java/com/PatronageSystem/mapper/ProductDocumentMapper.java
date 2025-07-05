package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.ProductDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品文档Mapper接口
 */
@Mapper
public interface ProductDocumentMapper {
    
    /**
     * 根据产品ID查询文档
     */
    List<ProductDocument> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据产品ID和文档类型查询文档
     */
    List<ProductDocument> selectByProductIdAndType(@Param("productId") Long productId, 
                                                  @Param("documentType") String documentType);
    
    /**
     * 根据ID查询文档
     */
    ProductDocument selectById(@Param("id") Long id);
    
    /**
     * 插入文档
     */
    int insert(ProductDocument document);
    
    /**
     * 更新文档
     */
    int update(ProductDocument document);
    
    /**
     * 删除文档
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新文档状态
     */
    int updateStatus(@Param("id") Long id, @Param("isActive") Integer isActive);
} 
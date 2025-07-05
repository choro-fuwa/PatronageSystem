package com.example.riskprofile.repository;

import com.example.riskprofile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据风险等级查找用户
     */
    List<User> findByRiskLevel(String riskLevel);
    
    /**
     * 根据投资周期查找用户
     */
    List<User> findByPreferredHorizon(String preferredHorizon);
    
    /**
     * 查找风险等级为指定值的用户数量
     */
    long countByRiskLevel(String riskLevel);
    
    /**
     * 自定义查询：根据风险等级和投资周期查找用户
     */
    @Query("SELECT u FROM User u WHERE u.riskLevel = :riskLevel AND u.preferredHorizon = :horizon")
    List<User> findByRiskLevelAndHorizon(@Param("riskLevel") String riskLevel, 
                                        @Param("horizon") String horizon);
    
    /**
     * 自定义查询：查找高风险用户
     */
    @Query("SELECT u FROM User u WHERE u.riskLevel = 'high'")
    List<User> findHighRiskUsers();
    
    /**
     * 自定义查询：查找低风险用户
     */
    @Query("SELECT u FROM User u WHERE u.riskLevel = 'low'")
    List<User> findLowRiskUsers();
    
    /**
     * 自定义查询：统计各风险等级的用户数量
     */
    @Query("SELECT u.riskLevel, COUNT(u) FROM User u GROUP BY u.riskLevel")
    List<Object[]> countUsersByRiskLevel();
}
package com.patronage.strategy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.Fscheduling.annotation.EnableScheduling;

/**
 * 策略管理子系统启动类
 */
@SpringBootApplication
@MapperScan("com.patronage.strategy.mapper")
@EnableScheduling
public class StrategyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrategyApplication.class, args);
    }
} 
package com.example.riskprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 风险画像服务主启动类
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.riskprofile.repository")
public class RiskProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskProfileApplication.class, args);
        System.out.println("风险画像服务启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API文档: http://localhost:8080/api/risk-profile");
    }
}
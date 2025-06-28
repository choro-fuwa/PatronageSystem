package com.patronage.strategy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("策略管理子系统API").version("1.0").description("策略管理相关接口文档"));
    }

    @Bean
    public GroupedOpenApi strategyApi() {
        return GroupedOpenApi.builder()
                .group("strategy")
                .pathsToMatch("/strategy/**", "/backtest/**", "/monitor/**")
                .build();
    }
} 
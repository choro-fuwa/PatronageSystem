package com.patronage.strategy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("策略管理子系统API文档")
                        .description("策略管理子系统的RESTful API接口文档，包含策略管理、回测引擎、监控预警等功能")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Patronage Team")
                                .url("http://www.patronage.com")
                                .email("support@patronage.com")))
                .tags(List.of(
                        new Tag().name("策略管理").description("策略的增删改查、启动停止等操作"),
                        new Tag().name("回测引擎").description("策略回测相关接口"),
                        new Tag().name("监控预警").description("策略监控和预警相关接口"),
                        new Tag().name("健康检查").description("系统健康检查接口")
                ));
    }
} 
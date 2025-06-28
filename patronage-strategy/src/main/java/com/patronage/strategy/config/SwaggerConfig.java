package com.patronage.strategy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.patronage.strategy.controller"))
                .paths(PathSelectors.any())
                .build()
                .tags(
                    new Tag("策略管理", "策略的增删改查、启动停止等操作"),
                    new Tag("回测引擎", "策略回测相关接口"),
                    new Tag("监控预警", "策略监控和预警相关接口"),
                    new Tag("健康检查", "系统健康检查接口")
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("策略管理子系统API文档")
                .description("策略管理子系统的RESTful API接口文档，包含策略管理、回测引擎、监控预警等功能")
                .contact(new Contact("Patronage Team", "http://www.patronage.com", "support@patronage.com"))
                .version("1.0.0")
                .build();
    }
} 
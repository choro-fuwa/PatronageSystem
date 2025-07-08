package com.PatronageSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatronageSystemApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        // 验证Spring上下文成功启动
        assertNotNull(context, "Spring上下文应成功加载");
    }

    @Test
    void mainClassIsCorrect() {
        // 验证主应用类配置正确
        Class<?> mainClass = PatronageSystemApplication.class;
        assertEquals("PatronageSystemApplication",
                mainClass.getSimpleName(),
                "主启动类名称不匹配");

        // 验证@SpringBootApplication注解存在
        assertTrue(mainClass.isAnnotationPresent(SpringBootApplication.class),
                "缺少@SpringBootApplication注解");
    }
}
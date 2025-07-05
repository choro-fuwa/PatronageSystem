package com.PatronageSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan("com.PatronageSystem.filter")
public class PatronageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatronageSystemApplication.class, args);
    }

}

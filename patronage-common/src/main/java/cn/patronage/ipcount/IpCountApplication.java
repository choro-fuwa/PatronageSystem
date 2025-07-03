package cn.patronage.ipcount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan("cn.patronage.ipcount.filter")
public class IpCountApplication {
    public static void main(String[] args) {
        SpringApplication.run(IpCountApplication.class, args);
    }

}

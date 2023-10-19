package com.example.reggiepro;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.example.reggiepro.mapper")
@EnableTransactionManagement
public class ReggieProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieProApplication.class, args);
        log.info("项目启动成功");
    }

}

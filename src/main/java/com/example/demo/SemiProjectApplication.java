package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.demo.mapper") // Mapper 패키지 경로
public class SemiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemiProjectApplication.class, args);
    }

}

package com.example.bookprogram1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bookprogram1.mapper")
public class Bookprogram1Application {

    public static void main(String[] args) {
        SpringApplication.run(Bookprogram1Application.class, args);
    }

}

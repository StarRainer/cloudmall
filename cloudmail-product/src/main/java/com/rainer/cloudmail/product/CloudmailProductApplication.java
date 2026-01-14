package com.rainer.cloudmail.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.product.dao")
public class CloudmailProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailProductApplication.class, args);
    }

}

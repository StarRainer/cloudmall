package com.rainer.cloudmail.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.order.dao")
public class CloudmailOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailOrderApplication.class, args);
    }

}

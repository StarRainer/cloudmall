package com.rainer.cloudmail.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.ware.dao")
public class CloudmailWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailWareApplication.class, args);
    }

}

package com.rainer.cloudmail.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.product.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmail.product.feign")
public class CloudmailProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailProductApplication.class, args);
    }

}

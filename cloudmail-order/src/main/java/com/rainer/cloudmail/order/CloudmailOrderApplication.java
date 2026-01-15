package com.rainer.cloudmail.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.order.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmail.order.feign")
public class CloudmailOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailOrderApplication.class, args);
    }

}

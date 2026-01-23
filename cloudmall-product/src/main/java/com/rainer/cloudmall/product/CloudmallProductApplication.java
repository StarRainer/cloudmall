package com.rainer.cloudmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmall.product.feign")
public class CloudmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmallProductApplication.class, args);
    }

}

package com.rainer.cloudmall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.rainer.cloudmall.cart.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class CloudmallCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmallCartApplication.class, args);
    }

}

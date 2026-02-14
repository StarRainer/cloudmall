package com.rainer.cloudmall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmall.ware.feign")
public class CloudmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmallWareApplication.class, args);
    }

}

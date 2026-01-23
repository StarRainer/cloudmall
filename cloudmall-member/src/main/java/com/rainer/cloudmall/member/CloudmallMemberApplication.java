package com.rainer.cloudmall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmall.member.feign")
public class CloudmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmallMemberApplication.class, args);
    }

}

package com.rainer.cloudmail.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.member.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmail.member.feign")
public class CloudmailMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailMemberApplication.class, args);
    }

}

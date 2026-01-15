package com.rainer.cloudmail.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.coupon.dao")
@EnableDiscoveryClient()
@EnableFeignClients("com.rainer.cloudmail.coupon.feign")
public class CloudmailCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailCouponApplication.class, args);
    }

}

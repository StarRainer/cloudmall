package com.rainer.cloudmail.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.coupon.dao")
public class CloudmailCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailCouponApplication.class, args);
    }

}

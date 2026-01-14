package com.rainer.cloudmail.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rainer.cloudmail.member.dao")
public class CloudmailMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudmailMemberApplication.class, args);
    }

}

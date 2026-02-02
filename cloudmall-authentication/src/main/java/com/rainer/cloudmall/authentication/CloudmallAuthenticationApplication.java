package com.rainer.cloudmall.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients("com.rainer.cloudmall.authentication.feign")
@SpringBootApplication
public class CloudmallAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudmallAuthenticationApplication.class, args);
	}

}

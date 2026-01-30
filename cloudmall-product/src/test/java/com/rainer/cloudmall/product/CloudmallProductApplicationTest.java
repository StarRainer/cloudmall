package com.rainer.cloudmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class CloudmallProductApplicationTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStringRedisTemplate() {
        stringRedisTemplate.opsForValue().set("hello", "world");
        String hello = stringRedisTemplate.opsForValue().get("hello");
        System.out.println(hello);
    }
}

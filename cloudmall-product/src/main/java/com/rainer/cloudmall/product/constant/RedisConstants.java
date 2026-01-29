package com.rainer.cloudmall.product.constant;

public final class RedisConstants {
    private RedisConstants() {

    }

    public static final String CATALOG_JSON__KEY_PREFIX = "cloudmall:product:catalog:json";


    public static final long CATALOG_JSON_KEY_EXPIRE = 5 * 60L;
}

package com.rainer.cloudmall.product.constant;

public final class RedisConstants {
    private RedisConstants() {

    }

    public static final String CATALOG_JSON_KEY_PREFIX = "cloudmall:product:catalogjson";

    public static final long CATALOG_JSON_KEY_EXPIRE = 5 * 60L;

    public static final String CATALOG_JSON_LOCK_PREFIX = "cloudmall:product:catalogjson:lock";
}

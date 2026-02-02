package com.rainer.cloudmall.authentication.constant;

public final class RedisConstants {
    private RedisConstants() {

    }

    public static final String KEY_PREFIX = "cloudmall:authentication";

    public static final String CODE_KEY_PREFIX = KEY_PREFIX + ":code";

    public static final String CODE_LIMIT_KEY_PREFIX = CODE_KEY_PREFIX + ":limit";

    public static final String CODE_LOCK_KEY_PREFIX = CODE_KEY_PREFIX + ":lock";
}

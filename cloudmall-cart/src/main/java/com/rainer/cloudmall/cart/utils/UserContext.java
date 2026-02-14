package com.rainer.cloudmall.cart.utils;

import com.rainer.cloudmall.cart.to.UserInfoTo;

public final class UserContext {
    private UserContext() {

    }

    private static final ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    public static void set(UserInfoTo userInfoTo) {
        threadLocal.set(userInfoTo);
    }

    public static UserInfoTo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}

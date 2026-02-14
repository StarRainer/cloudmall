package com.rainer.cloudmall.cart.intercept;

import com.rainer.cloudmall.cart.to.UserInfoTo;
import com.rainer.cloudmall.cart.utils.UserContext;
import com.rainer.cloudmall.cart.vo.UserVo;
import com.rainer.cloudmall.common.constant.CookieConstants;
import com.rainer.cloudmall.common.constant.SessionConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.UUID;

public class CartInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();

        UserVo userVo = (UserVo) session.getAttribute(SessionConstants.LOGIN_USER);
        if (userVo != null) {
            userInfoTo.setUserId(userVo.getId());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CookieConstants.USER_KEY.equals(cookie.getName())) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        if (!StringUtils.hasLength(userInfoTo.getUserKey())) {
            userInfoTo.setUserKey(UUID.randomUUID().toString());
        }
        UserContext.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = UserContext.get();
        if (userInfoTo != null && !userInfoTo.getTempUser()) {
            Cookie cookie = new Cookie(CookieConstants.USER_KEY, userInfoTo.getUserKey());
            cookie.setDomain("cloudmall.com");
            cookie.setMaxAge(CookieConstants.USER_KEY_TIMEOUT);
            response.addCookie(cookie);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
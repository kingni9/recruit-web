package com.recruit.interceptor;

import com.recruit.base.UserManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhuangjt on 2017/3/16.
 */
public class LoginValidateInterceptor implements HandlerInterceptor {
    private static final String LOGIN_URL_KEY = "login";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().contains(LOGIN_URL_KEY)) {
            return true;
        }

        return UserManager.getUserInfo(request.getSession()) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        return;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        return;
    }
}

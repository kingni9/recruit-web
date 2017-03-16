package com.recruit.base;

import com.recruit.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by zhuangjt on 2017-03-15.
 */
public class UserManager {

    private static final String USER_NAME_KEY = "userInfo";

    public static void setUserInfo(HttpSession session, User user) {
        if(session != null) {
            session.setAttribute(USER_NAME_KEY, user);
        }
    }

    public static User getUserInfo(HttpSession session) {
        if(session != null) {
            return (User) session.getAttribute(USER_NAME_KEY);
        }

        return null;
    }
}

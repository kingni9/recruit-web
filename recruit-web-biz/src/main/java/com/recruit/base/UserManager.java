package com.recruit.base;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * Created by zhuangjt on 2017-03-15.
 */
public class UserManager {

    private static final String USER_NAME_KEY = "userInfo";

    public static void setUserName(HttpSession session, String userName) {
        if(session != null) {
            session.setAttribute(USER_NAME_KEY, userName);
        }
    }

    public static String getUserName(HttpSession session) {
        if(session != null) {
            return (String) session.getAttribute(USER_NAME_KEY);
        }

        return StringUtils.EMPTY;
    }
}

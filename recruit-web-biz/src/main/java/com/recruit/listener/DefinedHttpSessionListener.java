package com.recruit.listener;

import com.recruit.base.SessionManager;
import com.recruit.entity.User;
import com.recruit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by zhuangjt on 2017/3/17.
 */
public class DefinedHttpSessionListener implements HttpSessionListener {
    @Autowired
    private UserService userService;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        return;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        User user = SessionManager.getUserInfo(session);

        if(user != null) {
            try {
                userService.decreaseLockPortQty(user);
            } catch (Exception e) {
                return;
            }
        }
    }
}

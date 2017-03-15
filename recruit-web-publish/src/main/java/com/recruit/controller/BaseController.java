package com.recruit.controller;

import com.recruit.base.ResultDTO;
import com.recruit.base.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by zhuangjt on 2017-03-15.
 */
@Slf4j
@Controller
@RequestMapping("recruit")
public class BaseController {


    @RequestMapping(value = "request0", method = RequestMethod.GET)
    public ResultDTO<Boolean> request0(HttpSession session, @RequestParam("name") String name) {
        if(session != null && StringUtils.isBlank(name)) {
            UserManager.setUserName(session, name);
        }

        return ResultDTO.succeed(Boolean.TRUE);
    }

    @RequestMapping(value = "request1", method = RequestMethod.GET)
    public ResultDTO<String> request1(HttpSession session) {
        if(session != null) {
            String userName = UserManager.getUserName(session);
            log.info("UserName : " + userName);

            return ResultDTO.succeed(userName);
        }

        return ResultDTO.failed("illegal session value");
    }
}

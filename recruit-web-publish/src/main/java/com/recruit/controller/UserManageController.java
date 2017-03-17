package com.recruit.controller;

import com.recruit.base.ResultDTO;
import com.recruit.base.SessionManager;
import com.recruit.entity.User;
import com.recruit.service.UserService;
import com.recruit.vo.request.UserLoginRequestVo;
import com.recruit.vo.request.UserRegisterRequestVo;
import com.recruit.vo.result.UserLoginResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Slf4j
@Controller
@RequestMapping("recruit")
public class UserManageController {
    @Autowired
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultDTO<UserLoginResultVo> login(HttpServletRequest request, @RequestBody UserLoginRequestVo requestVo) {
        if(requestVo == null || StringUtils.isBlank(requestVo.getUserAccount()) || StringUtils.isBlank(requestVo.getPassword())) {
            return ResultDTO.failed("用户名或密码为空");
        }

        User user = userService.queryByUserAccount(requestVo.getUserAccount());
        if(user == null) {
            return ResultDTO.failed("用户名或密码错误");
        }

        ResultDTO<Boolean> resultDTO = userService.validateUserPsw(user, requestVo.getPassword());
        if(!resultDTO.isSuccess()) {
            return ResultDTO.failed("登录失败");
        }

        if(!resultDTO.getModel()) {
            return ResultDTO.failed("用户名或密码错误");
        }

        SessionManager.setUserInfo(request.getSession(), user);

        return ResultDTO.succeed(UserLoginResultVo.builder()
            .id(user.getId())
            .userAccount(user.getUserAccount())
            .userName(user.getUserName())
            .roleId(user.getRoleId())
            .build());
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultDTO<Boolean> logout(HttpServletRequest request) {
        SessionManager.removeUserInfo(request.getSession());

        return ResultDTO.succeed(Boolean.TRUE);
    }

    @RequestMapping("register")
    @ResponseBody
    public ResultDTO<Boolean> register(@RequestBody UserRegisterRequestVo requestVo) {
        if(requestVo == null || StringUtils.isBlank(requestVo.getUserAccount()) || StringUtils.isBlank(requestVo.getPassword()) || requestVo.getRoleId() == null) {
            return ResultDTO.failed("注册信息不合法");
        }

        User user = User.builder()
                .userAccount(requestVo.getUserAccount())
                .userName(requestVo.getUserAccount())
                .password(requestVo.getPassword())
                .roleId(requestVo.getRoleId()).build();

        try {
            ResultDTO<Boolean> resultDTO = userService.insert(user);
            if(resultDTO.isSuccess() && resultDTO.getModel()) {
                return ResultDTO.succeed(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.info("failed to register", e);
        }

        return ResultDTO.failed("用户注册失败");
    }
}

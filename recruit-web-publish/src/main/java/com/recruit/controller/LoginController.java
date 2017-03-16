package com.recruit.controller;

import com.recruit.base.ResultDTO;
import com.recruit.base.UserManager;
import com.recruit.entity.User;
import com.recruit.service.UserService;
import com.recruit.vo.request.UserLoginRequestVo;
import com.recruit.vo.result.UserLoginResultVo;
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
@Controller
@RequestMapping("recruit")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultDTO<UserLoginResultVo> login(HttpServletRequest request, @RequestBody UserLoginRequestVo vo) {
        if(vo == null || StringUtils.isBlank(vo.getUserAccount()) || StringUtils.isBlank(vo.getPassword())) {
            return ResultDTO.failed("用户名或密码为空");
        }

        User user = userService.queryByUserAccount(vo.getUserAccount());
        if(user == null) {
            return ResultDTO.failed("用户名或密码错误");
        }

        ResultDTO<Boolean> resultDTO = userService.validateUserPsw(user, vo.getPassword());
        if(!resultDTO.isSuccess()) {
            return ResultDTO.failed("登录失败");
        }

        if(!resultDTO.getModel()) {
            return ResultDTO.failed("用户名或密码错误");
        }

        UserManager.setUserInfo(request.getSession(), user);

        return ResultDTO.succeed(UserLoginResultVo.builder()
            .id(user.getId())
            .userAccount(user.getUserAccount())
            .userName(user.getUserName())
            .roleId(user.getRoleId())
            .build());
    }
}

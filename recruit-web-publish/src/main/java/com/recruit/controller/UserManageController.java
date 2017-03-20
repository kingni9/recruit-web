package com.recruit.controller;

import com.recruit.base.ResultDTO;
import com.recruit.base.SessionManager;
import com.recruit.entity.User;
import com.recruit.service.UserService;
import com.recruit.vo.request.UserLoginRequestVo;
import com.recruit.vo.request.UserPageRequestVo;
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
@RequestMapping("recruit/user")
public class UserManageController {
    @Autowired
    private UserService userService;

    @RequestMapping("queryPage")
    @ResponseBody
    public ResultDTO<UserPageRequestVo> queryPage(@RequestBody UserPageRequestVo requestVo) {
        if(requestVo == null) {
            requestVo = requestVo.builder().build();
        }

        return userService.queryPage(requestVo);
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultDTO<Integer> update(@RequestBody User user) {
        if(!this.validateIfIllegal(user)) {
            return ResultDTO.failed("用户信息不合法!");
        }

        try {
            return userService.update(user);
        } catch (Exception e) {
            log.error("failed to update user record", e);
        }

        return ResultDTO.failed("用户信息修改失败!");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultDTO<Integer> delete(Integer id) {
        if(id == null || id <= 0) {
            return ResultDTO.failed("用户ID不合法!");
        }

        try {
            return userService.delete(id);
        } catch (Exception e) {
            log.error("failed to update user record", e);
        }

        return ResultDTO.failed("用户信息删除失败!");
    }


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

        ResultDTO<Boolean> validateUserPswResultDTO = userService.validateUserPsw(user, requestVo.getPassword());

        if(!validateUserPswResultDTO.isSuccess()) {
            return ResultDTO.failed("登录失败");
        }

        if(!validateUserPswResultDTO.getModel()) {
            return ResultDTO.failed("用户名或密码错误");
        }

        try {
            ResultDTO<Boolean> validatePortQtyResultDTO = userService.validateOrIncreaseLockPortQty(user);
            if(!validatePortQtyResultDTO.isSuccess()) {
                return ResultDTO.failed("登录失败");
            }

            if(!validatePortQtyResultDTO.getModel()) {
                return ResultDTO.failed("用户登陆端口数量已满");
            }
        } catch (Exception e) {
            log.info("failed to login", e);
            return ResultDTO.failed("登录失败");
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
        User user = SessionManager.getUserInfo(request.getSession());

        if(user != null) {
            try {
                userService.decreaseLockPortQty(user);
                SessionManager.removeUserInfo(request.getSession());

                return ResultDTO.succeed(Boolean.TRUE);
            } catch (Exception e) {
                log.error("failed to logout", e);
            }
        }

        return ResultDTO.failed("用户注销失败");
    }

    @RequestMapping("register")
    @ResponseBody
    public ResultDTO<Boolean> register(@RequestBody UserRegisterRequestVo requestVo) {
        if(requestVo == null || StringUtils.isBlank(requestVo.getUserAccount()) || StringUtils.isBlank(requestVo.getPassword()) || requestVo.getRoleId() == null) {
            return ResultDTO.failed("注册信息不合法");
        }

        User user = userService.queryByUserAccount(requestVo.getUserAccount());
        if(user == null) {
            return ResultDTO.failed("当前邮箱用户已存在!");
        }

        User newUser = User.builder()
                .userAccount(requestVo.getUserAccount())
                .userName(requestVo.getUserAccount())
                .password(requestVo.getPassword())
                .roleId(requestVo.getRoleId()).build();

        try {
            ResultDTO<Boolean> resultDTO = userService.insert(newUser);     // TODO：邮箱激活校验
            if(resultDTO.isSuccess() && resultDTO.getModel()) {
                return ResultDTO.succeed(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.info("failed to register", e);
        }

        return ResultDTO.failed("用户注册失败");
    }

    private Boolean validateIfIllegal(User user) {
        return user != null
                && StringUtils.isNoneBlank(user.getUserAccount())
                && user.getId() != null
                && user.getId() > 0;
    }
}

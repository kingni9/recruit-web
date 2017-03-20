package com.recruit.common;

import com.alibaba.fastjson.JSON;
import com.recruit.base.BaseTest;
import com.recruit.entity.User;
import com.recruit.mapper.UserMapper;
import com.recruit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Slf4j
public class CommonTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Test
    public void m0() {
        User user = User.builder()
                .userAccount("1632102@qq.com")
                .password("3201280")
                .salt("232")
                .userName("kingni")
                .roleId(1)
                .build();

        log.info(userMapper.insert(user) + StringUtils.EMPTY);
    }

    @Test
    public void m1() {
        User user = User.builder()
                .userAccount("109840320@qq.com")
                .password("2030451xas")
                .userName("Mary")
                .roleId(2)
                .build();

        userService.insert(user);

        log.info(JSON.toJSONString(user, true));
    }

    @Test
    public void m2() {
        User user = User.builder().id(2).build();

        userMapper.update(user);
    }
}

package com.recruit.service;

import com.recruit.base.ResultDTO;
import com.recruit.entity.User;

/**
 * Created by zhuangjt on 2017/3/16.
 */
public interface UserService {
    /**
     * 根据用户账号查询用户信息
     * @param userAccount
     * @return
     */
    User queryByUserAccount(String userAccount);

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    ResultDTO<Boolean> insert(User user);
}

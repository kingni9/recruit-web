package com.recruit.service;

import com.recruit.base.ResultDTO;
import com.recruit.entity.User;
import com.recruit.vo.request.UserPageRequestVo;

/**
 * Created by zhuangjt on 2017/3/16.
 */
public interface UserService {

    /**
     * 用户信息分页查询
     * @param requestVo
     * @return
     */
    ResultDTO<UserPageRequestVo> queryPage(UserPageRequestVo requestVo);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ResultDTO<Integer> update(User user);

    /**
     * 更新用户信息
     * @param id
     * @return
     */
    ResultDTO<Integer> delete(Integer id);

    /**
     * 根据用户账号查询用户信息
     * @param userAccount
     * @return
     */
    User queryByUserAccount(String userAccount);

    /**
     * 根据客户端密码及查询出的用户信息校验密码一致性
     * @param user
     * @param psw
     * @return
     */
    ResultDTO<Boolean> validateUserPsw(User user, String psw);

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    ResultDTO<Boolean> insert(User user);

    /**
     * 校验或自增用户登录端口数量
     * @param user
     * @return
     */
    ResultDTO<Boolean> validateOrIncreaseLockPortQty(User user);

    /**
     * 自减用户登录端口数量
     * @param user
     * @return
     */
    ResultDTO<Boolean> decreaseLockPortQty(User user);
}

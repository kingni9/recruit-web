package com.recruit.service.impl;

import com.recruit.base.ResultDTO;
import com.recruit.entity.User;
import com.recruit.mapper.UserMapper;
import com.recruit.service.UserService;
import com.recruit.utils.EncryptUtil;
import com.recruit.utils.RandomKeyGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    private static final String DEFAULT_SALT = "1234";

    @Override
    public User queryByUserAccount(String userAccount) {
        if(StringUtils.isBlank(userAccount)) {
            log.info("illegal arguments for queryByUserAccount::userAccount is empty!");
            return null;
        }

        return userMapper.queryByUserAccount(userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public ResultDTO<Boolean> insert(User user) {
        if(!this.validateIfIllegal(user)) {
            return ResultDTO.failed("illegal arguments!");
        }

        try {
            this.encryptUserInfo(user);
            userMapper.insert(user);
        } catch (Exception e) {
            log.info("failed to insert user", e);
            throw e;
        }

        return ResultDTO.succeed(Boolean.TRUE);
    }

    private boolean validateIfIllegal(User user) {
        return user != null
                && StringUtils.isNoneBlank(user.getUserAccount())
                && StringUtils.isNoneBlank(user.getPassword())
                && user.getRoleId() != null;
    }

    private void encryptUserInfo(User user) {
        try {
            String salt = RandomKeyGenerateUtil.getRandomStrKey(4);

            if(StringUtils.isEmpty(salt)) {
                salt = DEFAULT_SALT;
            }

            user.setSalt(salt);
            user.setPassword(EncryptUtil.getEncryptedStr(user.getPassword(), salt));
        } catch (Exception e) {
            log.error("failed to encryptUserInfo", e);
            user.setSalt(StringUtils.EMPTY);    // 盐为空认定密码加密异常，登录时进行异常兼容
            return;
        }
    }
}
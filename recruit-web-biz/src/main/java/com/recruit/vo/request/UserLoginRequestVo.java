package com.recruit.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestVo implements Serializable {
    private static final long serialVersionUID = 6891929291989722618L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;
}

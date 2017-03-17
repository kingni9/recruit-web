package com.recruit.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zhuangjt on 2017/3/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestVo implements Serializable {
    private static final long serialVersionUID = -389056681920475131L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色Id
     */
    private Integer roleId;
}

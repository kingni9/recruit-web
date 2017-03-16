package com.recruit.vo.result;

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
public class UserLoginResultVo implements Serializable {
    private static final long serialVersionUID = 8419117227948110570L;

    /**
     * 用户Id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 角色id
     */
    private Integer roleId;
}

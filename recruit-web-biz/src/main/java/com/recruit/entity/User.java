package com.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuangjt on 2017/3/16.
 * 用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -9219995870741230121L;

    /**
     * Id
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
     * 用户密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 锁定登陆端口数
     */
    private Integer lockPortQty;

    /**
     * 分配登录端口数
     */
    private Integer allotPortQty;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}

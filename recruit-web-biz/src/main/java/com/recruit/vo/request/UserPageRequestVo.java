package com.recruit.vo.request;

import com.recruit.base.BaseQuery;
import com.recruit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zhuangjt on 2017/3/20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPageRequestVo extends BaseQuery<User> implements Serializable {
    private static final long serialVersionUID = 4787065704721270433L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 角色id
     */
    private Integer roleId;
}

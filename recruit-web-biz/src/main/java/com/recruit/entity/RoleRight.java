package com.recruit.entity;

import com.recruit.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zhuangjt on 2017/3/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRight extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4842108202995714895L;

    /**
     * 角色Id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限编码
     */
    private String rightCode;

    /**
     * 权限URL
     */
    private String rightUrl;
}

package com.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuangjt on 2017/3/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRight implements Serializable {
    private static final long serialVersionUID = 4842108202995714895L;

    /**
     * Id
     */
    private Integer id;

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

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}

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
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7232479944937253586L;

    /***
     * 角色名称
     */
    private String roleName;
}

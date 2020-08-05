package com.yytx.maintenance.base.entity;

import com.yytx.maintenance.pojo.BaseEntity;

import java.io.Serializable;

public class UserRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4305729951607404568L;
    private Long id;
    private Long userId;
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", dr=" + dr +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}

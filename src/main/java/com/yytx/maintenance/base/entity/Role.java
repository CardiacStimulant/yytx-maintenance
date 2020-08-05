package com.yytx.maintenance.base.entity;

import com.yytx.maintenance.pojo.BaseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7344534995410472230L;
    private Long id;
    private String name;
    private String comments;
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                ", version=" + version +
                ", dr=" + dr +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}

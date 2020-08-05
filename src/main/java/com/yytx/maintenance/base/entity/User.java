package com.yytx.maintenance.base.entity;

import com.yytx.maintenance.pojo.BaseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7559254947665253702L;
    private Long id;
    private String loginAccount;
    private String password;
    private String jobNumber;
    private String name;
    private String mobile;
    private String email;
    private String comments;
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "User{" +
                "id=" + id +
                ", loginAccount='" + loginAccount + '\'' +
                ", password='" + password + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
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

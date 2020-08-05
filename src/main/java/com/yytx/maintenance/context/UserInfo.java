package com.yytx.maintenance.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 当前登录用户信息
 */
public class UserInfo {
    private static Logger logger = LoggerFactory.getLogger(UserInfo.class);

    private static UserInfo userInfo = new UserInfo();

    //用户ID
    private String userId = "1";
    //用户名称
    private String userName = "system";
    //用户角色列表，现在还没获取到
    private List<String> roleList;

    private UserInfo() {}

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static UserInfo getInstance() {
        return userInfo;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        UserInfo.userInfo = userInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}

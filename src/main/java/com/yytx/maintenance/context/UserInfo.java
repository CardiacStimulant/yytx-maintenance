package com.yytx.maintenance.context;

import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.base.entity.User;
import com.yytx.maintenance.excepion.UserInfoException;
import com.yytx.maintenance.utils.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前登录用户信息
 */
public class UserInfo {
    private static Logger logger = LoggerFactory.getLogger(UserInfo.class);

    private static Map<String, UserInfo> userInfoCache = new HashMap<>();

    //用户ID
    private User user;

    //用户角色集合
    private List<Role> roleList;

    // 角色资源集合
    private List<Resource> resourceList;

    private UserInfo() {
    }

    /**
     * 设置对象
     *
     * @return
     */
    public static void setUserInfoCache(String sessionId, User user, List<Role> roleList, List<Resource> resourceList) {
        if(StringUtils.isEmpty(sessionId)) {
            logger.error("设置用户对象失败，sessionId为空");
            throw new UserInfoException("设置用户对象失败，登录信息未获取到");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setRoleList(roleList);
        userInfo.setResourceList(resourceList);
        UserInfo.userInfoCache.put(sessionId, userInfo);
    }

    /**
     * 设置对象
     *
     * @return
     */
    public static void setUserInfoCache(String sessionId, UserInfo userInfo) {
        if(StringUtils.isEmpty(sessionId)) {
            logger.error("设置用户对象失败，sessionId为空");
            throw new UserInfoException("设置用户对象失败，登录信息未获取到");
        }
        UserInfo.userInfoCache.put(sessionId, userInfo);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static UserInfo getInstance() {
        HttpSession httpSession = HttpRequestUtil.getSession();
        if(httpSession!=null && StringUtils.isNotEmpty(httpSession.getId())){
            UserInfo userInfo = userInfoCache.get(httpSession.getId());
            if(userInfo != null){
                //通过本地内存快速返回userInfo，除了首次登录外其他时候调用都应该走到这里
                return userInfo;
            }else{
                return new UserInfo();
            }
        } else {
            logger.error("获取当前登录用户信息失败，获取session为空");
            throw new UserInfoException("未查询到当前登录用户信息");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }
}

package com.maintenance.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.maintenance.base.service.UserManagerService;
import com.maintenance.base.vo.UserRoleVo;
import com.maintenance.excepion.UserManagerException;
import com.maintenance.pojo.BaseControllerAnnotation;
import com.maintenance.pojo.SearchParams;
import com.maintenance.utils.SearchParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Slf4j
@BaseControllerAnnotation
@RequestMapping("userManager")
public class UserManagerController {
    @Autowired
    private UserManagerService userManagerService;

    /**
     * 查询用户管理分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 用户管理分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        SearchParams searchParams = new SearchParams();
        searchParams.setSearchMap(searchMap);
        // 创建时间的查询条件
        SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
        // 更新时间的查询条件
        SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
        return this.userManagerService.queryPage(searchParams);
    }

    /**
     * 保存用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/addUserManager", method = RequestMethod.POST)
    public Object addUserManager(@RequestBody UserRoleVo userRoleVo) {
        return this.userManagerService.addUserManager(userRoleVo);
    }

    /**
     * 修改用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/updateUserManager", method = RequestMethod.POST)
    public Object updateUserManager(@RequestBody UserRoleVo userRoleVo) {
        return this.userManagerService.updateUserManager(userRoleVo);
    }

    /**
     * 批量删除用户管理信息
     * @param userRoleVos    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/batchDeleteUserManager", method = RequestMethod.POST)
    public Object batchDeleteUserManager(@RequestBody List<UserRoleVo> userRoleVos) {
        this.userManagerService.batchDeleteUserManager(userRoleVos);
        return null;
    }

    /**
     * 删除用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/deleteUserManager", method = RequestMethod.POST)
    public Object deleteUserManager(@RequestBody UserRoleVo userRoleVo) {
        this.userManagerService.deleteUserManager(userRoleVo);
        return null;
    }

    /**
     * 修改密码
     * @param jsonData    密码信息
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(@RequestBody String jsonData) {
        if(StringUtils.isEmpty(jsonData)) {
            log.error("修改密码失败，参数为空");
            throw new UserManagerException("修改密码失败，参数为空");
        }
        JSONObject json = JSONObject.parseObject(jsonData);
        // ID
        Long userId = json.getLong("userId");
        // version
        Integer version = json.getInteger("version");
        // 新密码
        String newPassword = json.getString("newPassword");
        // 修改密码
        return this.userManagerService.updatePassword(userId, version, newPassword);
    }
}

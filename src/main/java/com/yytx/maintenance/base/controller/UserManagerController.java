package com.yytx.maintenance.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.service.UserManagerService;
import com.yytx.maintenance.base.vo.UserRoleVo;
import com.yytx.maintenance.excepion.UserManagerException;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import com.yytx.maintenance.utils.SearchParamsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("userManager")
public class UserManagerController {
    private Logger logger = LoggerFactory.getLogger(UserManagerController.class);

    @Autowired
    private UserManagerService userManagerService;

    /**
     * 查询用户管理分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 用户管理分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        Result<PageInfo<UserRoleVo>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            // 创建时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
            // 更新时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
            PageInfo<UserRoleVo> pageInfo = this.userManagerService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<UserRoleVo>>().setData(pageInfo);
        } catch (UserManagerException e) {
            result = new ResultUtil<PageInfo<UserRoleVo>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询用户管理分页数据异常", e);
            result = new ResultUtil<PageInfo<UserRoleVo>>().setErrorMsg("查询用户管理分页异常，请联系管理员");
        }
        return result;
    }

    /**
     * 保存用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/addUserManager", method = RequestMethod.POST)
    public Object addUserManager(@RequestBody UserRoleVo userRoleVo) {
        Result<UserRoleVo> result;
        try {
            userRoleVo = this.userManagerService.addUserManager(userRoleVo);
            result = new ResultUtil<UserRoleVo>().setData(userRoleVo);
        } catch (UserManagerException e) {
            result = new ResultUtil<UserRoleVo>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户管理信息异常", e);
            result = new ResultUtil<UserRoleVo>().setErrorMsg("保存用户信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 修改用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/updateUserManager", method = RequestMethod.POST)
    public Object updateUserManager(@RequestBody UserRoleVo userRoleVo) {
        Result<UserRoleVo> result;
        try {
            userRoleVo = this.userManagerService.updateUserManager(userRoleVo);
            result = new ResultUtil<UserRoleVo>().setData(userRoleVo);
        } catch (UserManagerException e) {
            result = new ResultUtil<UserRoleVo>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户管理信息异常", e);
            result = new ResultUtil<UserRoleVo>().setErrorMsg("保存用户信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 批量删除用户管理信息
     * @param userRoleVos    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/batchDeleteUserManager", method = RequestMethod.POST)
    public Object batchDeleteUserManager(@RequestBody List<UserRoleVo> userRoleVos) {
        Result<String> result;
        try {
            this.userManagerService.batchDeleteUserManager(userRoleVos);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (UserManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户管理信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("保存用户信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 删除用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
    @RequestMapping(value = "/deleteUserManager", method = RequestMethod.POST)
    public Object deleteUserManager(@RequestBody UserRoleVo userRoleVo) {
        Result<String> result;
        try {
            this.userManagerService.deleteUserManager(userRoleVo);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (UserManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户管理信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("保存用户信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 修改密码
     * @param jsonData    密码信息
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(@RequestBody String jsonData) {
        Result<String> result;
        try {
            if(StringUtils.isEmpty(jsonData)) {
                logger.error("修改密码失败，参数为空");
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
            this.userManagerService.updatePassword(userId, version, newPassword);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (UserManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("修改密码异常，参数：" + jsonData, e);
            result = new ResultUtil<String>().setErrorMsg("修改密码异常，请联系管理员");
        }
        return result;
    }
}

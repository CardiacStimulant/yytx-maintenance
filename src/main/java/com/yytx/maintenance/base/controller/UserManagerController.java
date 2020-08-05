package com.yytx.maintenance.base.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.service.UserManagerService;
import com.yytx.maintenance.base.vo.UserRoleVo;
import com.yytx.maintenance.excepion.UserManagerException;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import com.yytx.maintenance.utils.SearchParamsUtil;
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
}

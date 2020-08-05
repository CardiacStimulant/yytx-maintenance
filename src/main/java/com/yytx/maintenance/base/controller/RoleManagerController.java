package com.yytx.maintenance.base.controller;

import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.base.service.RoleManagerService;
import com.yytx.maintenance.excepion.RoleManagerException;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("roleManager")
public class RoleManagerController {
    private Logger logger = LoggerFactory.getLogger(RoleManagerController.class);

    @Autowired
    private RoleManagerService roleManagerService;

    /**
     * 查询用户管理分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 用户管理分页数据
     */
//    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
//    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
//        Result<PageInfo<UserRoleVo>> result;
//        try {
//            SearchParams searchParams = new SearchParams();
//            searchParams.setSearchMap(searchMap);
//            // 创建时间的查询条件
//            SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
//            // 更新时间的查询条件
//            SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
//            PageInfo<UserRoleVo> pageInfo = this.roleManagerService.queryPage(searchParams);
//            result = new ResultUtil<PageInfo<UserRoleVo>>().setData(pageInfo);
//        } catch (RoleManagerException e) {
//            result = new ResultUtil<PageInfo<UserRoleVo>>().setErrorMsg(e.getMessage());
//        } catch (Exception e) {
//            logger.error("查询用户管理分页数据异常", e);
//            result = new ResultUtil<PageInfo<UserRoleVo>>().setErrorMsg("查询用户管理分页异常，请联系管理员");
//        }
//        return result;
//    }

    /**
     * 查询所有角色信息
     * @param searchMap 查询条件
     * @return 角色分页数据
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryList(@RequestParam Map<String, Object> searchMap) {
        Result<List<Role>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            List<Role> roles = this.roleManagerService.queryList(searchParams);
            result = new ResultUtil<List<Role>>().setData(roles);
        } catch (RoleManagerException e) {
            result = new ResultUtil<List<Role>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询角色信息异常", e);
            result = new ResultUtil<List<Role>>().setErrorMsg("查询角色信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 保存用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     */
//    @RequestMapping(value = "/addUserManager", method = RequestMethod.POST)
//    public Object addUserManager(@RequestBody UserRoleVo userRoleVo) {
//        Result<UserRoleVo> result;
//        try {
//            userRoleVo = this.userManagerService.addUserManager(userRoleVo);
//            result = new ResultUtil<UserRoleVo>().setData(userRoleVo);
//        } catch (RoleManagerException e) {
//            result = new ResultUtil<UserRoleVo>().setErrorMsg(e.getMessage());
//        } catch (Exception e) {
//            logger.error("保存用户管理信息异常", e);
//            result = new ResultUtil<UserRoleVo>().setErrorMsg("保存用户信息异常，请联系管理员");
//        }
//        return result;
//    }
//
//    /**
//     * 修改用户管理信息
//     * @param userRoleVo    用户管理信息
//     * @return 用户管理信息
//     */
//    @RequestMapping(value = "/updateUserManager", method = RequestMethod.POST)
//    public Object updateUserManager(@RequestBody UserRoleVo userRoleVo) {
//        Result<UserRoleVo> result;
//        try {
//            userRoleVo = this.userManagerService.updateUserManager(userRoleVo);
//            result = new ResultUtil<UserRoleVo>().setData(userRoleVo);
//        } catch (RoleManagerException e) {
//            result = new ResultUtil<UserRoleVo>().setErrorMsg(e.getMessage());
//        } catch (Exception e) {
//            logger.error("保存用户管理信息异常", e);
//            result = new ResultUtil<UserRoleVo>().setErrorMsg("保存用户信息异常，请联系管理员");
//        }
//        return result;
//    }
//
//    /**
//     * 删除用户管理信息
//     * @param userRoleVo    用户管理信息
//     * @return 用户管理信息
//     */
//    @RequestMapping(value = "/deleteUserManager", method = RequestMethod.POST)
//    public Object deleteUserManager(@RequestBody UserRoleVo userRoleVo) {
//        Result<String> result;
//        try {
//            this.userManagerService.deleteUserManager(userRoleVo);
//            result = new ResultUtil<String>().setData("删除成功");
//        } catch (RoleManagerException e) {
//            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
//        } catch (Exception e) {
//            logger.error("保存用户管理信息异常", e);
//            result = new ResultUtil<String>().setErrorMsg("保存用户信息异常，请联系管理员");
//        }
//        return result;
//    }
}

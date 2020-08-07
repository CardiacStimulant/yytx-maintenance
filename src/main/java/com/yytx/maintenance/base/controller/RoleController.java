package com.yytx.maintenance.base.controller;

import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.base.service.RoleService;
import com.yytx.maintenance.excepion.RoleException;
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
@RequestMapping("roleManager")
public class RoleController {
    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色管理分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 角色管理分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        Result<PageInfo<Role>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            // 创建时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
            // 更新时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
            PageInfo<Role> pageInfo = this.roleService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<Role>>().setData(pageInfo);
        } catch (RoleException e) {
            result = new ResultUtil<PageInfo<Role>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询角色管理分页数据异常", e);
            result = new ResultUtil<PageInfo<Role>>().setErrorMsg("查询角色管理分页异常，请联系管理员");
        }
        return result;
    }

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
            List<Role> roles = this.roleService.queryList(searchParams);
            result = new ResultUtil<List<Role>>().setData(roles);
        } catch (RoleException e) {
            result = new ResultUtil<List<Role>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询角色信息异常", e);
            result = new ResultUtil<List<Role>>().setErrorMsg("查询角色信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 新增角色信息
     * @param role    角色信息
     * @return 角色信息
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public Object addRole(@RequestBody Role role) {
        Result<Role> result;
        try {
            role = this.roleService.addRole(role);
            result = new ResultUtil<Role>().setData(role);
        } catch (RoleException e) {
            result = new ResultUtil<Role>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存角色信息异常", e);
            result = new ResultUtil<Role>().setErrorMsg("保存角色信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 修改角色信息
     * @param role    角色信息
     * @return 角色信息
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public Object updateRole(@RequestBody Role role) {
        Result<Role> result;
        try {
            role = this.roleService.updateRole(role);
            result = new ResultUtil<Role>().setData(role);
        } catch (RoleException e) {
            result = new ResultUtil<Role>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存角色信息异常", e);
            result = new ResultUtil<Role>().setErrorMsg("保存角色信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 批量删除角色信息
     * @param roles    角色信息
     * @return 角色信息
     */
    @RequestMapping(value = "/batchDeleteRole", method = RequestMethod.POST)
    public Object batchDeleteRole(@RequestBody List<Role> roles) {
        Result<String> result;
        try {
            this.roleService.batchDeleteRole(roles);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (RoleException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除角色信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("删除角色信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 删除角色管理信息
     * @param role    角色信息
     * @return 角色管理信息
     */
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public Object deleteRole(@RequestBody Role role) {
        Result<String> result;
        try {
            this.roleService.deleteRole(role);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (RoleException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除角色管理信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("删除角色信息异常，请联系管理员");
        }
        return result;
    }
}

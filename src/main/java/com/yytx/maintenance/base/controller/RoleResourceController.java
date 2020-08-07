package com.yytx.maintenance.base.controller;

import com.yytx.maintenance.base.entity.RoleResource;
import com.yytx.maintenance.base.service.RoleResourceService;
import com.yytx.maintenance.excepion.ResourceManagerException;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("roleResource")
public class RoleResourceController {
    private Logger logger = LoggerFactory.getLogger(RoleResourceController.class);

    @Autowired
    private RoleResourceService roleResourceService;

    /**
     * 角色添加全部资源（添加角色-资源关系）
     * @param roleId    角色ID
     * @return
     */
    @RequestMapping(value = "/addAllRoleResource", method = RequestMethod.GET)
    public Object addAllRoleResource(@RequestParam("roleId") Long roleId) {
        Result<String> result;
        try {
            this.roleResourceService.addAllRoleResource(roleId);
            result = new ResultUtil<String>().setData("添加资源成功");
        } catch (ResourceManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("添加资源异常", e);
            result = new ResultUtil<String>().setErrorMsg("添加资源异常，请联系管理员");
        }
        return result;
    }

    /**
     * 角色添加资源
     * @param roleResources    角色ID
     * @return
     */
    @RequestMapping(value = "/addRoleResources", method = RequestMethod.POST)
    public Object addRoleResources(@RequestBody List<RoleResource> roleResources) {
        Result<String> result;
        try {
            this.roleResourceService.addRoleResources(roleResources);
            result = new ResultUtil<String>().setData("添加资源成功");
        } catch (ResourceManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("添加资源异常", e);
            result = new ResultUtil<String>().setErrorMsg("添加资源异常，请联系管理员");
        }
        return result;
    }

    /**
     * 角色移除全部资源（添加角色-资源关系）
     * @param roleId    角色ID
     * @return
     */
    @RequestMapping(value = "/removeAllRoleResource", method = RequestMethod.GET)
    public Object removeAllRoleResource(@RequestParam("roleId") Long roleId) {
        Result<String> result;
        try {
            this.roleResourceService.removeAllRoleResource(roleId);
            result = new ResultUtil<String>().setData("移除资源成功");
        } catch (ResourceManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("移除资源异常", e);
            result = new ResultUtil<String>().setErrorMsg("移除资源异常，请联系管理员");
        }
        return result;
    }

    /**
     * 角色移除资源
     * @param roleResources    角色ID
     * @return
     */
    @RequestMapping(value = "/removeRoleResources", method = RequestMethod.POST)
    public Object removeRoleResources(@RequestBody List<RoleResource> roleResources) {
        Result<String> result;
        try {
            this.roleResourceService.removeRoleResources(roleResources);
            result = new ResultUtil<String>().setData("移除资源成功");
        } catch (ResourceManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("移除资源异常", e);
            result = new ResultUtil<String>().setErrorMsg("移除资源异常，请联系管理员");
        }
        return result;
    }
}

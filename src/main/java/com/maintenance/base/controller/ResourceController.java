package com.maintenance.base.controller;

import com.github.pagehelper.PageInfo;
import com.maintenance.base.entity.Resource;
import com.maintenance.base.service.ResourceService;
import com.maintenance.pojo.Result;
import com.maintenance.utils.ResultUtil;
import com.maintenance.utils.SearchParamsUtil;
import com.maintenance.excepion.ResourceException;
import com.maintenance.pojo.SearchParams;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("resource")
public class ResourceController {
    private Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    /**
     * 查询资源分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 资源分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        Result<PageInfo<Resource>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            // 创建时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
            // 更新时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
            PageInfo<Resource> pageInfo = this.resourceService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<Resource>>().setData(pageInfo);
        } catch (ResourceException e) {
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询资源分页数据异常", e);
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg("查询资源分页异常，请联系管理员");
        }
        return result;
    }

    /**
     * 新增资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/addResource", method = RequestMethod.POST)
    public Object addResource(@RequestBody Resource resource) {
        Result<Resource> result;
        try {
            resource = this.resourceService.addResource(resource);
            result = new ResultUtil<Resource>().setData(resource);
        } catch (ResourceException e) {
            result = new ResultUtil<Resource>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存资源信息异常", e);
            result = new ResultUtil<Resource>().setErrorMsg("保存资源信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 修改资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/updateResource", method = RequestMethod.POST)
    public Object updateResource(@RequestBody Resource resource) {
        Result<Resource> result;
        try {
            resource = this.resourceService.updateResource(resource);
            result = new ResultUtil<Resource>().setData(resource);
        } catch (ResourceException e) {
            result = new ResultUtil<Resource>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存资源信息异常", e);
            result = new ResultUtil<Resource>().setErrorMsg("保存资源信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 批量删除资源信息
     * @param resources    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/batchDeleteResource", method = RequestMethod.POST)
    public Object batchDeleteResource(@RequestBody List<Resource> resources) {
        Result<String> result;
        try {
            this.resourceService.batchDeleteResource(resources);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (ResourceException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除资源信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("删除资源信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 删除资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/deleteResource", method = RequestMethod.POST)
    public Object deleteResource(@RequestBody Resource resource) {
        Result<String> result;
        try {
            this.resourceService.deleteResource(resource);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (ResourceException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除资源信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("删除资源信息异常，请联系管理员");
        }
        return result;
    }

    /**
     * 查询待配置分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小，roleId必传，searchConfig必传
     * @return 资源分页数据
     */
    @RequestMapping(value = "/queryConfigPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryConfigPage(@RequestParam Map<String, Object> searchMap) {
        Result<PageInfo<Resource>> result;
        try {
            /* 校验roleId是否传递，如果没有传递，则直接返回 */
            Long roleId = MapUtils.getLong(searchMap, "roleId");
            if(roleId==null || roleId<=0) {
                throw new ResourceException("查询待配置资源数据失败，角色ID为空");
            }
            /* 校验searchConfig是否传递，如果没有传递，则直接返回 */
            String searchConfig = MapUtils.getString(searchMap, "searchConfig");
            if(StringUtils.isEmpty(searchConfig)) {
                throw new ResourceException("查询待配置资源数据失败，参数错误");
            }
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            PageInfo<Resource> pageInfo = this.resourceService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<Resource>>().setData(pageInfo);
        } catch (ResourceException e) {
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询资源分页数据异常", e);
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg("查询资源分页异常，请联系管理员");
        }
        return result;
    }
}

package com.maintenance.base.controller;

import com.maintenance.base.entity.Resource;
import com.maintenance.base.service.ResourceService;
import com.maintenance.excepion.ResourceException;
import com.maintenance.pojo.BaseControllerAnnotation;
import com.maintenance.pojo.SearchParams;
import com.maintenance.utils.SearchParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Slf4j
@BaseControllerAnnotation()
@RequestMapping("resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * 查询资源分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 资源分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        SearchParams searchParams = new SearchParams();
        searchParams.setSearchMap(searchMap);
        // 创建时间的查询条件
        SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
        // 更新时间的查询条件
        SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
        return this.resourceService.queryPage(searchParams);
    }

    /**
     * 新增资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/addResource", method = RequestMethod.POST)
    public Object addResource(@RequestBody Resource resource) {
        return this.resourceService.addResource(resource);
    }

    /**
     * 修改资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/updateResource", method = RequestMethod.POST)
    public Object updateResource(@RequestBody Resource resource) {
        return this.resourceService.updateResource(resource);
    }

    /**
     * 批量删除资源信息
     * @param resources    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/batchDeleteResource", method = RequestMethod.POST)
    public Object batchDeleteResource(@RequestBody List<Resource> resources) {
        this.resourceService.batchDeleteResource(resources);
        return null;
    }

    /**
     * 删除资源信息
     * @param resource    资源信息
     * @return 资源信息
     */
    @RequestMapping(value = "/deleteResource", method = RequestMethod.POST)
    public Object deleteResource(@RequestBody Resource resource) {
        this.resourceService.deleteResource(resource);
        return null;
    }

    /**
     * 查询待配置分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小，roleId必传，searchConfig必传
     * @return 资源分页数据
     */
    @RequestMapping(value = "/queryConfigPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryConfigPage(@RequestParam Map<String, Object> searchMap) {
        /* 校验roleId是否传递，如果没有传递，则直接返回 */
        Long roleId = MapUtils.getLong(searchMap, "roleId");
        if(roleId==null || roleId<=0) {
            log.error("查询待配置资源数据失败，角色ID为空");
            throw new ResourceException("查询待配置资源数据失败，角色ID为空");
        }
        /* 校验searchConfig是否传递，如果没有传递，则直接返回 */
        String searchConfig = MapUtils.getString(searchMap, "searchConfig");
        if(StringUtils.isEmpty(searchConfig)) {
            log.error("查询待配置资源数据失败，参数错误");
            throw new ResourceException("查询待配置资源数据失败，参数错误");
        }
        SearchParams searchParams = new SearchParams();
        searchParams.setSearchMap(searchMap);
        return this.resourceService.queryPage(searchParams);
    }
}

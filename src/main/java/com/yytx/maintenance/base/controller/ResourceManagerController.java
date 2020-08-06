package com.yytx.maintenance.base.controller;

import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.base.service.ResourceManagerService;
import com.yytx.maintenance.excepion.ResourceManagerException;
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
@RequestMapping("resourceManager")
public class ResourceManagerController {
    private Logger logger = LoggerFactory.getLogger(ResourceManagerController.class);

    @Autowired
    private ResourceManagerService resourceManagerService;

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
            PageInfo<Resource> pageInfo = this.resourceManagerService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<Resource>>().setData(pageInfo);
        } catch (ResourceManagerException e) {
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询资源分页数据异常", e);
            result = new ResultUtil<PageInfo<Resource>>().setErrorMsg("查询资源分页异常，请联系管理员");
        }
        return result;
    }

    /**
     * 查询所有资源信息
     * @param searchMap 查询条件
     * @return 资源分页数据
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryList(@RequestParam Map<String, Object> searchMap) {
        Result<List<Resource>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            List<Resource> resources = this.resourceManagerService.queryList(searchParams);
            result = new ResultUtil<List<Resource>>().setData(resources);
        } catch (ResourceManagerException e) {
            result = new ResultUtil<List<Resource>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询资源信息异常", e);
            result = new ResultUtil<List<Resource>>().setErrorMsg("查询资源信息异常，请联系管理员");
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
            resource = this.resourceManagerService.addResource(resource);
            result = new ResultUtil<Resource>().setData(resource);
        } catch (ResourceManagerException e) {
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
            resource = this.resourceManagerService.updateResource(resource);
            result = new ResultUtil<Resource>().setData(resource);
        } catch (ResourceManagerException e) {
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
            this.resourceManagerService.batchDeleteResource(resources);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (ResourceManagerException e) {
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
            this.resourceManagerService.deleteResource(resource);
            result = new ResultUtil<String>().setData("删除成功");
        } catch (ResourceManagerException e) {
            result = new ResultUtil<String>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("删除资源信息异常", e);
            result = new ResultUtil<String>().setErrorMsg("删除资源信息异常，请联系管理员");
        }
        return result;
    }
}

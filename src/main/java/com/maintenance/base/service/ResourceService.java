package com.maintenance.base.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maintenance.base.dao.ResourceDao;
import com.maintenance.base.entity.OperationLog;
import com.maintenance.base.entity.Resource;
import com.maintenance.constant.OperationLogBusinessTypeEnum;
import com.maintenance.constant.OperationLogOperationTypeEnum;
import com.maintenance.context.UserInfo;
import com.maintenance.excepion.ResourceException;
import com.maintenance.pojo.SearchParams;
import com.maintenance.utils.InitializeObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ResourceService {
    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询资源分页数据
     *
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 资源分页数据
     * @throws ResourceException 自定义异常
     */
    public PageInfo<Resource> queryPage(SearchParams searchParams) throws ResourceException{
        if(searchParams==null || searchParams.getSearchMap()==null) {
            log.error("查询资源失败，参数为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            log.error("查询资源失败，参数错误，pageNum为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            log.error("查询资源失败，参数错误，pageSize为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<Resource> page = resourceDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            log.error("查询资源分页异常，参数：" + searchParams.getSearchMap(), e);
            throw new ResourceException("查询资源分页异常，请联系管理员");
        }
    }

    /**
     * 查询资源数据
     *
     * @param searchParams 查询条件
     * @return 资源分页数据
     * @throws ResourceException 自定义异常
     */
    public List<Resource> queryList(SearchParams searchParams) throws ResourceException{
        try {
            return resourceDao.queryList(searchParams);
        } catch (Exception e) {
            log.error("查询资源数据异常，参数：" + searchParams.getSearchMap(), e);
            throw new ResourceException("查询资源数据异常，请联系管理员");
        }
    }

    /**
     * 根据ID查询资源信息
     * @param resourceId
     * @return
     * @throws ResourceException
     */
    public Resource getResourceById(Long resourceId) throws ResourceException {
        if(resourceId==null || resourceId<=0) {
            log.error("查询资源信息失败，ID为空");
            throw new ResourceException("查询资源信息失败，资源ID为空");
        }
        try {
            return this.resourceDao.getResourceById(resourceId);
        } catch (Exception e) {
            throw new ResourceException("查询资源信息异常，请联系管理员");
        }
    }

    /**
     * 新增资源信息
     * @param resource
     * @return
     */
    public Resource addResource(Resource resource) throws ResourceException {
        try {
            // 校验资源编码是否重复
            boolean isExists = this.checkResourceKeyExists(resource);
            if(isExists) {
                throw new ResourceException("新增资源信息失败，资源编码重复");
            } else {
                // 设置创建信息和修改信息
                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(resource, UserInfo.getInstance());
                int add = this.resourceDao.addResource(resource);
                if(add==1) {
                    resource = this.getResourceById(resource.getId());
                } else {
                    log.error("新增资源信息失败，返回数量为0");
                    throw new ResourceException("保存资源信息失败，请联系管理员");
                }
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            log.error("新增资源信息异常，参数：" + resource, e);
            throw new ResourceException("保存资源信息异常，请联系管理员");
        }
        return resource;
    }

    /**
     * 修改资源信息
     * @param resource
     * @return
     */
    public Resource updateResource(Resource resource) throws ResourceException {
        if(resource==null) {
            log.error("修改资源信息失败，参数为空");
            throw new ResourceException("保存资源信息失败，参数为空");
        }
        if(resource.getId()==null || resource.getId()<=0) {
            log.error("修改资源信息失败，资源ID为空");
            throw new ResourceException("保存资源信息失败，资源ID为空");
        }
        if(StringUtils.isEmpty(resource.getName())) {
            log.error("修改资源信息失败，资源名称为空");
            throw new ResourceException("保存资源信息失败，资源名称为空");
        }
        if(StringUtils.isEmpty(resource.getOwner())) {
            log.error("修改资源信息失败，资源归属为空");
            throw new ResourceException("保存资源信息失败，资源归属为空");
        }
        if(StringUtils.isEmpty(resource.getType())) {
            log.error("修改资源信息失败，资源类型为空");
            throw new ResourceException("保存资源信息失败，资源类型为空");
        }
        try {
            // 查询资源信息，校验数据
            Resource oldResource = this.getResourceById(resource.getId());
            // 校验版本号
            if(!oldResource.getVersion().equals(resource.getVersion())) {
                log.error("修改资源信息失败，版本号不一致");
                throw new ResourceException("保存资源信息失败，资源已被修改，请重新刷新页面重试");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(resource, UserInfo.getInstance());
            int add = this.resourceDao.updateResource(resource);
            if(add==1) {
                resource = this.getResourceById(resource.getId());
            } else {
                log.error("修改资源信息失败，返回数量为0");
                throw new ResourceException("保存资源信息失败，请联系管理员");
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改资源信息异常，参数：" + resource, e);
            throw new ResourceException("保存资源信息异常，请联系管理员");
        }
        return resource;
    }

    /**
     * 校验资源编码是否存在
     * @param resource  资源
     * @return
     */
    private boolean checkResourceKeyExists(Resource resource) {
        return this.resourceDao.checkResourceKeyExists(resource)>0;
    }

    /**
     * 批量刪除资源
     * @param resources  资源信息
     * @return
     */
    public void batchDeleteResource(List<Resource> resources) throws ResourceException {
        if(resources==null || resources.size()<=0) {
            log.error("删除资源失败，参数为空");
            throw new ResourceException("删除资源失败，参数为空");
        }
        try {
            for(Resource resource : resources) {
                this.deleteResource(resource);
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除资源异常，参数：" + resources, e);
            throw new ResourceException("删除资源异常，请联系管理员");
        }
    }

    /**
     * 刪除资源信息
     * @param resource  资源信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteResource(Resource resource) throws ResourceException {
        if(resource==null) {
            log.error("删除资源失败，参数为空");
            throw new ResourceException("删除资源失败，参数为空");
        }
        if(resource.getId()==null || resource.getId()<=0) {
            log.error("删除资源失败，资源ID为空");
            throw new ResourceException("删除资源失败，资源ID为空");
        }
        if(resource.getVersion()==null || resource.getVersion()<0) {
            log.error("删除资源失败，版本号为空");
            throw new ResourceException("删除资源失败，参数错误");
        }
        try {
            // 删除资源信息
            int delete = this.resourceDao.deleteResource(resource);
            if(delete==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(resource.getId(),
                        OperationLogBusinessTypeEnum.RESOURCE.getKey(),
                        OperationLogOperationTypeEnum.DELETE.getKey(),
                        UserInfo.getInstance().getUser().getName() + "删除了资源：" + resource.getName() + "（" + resource.getKey() + "）",
                        UserInfo.getInstance()));
                // 删除资源关系
                this.roleResourceService.removeRoleResourceByResourceId(resource.getId());
            } else {
                log.error("删除资源失败，删除数量返回为0，参数：" + resource);
                throw new ResourceException("删除资源失败，资源已被修改，请重新刷新页面重试");
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除资源异常，参数：" + resource, e);
            throw new ResourceException("删除资源异常，请联系管理员");
        }
    }
}

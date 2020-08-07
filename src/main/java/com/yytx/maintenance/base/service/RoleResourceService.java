package com.yytx.maintenance.base.service;

import com.yytx.maintenance.base.dao.RoleResourceDao;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.base.entity.RoleResource;
import com.yytx.maintenance.context.UserInfo;
import com.yytx.maintenance.excepion.RoleException;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.InitializeObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleResourceService {
    private Logger logger = LoggerFactory.getLogger(RoleResourceService.class);

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Autowired
    private ResourceService resourceService;

    /**
     * 角色添加全部资源
     * @param roleId
     * @throws RoleException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAllRoleResource(Long roleId) throws RoleException {
        if(roleId==null || roleId<=0) {
            logger.error("角色添加资源失败，角色ID为空");
            throw new RoleException("角色添加资源失败，角色ID为空");
        }
        try {
            // 查询角色全部未配置的资源
            SearchParams searchParams = new SearchParams();
            searchParams.addCondition("roleId", roleId);
            searchParams.addCondition("searchConfig", "wait");
            List<Resource> resources = this.resourceService.queryList(searchParams);
            /* 配置角色资源数据 */
            RoleResource roleResource = new RoleResource();
            for(Resource resource : resources) {
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resource.getId());
                this.addRoleResource(roleResource);
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色添加资源异常，参数：" + roleId, e);
            throw new RoleException("角色添加资源异常，请联系管理员");
        }
    }

    /**
     * 角色添加资源
     * @param roleResources
     * @throws RoleException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRoleResources(List<RoleResource> roleResources) throws RoleException {
        if(roleResources==null || roleResources.size()<=0) {
            return;
        }
        try {
            for(RoleResource roleResource : roleResources) {
                this.addRoleResource(roleResource);
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色添加资源异常，参数：" + roleResources, e);
            throw new RoleException("角色添加资源异常，请联系管理员");
        }
    }

    /**
     * 添加角色-资源关系数据
     * @param roleResource
     * @return
     * @throws RoleException
     */
    public RoleResource addRoleResource(RoleResource roleResource) throws RoleException {
        if(roleResource==null) {
            logger.error("角色添加资源失败，参数为空");
            throw new RoleException("角色添加资源失败，参数为空");
        }
        if(roleResource.getRoleId()==null || roleResource.getRoleId()<=0) {
            logger.error("角色添加资源失败，角色ID为空");
            throw new RoleException("角色添加资源失败，角色ID为空");
        }
        if(roleResource.getResourceId()==null || roleResource.getResourceId()<=0) {
            logger.error("角色添加资源失败，资源ID为空");
            throw new RoleException("角色添加资源失败，资源ID为空");
        }
        try {
            // 校验角色-资源关系是否已经存在
            boolean isExists = this.checkRoleResourceExists(roleResource);
            if(isExists) {
                logger.error("角色添加资源失败，关系已存在，参数：" + roleResource);
                throw new RoleException("角色添加资源失败，角色-资源关系发生变化，请重新刷新后重试");
            } else {
                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(roleResource, UserInfo.getInstance());
                int add = this.roleResourceDao.addRoleResource(roleResource);
                if(add==1) {
                    return roleResource;
                } else {
                    logger.error("角色添加资源失败，新增返回数量为0，参数：" + roleResource);
                    throw new RoleException("角色添加资源失败，请联系管理员");
                }
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色添加资源异常，参数：" + roleResource, e);
            throw new RoleException("角色添加资源异常，请联系管理员");
        }
    }

    /**
     * 校验角色-资源关系是否存在
     * @param roleResource
     * @return
     */
    private boolean checkRoleResourceExists(RoleResource roleResource) {
        return this.roleResourceDao.checkRoleResourceExists(roleResource)>0;
    }

    /**
     * 角色移除全部资源
     * @param roleId
     * @throws RoleException
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAllRoleResource(Long roleId) throws RoleException {
        if(roleId==null || roleId<=0) {
            logger.error("角色移除资源失败，角色ID为空");
            throw new RoleException("角色移除资源失败，角色ID为空");
        }
        try {
            // 查询角色全部已配置的资源
            SearchParams searchParams = new SearchParams();
            searchParams.addCondition("roleId", roleId);
            searchParams.addCondition("searchConfig", "configured");
            List<Resource> resources = this.resourceService.queryList(searchParams);
            /* 移除已配置角色资源数据 */
            RoleResource roleResource = new RoleResource();
            for(Resource resource : resources) {
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resource.getId());
                this.removeRoleResource(roleResource);
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色移除资源异常，参数：" + roleId, e);
            throw new RoleException("角色移除资源异常，请联系管理员");
        }
    }

    /**
     * 角色移除资源
     * @param roleResources
     * @throws RoleException
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleResources(List<RoleResource> roleResources) throws RoleException {
        if(roleResources==null || roleResources.size()<=0) {
            return;
        }
        try {
            for(RoleResource roleResource : roleResources) {
                this.removeRoleResource(roleResource);
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色移除资源异常，参数：" + roleResources, e);
            throw new RoleException("角色移除资源异常，请联系管理员");
        }
    }

    /**
     * 移除角色-资源关系数据
     * @param roleResource
     * @return
     * @throws RoleException
     */
    public RoleResource removeRoleResource(RoleResource roleResource) throws RoleException {
        if(roleResource==null) {
            logger.error("角色移除资源失败，参数为空");
            throw new RoleException("角色移除资源失败，参数为空");
        }
        if(roleResource.getRoleId()==null || roleResource.getRoleId()<=0) {
            logger.error("角色移除资源失败，角色ID为空");
            throw new RoleException("角色移除资源失败，角色ID为空");
        }
        if(roleResource.getResourceId()==null || roleResource.getResourceId()<=0) {
            logger.error("角色移除资源失败，资源ID为空");
            throw new RoleException("角色移除资源失败，资源ID为空");
        }
        try {
            int remove = this.roleResourceDao.removeRoleResource(roleResource);
            if(remove==1) {
                return roleResource;
            } else {
                logger.error("角色移除资源失败，移除返回数量为0，参数：" + roleResource);
                throw new RoleException("角色移除资源失败，请联系管理员");
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("角色移除资源异常，参数：" + roleResource, e);
            throw new RoleException("角色移除资源异常，请联系管理员");
        }
    }
}

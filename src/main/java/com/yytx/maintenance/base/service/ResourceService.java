package com.yytx.maintenance.base.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.dao.ResourceDao;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.constant.OperationLogBusinessTypeEnum;
import com.yytx.maintenance.constant.OperationLogOperationTypeEnum;
import com.yytx.maintenance.context.UserInfo;
import com.yytx.maintenance.excepion.ResourceException;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.InitializeObjectUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceService {
    private Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ResourceDao resourceDao;

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
            logger.error("查询资源失败，参数为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询资源失败，参数错误，pageNum为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询资源失败，参数错误，pageSize为空");
            throw new ResourceException("查询资源失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<Resource> page = resourceDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询资源分页异常，参数：" + searchParams.getSearchMap(), e);
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
            logger.error("查询资源数据异常，参数：" + searchParams.getSearchMap(), e);
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
            logger.error("查询资源信息失败，ID为空");
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
                    logger.error("新增资源信息失败，返回数量为0");
                    throw new ResourceException("保存资源信息失败，请联系管理员");
                }
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增资源信息异常，参数：" + resource, e);
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
            logger.error("修改资源信息失败，参数为空");
            throw new ResourceException("保存资源信息失败，参数为空");
        }
        if(resource.getId()==null || resource.getId()<=0) {
            logger.error("修改资源信息失败，资源ID为空");
            throw new ResourceException("保存资源信息失败，资源ID为空");
        }
        if(StringUtils.isEmpty(resource.getName())) {
            logger.error("修改资源信息失败，资源名称为空");
            throw new ResourceException("保存资源信息失败，资源名称为空");
        }
        if(StringUtils.isEmpty(resource.getOwner())) {
            logger.error("修改资源信息失败，资源归属为空");
            throw new ResourceException("保存资源信息失败，资源归属为空");
        }
        if(StringUtils.isEmpty(resource.getType())) {
            logger.error("修改资源信息失败，资源类型为空");
            throw new ResourceException("保存资源信息失败，资源类型为空");
        }
        try {
            // 查询资源信息，校验数据
            Resource oldResource = this.getResourceById(resource.getId());
            // 校验版本号
            if(!oldResource.getVersion().equals(resource.getVersion())) {
                logger.error("修改资源信息失败，版本号不一致");
                throw new ResourceException("保存资源信息失败，资源已被修改，请重新刷新页面重试");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(resource, UserInfo.getInstance());
            int add = this.resourceDao.updateResource(resource);
            if(add==1) {
                resource = this.getResourceById(resource.getId());
            } else {
                logger.error("修改资源信息失败，返回数量为0");
                throw new ResourceException("保存资源信息失败，请联系管理员");
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("修改资源信息异常，参数：" + resource, e);
            throw new ResourceException("保存资源信息异常，请联系管理员");
        }
        return resource;
    }
//
//    /**
//     * 新增资源信息
//     * @param roleResourceVo    资源信息
//     * @return 资源信息
//     * @throws ResourceException 自定义异常
//     */
//    @Transactional(rollbackFor=Exception.class)
//    public RoleResourceVo addRoleManager(RoleResourceVo roleResourceVo) throws ResourceException {
//        if(roleResourceVo==null) {
//            logger.error("保存资源信息失败，参数为空");
//            throw new ResourceException("保存资源信息失败，参数为空");
//        }
//        if(StringUtils.isEmpty(roleResourceVo.getName())) {
//            logger.error("保存资源信息失败，资源名称为空");
//            throw new ResourceException("保存资源信息失败，资源名称为空");
//        }
//        if(StringUtils.isEmpty(roleResourceVo.getCode())) {
//            logger.error("保存资源信息失败，资源编码为空");
//            throw new ResourceException("保存资源信息失败，资源编码为空");
//        }
//        try {
//            /* 新增资源信息 */
//            Role role = roleResourceVo.generateRole();
//            role = this.addRole(role);
//            if(role!=null && role.getId()>0) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(role.getId(),
//                        OperationLogBusinessTypeEnum.ROLE.getKey(),
//                        OperationLogOperationTypeEnum.ADD.getKey(),
//                        UserInfo.getInstance().getUserName() + "新增资源：" + role.getName() + "（" + role.getCode() + "）",
//                        UserInfo.getInstance()));
//                /* 新增资源资源信息 */
//                this.addRoleResourceRelation(role, roleResourceVo.getResourceList());
//            } else {
//                logger.error("新增资源信息失败，返回数量为0，参数：" + roleResourceVo);
//                throw new ResourceException("新增资源信息失败，请联系管理员");
//            }
//            roleResourceVo.initializationRole(role);
//        } catch (ResourceException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("保存资源信息异常，参数：" + roleResourceVo, e);
//            throw new ResourceException("保存资源信息异常，请联系管理员");
//        }
//        return roleResourceVo;
//    }
//
    /**
     * 校验资源编码是否存在
     * @param resource  资源
     * @return
     */
    private boolean checkResourceKeyExists(Resource resource) {
        return this.resourceDao.checkResourceKeyExists(resource)>0;
    }
//
//    /**
//     * 新增资源和资源关系
//     * @param role 资源信息
//     * @param resourceList  资源信息
//     * @return
//     */
//    private int addRoleResourceRelation(Role role, List<Resource> resourceList) {
//        int success = 0;
//        if(resourceList!=null && resourceList.size()>0) {
//            RoleResource roleResource = new RoleResource();
//            roleResource.setRoleId(role.getId());
//            for(Resource resource : resourceList) {
//                roleResource.setResourceId(resource.getId());
//                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(roleResource, UserInfo.getInstance());
//                int addRelation = this.resourceDao.addRoleResourceRelation(roleResource);
//                if(addRelation==1) {
//                    success += addRelation;
//                } else {
//                    logger.error("添加资源资源关系失败，返回数量为0");
//                }
//            }
//        }
//        return success;
//    }

    /**
     * 批量刪除资源
     * @param resources  资源信息
     * @return
     */
    public void batchDeleteResource(List<Resource> resources) throws ResourceException {
        if(resources==null || resources.size()<=0) {
            logger.error("删除资源失败，参数为空");
            throw new ResourceException("删除资源失败，参数为空");
        }
        try {
            for(Resource resource : resources) {
                this.deleteResource(resource);
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除资源异常，参数：" + resources, e);
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
            logger.error("删除资源失败，参数为空");
            throw new ResourceException("删除资源失败，参数为空");
        }
        if(resource.getId()==null || resource.getId()<=0) {
            logger.error("删除资源失败，资源ID为空");
            throw new ResourceException("删除资源失败，资源ID为空");
        }
        if(resource.getVersion()==null || resource.getVersion()<0) {
            logger.error("删除资源失败，版本号为空");
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
                        UserInfo.getInstance().getUserName() + "删除了资源：" + resource.getName() + "（" + resource.getKey() + "）",
                        UserInfo.getInstance()));
                // 删除资源关系
                this.deleteRoleResourceByResourceId(resource.getId());
            } else {
                logger.error("删除资源失败，删除数量返回为0，参数：" + resource);
                throw new ResourceException("删除资源失败，资源已被修改，请重新刷新页面重试");
            }
        } catch (ResourceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除资源异常，参数：" + resource, e);
            throw new ResourceException("删除资源异常，请联系管理员");
        }
    }

    /**
     * 删除资源相关的资源关系
     * @param resourceId 资源ID
     * @return
     */
    private int deleteRoleResourceByResourceId(Long resourceId) {
        int success = 0;
        if(resourceId!=null && resourceId>0) {
            success = this.resourceDao.deleteRoleResourceByResourceId(resourceId);
        }
        return success;
    }
//
//    /**
//     * 修改资源信息
//     * @param userRoleVo    资源信息
//     * @return 资源信息
//     * @throws ResourceException 自定义异常
//     */
//    @Transactional(rollbackFor=Exception.class)
//    public UserRoleVo updateUserManager(UserRoleVo userRoleVo) throws ResourceException {
//        if(userRoleVo==null) {
//            logger.error("保存资源信息失败，参数为空");
//            throw new ResourceException("保存资源信息失败，参数为空");
//        }
//        if(userRoleVo.getId()!=null && userRoleVo.getId()<=0) {
//            logger.error("保存资源信息失败，资源ID为空");
//            throw new ResourceException("保存资源信息失败，资源ID为空");
//        }
//        if(userRoleVo.getVersion()!=null && userRoleVo.getVersion()<0) {
//            logger.error("保存资源信息失败，版本号为空");
//            throw new ResourceException("保存资源信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getPassword())) {
//            logger.error("保存资源信息失败，资源密码为空");
//            throw new ResourceException("保存资源信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getName())) {
//            logger.error("保存资源信息失败，资源名称为空");
//            throw new ResourceException("保存资源信息失败，资源名称为空");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getLoginAccount())) {
//            logger.error("保存资源信息失败，资源登录账号为空");
//            throw new ResourceException("保存资源信息失败，资源登录账号为空");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getEmail()) && !CommUtils.isEmail(userRoleVo.getEmail())) {
//            logger.error("保存资源信息失败，邮箱校验失败");
//            throw new ResourceException("保存资源信息失败，邮箱格式错误");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getMobile()) && !CommUtils.isMobileOrPhone(userRoleVo.getMobile())) {
//            logger.error("保存资源信息失败，电话号码校验失败");
//            throw new ResourceException("保存资源信息失败，电话号码格式错误");
//        }
//        try {
//            // 查询资源信息，校验数据
//            User oldUser = this.resourceDao.getUserById(userRoleVo.getId());
//            // 校验版本号
//            if(!oldUser.getVersion().equals(userRoleVo.getVersion())) {
//                logger.error("保存资源信息失败，版本号不一致");
//                throw new ResourceException("保存资源信息失败，资源已被修改，请重新刷新页面重试");
//            }
//            // 校验账号，账号不可修改
//            if(!oldUser.getLoginAccount().equals(userRoleVo.getLoginAccount())) {
//                logger.error("保存资源信息失败，账号不一致");
//                throw new ResourceException("保存资源信息失败，参数错误");
//            }
//            /* 修改资源信息 */
//            User user = userRoleVo.generateUser();
//            int updateUser = this.updateUser(user);
//            if(updateUser==1) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(user.getId(),
//                        OperationLogBusinessTypeEnum.USER.getKey(),
//                        OperationLogOperationTypeEnum.UPDATE.getKey(),
//                        UserInfo.getInstance().getUserName() + "修改资源：" + user.getName() + "（" + user.getLoginAccount() + "）",
//                        UserInfo.getInstance()));
//                /* 处理资源问题 */
//                if(userRoleVo.getRoleList()!=null && userRoleVo.getRoleList().size()>0) {
//                    List<Role> roles = userRoleVo.getRoleList();
//                    List<Long> removeRoleIds = new ArrayList<Long>();
//                    // 查询已存在的资源关系
//                    List<UserRole> userRoles = this.resourceDao.queryUserRoleList(user.getId());
//                    if(userRoles!=null && userRoles.size()>0) {
//                        /* 判断逻辑，如果匹配到相同的资源ID，那么就从待新增的资源集合中移除，如果没有匹配到相同的资源ID，那么将已存在的资源ID添加到待删除的集合中 */
//                        boolean isExist;
//                        for(UserRole userRole : userRoles) {
//                            isExist = false;
//                            for(Role role : roles) {
//                                if(userRole.getRoleId().equals(role.getId())) {
//                                    roles.remove(role);
//                                    isExist = true;
//                                }
//                            }
//                            if(!isExist) {
//                                removeRoleIds.add(userRole.getRoleId());
//                            }
//                        }
//                    }
//                    if(removeRoleIds.size()>0) {
//                        this.resourceDao.deleteUserRoleByUserIdAndRoleIds(user.getId(), removeRoleIds);
//                    }
//                    if(roles.size()>0) {
//                        /* 新增资源信息 */
//                        this.addUserRoleRelation(user, roles);
//                    }
//                } else {
//                    // 删除资源资源关联信息
//                    this.deleteUserRoleByUserId(user.getId());
//                }
//            } else {
//                logger.error("保存资源信息失败，返回数量为0，参数：" + user);
//                throw new ResourceException("保存资源信息失败，资源已被修改，请重新刷新页面重试");
//            }
//            userRoleVo.initializationUser(user);
//        } catch (ResourceException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("保存资源信息异常，参数：" + userRoleVo, e);
//            throw new ResourceException("保存资源信息异常，请联系管理员");
//        }
//        return userRoleVo;
//    }
//
}

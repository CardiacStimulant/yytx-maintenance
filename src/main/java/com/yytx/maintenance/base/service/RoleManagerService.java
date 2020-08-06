package com.yytx.maintenance.base.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.dao.RoleManagerDao;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.base.entity.RoleResource;
import com.yytx.maintenance.base.vo.RoleResourceVo;
import com.yytx.maintenance.constant.OperationLogBusinessTypeEnum;
import com.yytx.maintenance.constant.OperationLogOperationTypeEnum;
import com.yytx.maintenance.context.UserInfo;
import com.yytx.maintenance.excepion.RoleManagerException;
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
public class RoleManagerService {
    private Logger logger = LoggerFactory.getLogger(RoleManagerService.class);

    @Autowired
    private RoleManagerDao roleManagerDao;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询角色管理分页数据
     *
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 角色管理分页数据
     * @throws RoleManagerException 自定义异常
     */
    public PageInfo<Role> queryPage(SearchParams searchParams) throws RoleManagerException{
        if(searchParams==null || searchParams.getSearchMap()==null) {
            logger.error("查询角色管理失败，参数为空");
            throw new RoleManagerException("查询角色管理失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询角色管理失败，参数错误，pageNum为空");
            throw new RoleManagerException("查询角色管理失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询角色管理失败，参数错误，pageSize为空");
            throw new RoleManagerException("查询角色管理失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<Role> page = roleManagerDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询角色管理分页异常，参数：" + searchParams.getSearchMap(), e);
            throw new RoleManagerException("查询角色管理分页异常，请联系管理员");
        }
    }

    /**
     * 查询所有角色信息
     * @param searchParams 查询条件
     * @return 角色分页数据
     */
    public List<Role> queryList(SearchParams searchParams) throws RoleManagerException {
        try {
            return roleManagerDao.queryList(searchParams);
        } catch (Exception e) {
            logger.error("查询角色信息异常，参数：" + searchParams.getSearchMap(), e);
            throw new RoleManagerException("查询角色信息异常，请联系管理员");
        }
    }

    /**
     * 根据ID查询角色信息
     * @param roleId
     * @return
     * @throws RoleManagerException
     */
    public Role getRoleById(Long roleId) throws RoleManagerException {
        if(roleId==null || roleId<=0) {
            logger.error("查询角色信息失败，ID为空");
            throw new RoleManagerException("查询角色信息失败，角色ID为空");
        }
        try {
            return this.roleManagerDao.getRoleById(roleId);
        } catch (Exception e) {
            throw new RoleManagerException("查询角色信息异常，请联系管理员");
        }
    }

    /**
     * 新增角色信息
     * @param role
     * @return
     */
    public Role addRole(Role role) throws RoleManagerException {
        try {
            // 校验角色编码是否重复
            boolean isExists = this.checkRoleCodeExists(role.getCode());
            if(isExists) {
                throw new RoleManagerException("新增角色信息失败，角色编码重复");
            } else {
                // 设置创建信息和修改信息
                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(role, UserInfo.getInstance());
                int add = this.roleManagerDao.addRole(role);
                if(add==1) {
                    role = this.getRoleById(role.getId());
                } else {
                    logger.error("新增角色信息失败，返回数量为0");
                    throw new RoleManagerException("保存角色信息失败，请联系管理员");
                }
            }
        } catch (RoleManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增角色信息异常，参数：" + role, e);
            throw new RoleManagerException("保存角色信息异常，请联系管理员");
        }
        return role;
    }

    /**
     * 修改角色信息
     * @param role
     * @return
     */
    public Role updateRole(Role role) throws RoleManagerException {
        if(role==null) {
            logger.error("修改角色信息失败，参数为空");
            throw new RoleManagerException("保存角色信息失败，参数为空");
        }
        if(role.getId()==null || role.getId()<=0) {
            logger.error("修改角色信息失败，角色ID为空");
            throw new RoleManagerException("保存角色信息失败，角色ID为空");
        }
        if(StringUtils.isEmpty(role.getName())) {
            logger.error("修改角色信息失败，角色名称为空");
            throw new RoleManagerException("保存角色信息失败，角色名称为空");
        }
        if(StringUtils.isEmpty(role.getCode())) {
            logger.error("修改角色信息失败，角色编码为空");
            throw new RoleManagerException("保存角色信息失败，角色编码为空");
        }
        try {
            // 查询角色信息，校验数据
            Role oldRole = this.getRoleById(role.getId());
            // 校验版本号
            if(!oldRole.getVersion().equals(role.getVersion())) {
                logger.error("修改角色信息失败，版本号不一致");
                throw new RoleManagerException("保存角色信息失败，角色已被修改，请重新刷新页面重试");
            }
            // 校验账号，角色编码不可修改
            if(!role.getCode().equals(oldRole.getCode())) {
                logger.error("保存角色信息失败，角色编码不一致");
                throw new RoleManagerException("保存角色信息失败，参数错误");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(role, UserInfo.getInstance());
            int add = this.roleManagerDao.updateRole(role);
            if(add==1) {
                role = this.getRoleById(role.getId());
            } else {
                logger.error("修改角色信息失败，返回数量为0");
                throw new RoleManagerException("保存角色信息失败，请联系管理员");
            }
        } catch (RoleManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("修改角色信息异常，参数：" + role, e);
            throw new RoleManagerException("保存角色信息异常，请联系管理员");
        }
        return role;
    }

    /**
     * 新增角色管理信息
     * @param roleResourceVo    角色管理信息
     * @return 角色管理信息
     * @throws RoleManagerException 自定义异常
     */
    @Transactional(rollbackFor=Exception.class)
    public RoleResourceVo addRoleManager(RoleResourceVo roleResourceVo) throws RoleManagerException {
        if(roleResourceVo==null) {
            logger.error("保存角色信息失败，参数为空");
            throw new RoleManagerException("保存角色信息失败，参数为空");
        }
        if(StringUtils.isEmpty(roleResourceVo.getName())) {
            logger.error("保存角色信息失败，角色名称为空");
            throw new RoleManagerException("保存角色信息失败，角色名称为空");
        }
        if(StringUtils.isEmpty(roleResourceVo.getCode())) {
            logger.error("保存角色信息失败，角色编码为空");
            throw new RoleManagerException("保存角色信息失败，角色编码为空");
        }
        try {
            /* 新增角色信息 */
            Role role = roleResourceVo.generateRole();
            role = this.addRole(role);
            if(role!=null && role.getId()>0) {
                // 添加操作日志
                operationLogService.save(new OperationLog(role.getId(),
                        OperationLogBusinessTypeEnum.ROLE.getKey(),
                        OperationLogOperationTypeEnum.ADD.getKey(),
                        UserInfo.getInstance().getUserName() + "新增角色：" + role.getName() + "（" + role.getCode() + "）",
                        UserInfo.getInstance()));
                /* 新增角色资源信息 */
                this.addRoleResourceRelation(role, roleResourceVo.getResourceList());
            } else {
                logger.error("新增角色信息失败，返回数量为0，参数：" + roleResourceVo);
                throw new RoleManagerException("新增角色信息失败，请联系管理员");
            }
            roleResourceVo.initializationRole(role);
        } catch (RoleManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("保存角色信息异常，参数：" + roleResourceVo, e);
            throw new RoleManagerException("保存角色信息异常，请联系管理员");
        }
        return roleResourceVo;
    }

    /**
     * 校验角色编码是否存在
     * @param roleCode  角色编码
     * @return
     */
    private boolean checkRoleCodeExists(String roleCode) {
        return this.roleManagerDao.checkRoleCodeExists(roleCode)>0;
    }

    /**
     * 新增角色和资源关系
     * @param role 角色信息
     * @param resourceList  资源信息
     * @return
     */
    private int addRoleResourceRelation(Role role, List<Resource> resourceList) {
        int success = 0;
        if(resourceList!=null && resourceList.size()>0) {
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(role.getId());
            for(Resource resource : resourceList) {
                roleResource.setResourceId(resource.getId());
                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(roleResource, UserInfo.getInstance());
                int addRelation = this.roleManagerDao.addRoleResourceRelation(roleResource);
                if(addRelation==1) {
                    success += addRelation;
                } else {
                    logger.error("添加角色资源关系失败，返回数量为0");
                }
            }
        }
        return success;
    }

    /**
     * 刪除角色
     * @param roles  角色信息
     * @return
     */
    public void batchDeleteRole(List<Role> roles) throws RoleManagerException {
        if(roles==null || roles.size()<=0) {
            logger.error("删除角色失败，参数为空");
            throw new RoleManagerException("删除角色失败，参数为空");
        }
        try {
            for(Role role : roles) {
                this.deleteRole(role);
            }
        } catch (RoleManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除角色异常，参数：" + roles, e);
            throw new RoleManagerException("删除角色异常，请联系管理员");
        }
    }

    /**
     * 刪除角色信息
     * @param role  角色信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Role role) throws RoleManagerException {
        if(role==null) {
            logger.error("删除角色失败，参数为空");
            throw new RoleManagerException("删除角色失败，参数为空");
        }
        if(role.getId()==null || role.getId()<=0) {
            logger.error("删除角色失败，角色ID为空");
            throw new RoleManagerException("删除角色失败，角色ID为空");
        }
        if(role.getVersion()==null || role.getVersion()<0) {
            logger.error("删除角色失败，版本号为空");
            throw new RoleManagerException("删除角色失败，参数错误");
        }
        try {
            // 删除角色信息
            int delete = this.roleManagerDao.deleteRole(role);
            if(delete==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(role.getId(),
                        OperationLogBusinessTypeEnum.ROLE.getKey(),
                        OperationLogOperationTypeEnum.DELETE.getKey(),
                        UserInfo.getInstance().getUserName() + "删除了角色：" + role.getName() + "（" + role.getCode() + "）",
                        UserInfo.getInstance()));
                // 删除角色关系
                this.deleteRoleResourceByRoleId(role.getId());
            } else {
                logger.error("删除角色失败，删除数量返回为0，参数：" + role);
                throw new RoleManagerException("删除角色失败，角色已被修改，请重新刷新页面重试");
            }
        } catch (RoleManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除角色管理异常，参数：" + role, e);
            throw new RoleManagerException("删除角色管理异常，请联系管理员");
        }
    }

    /**
     * 删除角色的资源关系
     * @param userId 角色ID
     * @return
     */
    private int deleteRoleResourceByRoleId(Long userId) {
        int success = 0;
        if(userId!=null && userId>0) {
            success = this.roleManagerDao.deleteRoleResourceByRoleId(userId);
        }
        return success;
    }
//
//    /**
//     * 修改角色管理信息
//     * @param userRoleVo    角色管理信息
//     * @return 角色管理信息
//     * @throws RoleManagerException 自定义异常
//     */
//    @Transactional(rollbackFor=Exception.class)
//    public UserRoleVo updateUserManager(UserRoleVo userRoleVo) throws RoleManagerException {
//        if(userRoleVo==null) {
//            logger.error("保存角色信息失败，参数为空");
//            throw new RoleManagerException("保存角色信息失败，参数为空");
//        }
//        if(userRoleVo.getId()!=null && userRoleVo.getId()<=0) {
//            logger.error("保存角色信息失败，角色ID为空");
//            throw new RoleManagerException("保存角色信息失败，角色ID为空");
//        }
//        if(userRoleVo.getVersion()!=null && userRoleVo.getVersion()<0) {
//            logger.error("保存角色信息失败，版本号为空");
//            throw new RoleManagerException("保存角色信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getPassword())) {
//            logger.error("保存角色信息失败，角色密码为空");
//            throw new RoleManagerException("保存角色信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getName())) {
//            logger.error("保存角色信息失败，角色名称为空");
//            throw new RoleManagerException("保存角色信息失败，角色名称为空");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getLoginAccount())) {
//            logger.error("保存角色信息失败，角色登录账号为空");
//            throw new RoleManagerException("保存角色信息失败，角色登录账号为空");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getEmail()) && !CommUtils.isEmail(userRoleVo.getEmail())) {
//            logger.error("保存角色信息失败，邮箱校验失败");
//            throw new RoleManagerException("保存角色信息失败，邮箱格式错误");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getMobile()) && !CommUtils.isMobileOrPhone(userRoleVo.getMobile())) {
//            logger.error("保存角色信息失败，电话号码校验失败");
//            throw new RoleManagerException("保存角色信息失败，电话号码格式错误");
//        }
//        try {
//            // 查询角色信息，校验数据
//            User oldUser = this.roleManagerDao.getUserById(userRoleVo.getId());
//            // 校验版本号
//            if(!oldUser.getVersion().equals(userRoleVo.getVersion())) {
//                logger.error("保存角色信息失败，版本号不一致");
//                throw new RoleManagerException("保存角色信息失败，角色已被修改，请重新刷新页面重试");
//            }
//            // 校验账号，账号不可修改
//            if(!oldUser.getLoginAccount().equals(userRoleVo.getLoginAccount())) {
//                logger.error("保存角色信息失败，账号不一致");
//                throw new RoleManagerException("保存角色信息失败，参数错误");
//            }
//            /* 修改角色信息 */
//            User user = userRoleVo.generateUser();
//            int updateUser = this.updateUser(user);
//            if(updateUser==1) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(user.getId(),
//                        OperationLogBusinessTypeEnum.USER.getKey(),
//                        OperationLogOperationTypeEnum.UPDATE.getKey(),
//                        UserInfo.getInstance().getUserName() + "修改角色：" + user.getName() + "（" + user.getLoginAccount() + "）",
//                        UserInfo.getInstance()));
//                /* 处理角色问题 */
//                if(userRoleVo.getRoleList()!=null && userRoleVo.getRoleList().size()>0) {
//                    List<Role> roles = userRoleVo.getRoleList();
//                    List<Long> removeRoleIds = new ArrayList<Long>();
//                    // 查询已存在的角色关系
//                    List<UserRole> userRoles = this.roleManagerDao.queryUserRoleList(user.getId());
//                    if(userRoles!=null && userRoles.size()>0) {
//                        /* 判断逻辑，如果匹配到相同的角色ID，那么就从待新增的角色集合中移除，如果没有匹配到相同的角色ID，那么将已存在的角色ID添加到待删除的集合中 */
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
//                        this.roleManagerDao.deleteUserRoleByUserIdAndRoleIds(user.getId(), removeRoleIds);
//                    }
//                    if(roles.size()>0) {
//                        /* 新增角色信息 */
//                        this.addUserRoleRelation(user, roles);
//                    }
//                } else {
//                    // 删除角色角色关联信息
//                    this.deleteUserRoleByUserId(user.getId());
//                }
//            } else {
//                logger.error("保存角色信息失败，返回数量为0，参数：" + user);
//                throw new RoleManagerException("保存角色信息失败，角色已被修改，请重新刷新页面重试");
//            }
//            userRoleVo.initializationUser(user);
//        } catch (RoleManagerException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("保存角色信息异常，参数：" + userRoleVo, e);
//            throw new RoleManagerException("保存角色信息异常，请联系管理员");
//        }
//        return userRoleVo;
//    }
//
}

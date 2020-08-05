package com.yytx.maintenance.base.service;

import com.yytx.maintenance.base.dao.RoleManagerDao;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.excepion.RoleManagerException;
import com.yytx.maintenance.pojo.SearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManagerService {
    private Logger logger = LoggerFactory.getLogger(RoleManagerService.class);

    @Autowired
    private RoleManagerDao roleManagerDao;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询用户管理分页数据
     *
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 用户管理分页数据
     * @throws RoleManagerException 自定义异常
     */
//    public PageInfo<UserRoleVo> queryPage(SearchParams searchParams) throws RoleManagerException{
//        if(searchParams==null || searchParams.getSearchMap()==null) {
//            logger.error("查询用户管理失败，参数为空");
//            throw new RoleManagerException("查询用户管理失败，参数错误");
//        }
//        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
//        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
//        if(pageNum==null || pageNum<=0) {
//            logger.error("查询用户管理失败，参数错误，pageNum为空");
//            throw new RoleManagerException("查询用户管理失败，参数错误");
//        }
//        if(pageSize==null || pageSize<=0) {
//            logger.error("查询用户管理失败，参数错误，pageSize为空");
//            throw new RoleManagerException("查询用户管理失败，参数错误");
//        }
//        try {
//            PageHelper.startPage(pageNum, pageSize);
//            Page<UserRoleVo> page = userManagerDao.queryPage(searchParams);
//            return page.toPageInfo();
//        } catch (Exception e) {
//            logger.error("查询用户管理分页异常，参数：" + searchParams.getSearchMap(), e);
//            throw new RoleManagerException("查询用户管理分页异常，请联系管理员");
//        }
//    }

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
     * 保存用户管理信息
     * @param userRoleVo    用户管理信息
     * @return 用户管理信息
     * @throws RoleManagerException 自定义异常
     */
//    @Transactional(rollbackFor=Exception.class)
//    public UserRoleVo addUserManager(UserRoleVo userRoleVo) throws RoleManagerException {
//        if(userRoleVo==null) {
//            logger.error("保存用户信息失败，参数为空");
//            throw new RoleManagerException("保存用户信息失败，参数为空");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getName())) {
//            logger.error("保存用户信息失败，用户姓名为空");
//            throw new RoleManagerException("保存用户信息失败，用户姓名为空");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getLoginAccount())) {
//            logger.error("保存用户信息失败，用户登录账号为空");
//            throw new RoleManagerException("保存用户信息失败，用户登录账号为空");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getEmail()) && !CommUtils.isEmail(userRoleVo.getEmail())) {
//            logger.error("保存用户信息失败，邮箱校验失败");
//            throw new RoleManagerException("保存用户信息失败，邮箱格式错误");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getMobile()) && !CommUtils.isMobileOrPhone(userRoleVo.getMobile())) {
//            logger.error("保存用户信息失败，电话号码校验失败");
//            throw new RoleManagerException("保存用户信息失败，电话号码格式错误");
//        }
//        try {
//            /* 新增用户信息 */
//            User user = userRoleVo.generateUser();
//            int addUser = this.addUser(user);
//            if(addUser==1) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(user.getId(),
//                        OperationLogBusinessTypeEnum.USER.getKey(),
//                        OperationLogOperationTypeEnum.ADD.getKey(),
//                        UserInfo.getInstance().getUserName() + "新增用户：" + user.getName() + "（" + user.getLoginAccount() + "）",
//                        UserInfo.getInstance()));
//                /* 新增角色信息 */
//                this.addUserRoleRelation(user, userRoleVo.getRoleList());
//            } else {
//                logger.error("新增用户信息失败，返回数量为0，参数：" + userRoleVo);
//                throw new RoleManagerException("新增用户信息失败，请联系管理员");
//            }
//            userRoleVo.initializationUser(user);
//        } catch (RoleManagerException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("保存用户信息异常，参数：" + userRoleVo, e);
//            throw new RoleManagerException("保存用户信息异常，请联系管理员");
//        }
//        return userRoleVo;
//    }

//    /**
//     * 新增用户信息
//     * @param user
//     * @return
//     */
//    private int addUser(User user) {
//        // 校验用户登录账号是否重复
//        boolean isExists = this.checkUserLoginAccountExists(user.getLoginAccount());
//        if(isExists) {
//            throw new RoleManagerException("新增用户信息失败，登录账号重复");
//        } else {
//            // 设置默认密码
//            if(StringUtils.isEmpty(user.getPassword())) {
//                user.setPassword(Encrypt.md5Encrypt("123456"));
//            }
//            // 设置创建信息和修改信息
//            InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(user, UserInfo.getInstance());
//            return this.userManagerDao.addUser(user);
//        }
//    }
//
//    /**
//     * 校验用户登录账号是否存在
//     * @param loginAccount  登录账号
//     * @return
//     */
//    private boolean checkUserLoginAccountExists(String loginAccount) {
//        return this.userManagerDao.checkUserLoginAccountExists(loginAccount)>0;
//    }
//
//    /**
//     * 新增用户和角色关系
//     * @param user 用户信息
//     * @param roleList  角色信息
//     * @return
//     */
//    private int addUserRoleRelation(User user, List<Role> roleList) {
//        int success = 0;
//        if(roleList!=null && roleList.size()>0) {
//            UserRole userRole = new UserRole();
//            userRole.setUserId(user.getId());
//            for(Role role : roleList) {
//                userRole.setRoleId(role.getId());
//                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(userRole, UserInfo.getInstance());
//                int addRelation = this.userManagerDao.addUserRoleRelation(userRole);
//                if(addRelation==1) {
//                    success += addRelation;
//                    // 添加操作日志
////                    operationLogService.save(new OperationLog(userRole.getId(),
////                            OperationLogBusinessTypeEnum.USER_ROLE_RELATION.getKey(),
////                            OperationLogOperationTypeEnum.ADD.getKey(),
////                            UserInfo.getInstance().getUserName() + "新增用户角色关系，用户：" + user.getName() + "（" + user.getLoginAccount() + "）" + "添加角色：" + role.getName(),
////                            UserInfo.getInstance()));
//                } else {
//                    logger.error("添加用户角色失败，返回数量为0");
//                }
//            }
//        }
//        return success;
//    }
//
//    /**
//     * 刪除用户管理信息
//     * @param userRoleVo  用户角色信息
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteUserManager(UserRoleVo userRoleVo) throws RoleManagerException {
//        if(userRoleVo==null) {
//            logger.error("删除用户失败，参数为空");
//            throw new RoleManagerException("删除用户失败，参数为空");
//        }
//        if(userRoleVo.getId()==null || userRoleVo.getId()<=0) {
//            logger.error("删除用户失败，用户ID为空");
//            throw new RoleManagerException("删除用户失败，用户ID为空");
//        }
//        if(userRoleVo.getVersion()==null || userRoleVo.getVersion()<0) {
//            logger.error("删除用户失败，版本号为空");
//            throw new RoleManagerException("删除用户失败，参数错误");
//        }
//        try {
//            // 删除用户信息
//            int delete = this.userManagerDao.deleteUser(userRoleVo.generateUser());
//            if(delete==1) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(userRoleVo.getId(),
//                        OperationLogBusinessTypeEnum.USER.getKey(),
//                        OperationLogOperationTypeEnum.DELETE.getKey(),
//                        UserInfo.getInstance().getUserName() + "删除了用户：" + userRoleVo.getName() + "（" + userRoleVo.getLoginAccount() + "）",
//                        UserInfo.getInstance()));
//                // 删除角色关系
//                this.deleteUserRoleByUserId(userRoleVo.getId());
//            } else {
//                logger.error("删除用户失败，删除数量返回为0，参数：" + userRoleVo);
//                throw new RoleManagerException("删除用户失败，用户已被修改，请重新刷新页面重试");
//            }
//        } catch (RoleManagerException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("删除用户管理异常，参数：" + userRoleVo, e);
//            throw new RoleManagerException("删除用户管理异常，请联系管理员");
//        }
//    }
//
//    /**
//     * 删除用户的角色关系
//     * @param userId 用户ID
//     * @return
//     */
//    private int deleteUserRoleByUserId(Long userId) {
//        int success = 0;
//        if(userId!=null && userId>0) {
//            success = this.userManagerDao.deleteUserRoleByUserId(userId);
//        }
//        return success;
//    }
//
//    /**
//     * 修改用户管理信息
//     * @param userRoleVo    用户管理信息
//     * @return 用户管理信息
//     * @throws RoleManagerException 自定义异常
//     */
//    @Transactional(rollbackFor=Exception.class)
//    public UserRoleVo updateUserManager(UserRoleVo userRoleVo) throws RoleManagerException {
//        if(userRoleVo==null) {
//            logger.error("保存用户信息失败，参数为空");
//            throw new RoleManagerException("保存用户信息失败，参数为空");
//        }
//        if(userRoleVo.getId()!=null && userRoleVo.getId()<=0) {
//            logger.error("保存用户信息失败，用户ID为空");
//            throw new RoleManagerException("保存用户信息失败，用户ID为空");
//        }
//        if(userRoleVo.getVersion()!=null && userRoleVo.getVersion()<0) {
//            logger.error("保存用户信息失败，版本号为空");
//            throw new RoleManagerException("保存用户信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getPassword())) {
//            logger.error("保存用户信息失败，用户密码为空");
//            throw new RoleManagerException("保存用户信息失败，参数错误");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getName())) {
//            logger.error("保存用户信息失败，用户姓名为空");
//            throw new RoleManagerException("保存用户信息失败，用户姓名为空");
//        }
//        if(StringUtils.isEmpty(userRoleVo.getLoginAccount())) {
//            logger.error("保存用户信息失败，用户登录账号为空");
//            throw new RoleManagerException("保存用户信息失败，用户登录账号为空");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getEmail()) && !CommUtils.isEmail(userRoleVo.getEmail())) {
//            logger.error("保存用户信息失败，邮箱校验失败");
//            throw new RoleManagerException("保存用户信息失败，邮箱格式错误");
//        }
//        if(StringUtils.isNotEmpty(userRoleVo.getMobile()) && !CommUtils.isMobileOrPhone(userRoleVo.getMobile())) {
//            logger.error("保存用户信息失败，电话号码校验失败");
//            throw new RoleManagerException("保存用户信息失败，电话号码格式错误");
//        }
//        try {
//            // 查询用户信息，校验数据
//            User oldUser = this.userManagerDao.getUserById(userRoleVo.getId());
//            // 校验版本号
//            if(!oldUser.getVersion().equals(userRoleVo.getVersion())) {
//                logger.error("保存用户信息失败，版本号不一致");
//                throw new RoleManagerException("保存用户信息失败，用户已被修改，请重新刷新页面重试");
//            }
//            // 校验账号，账号不可修改
//            if(!oldUser.getLoginAccount().equals(userRoleVo.getLoginAccount())) {
//                logger.error("保存用户信息失败，账号不一致");
//                throw new RoleManagerException("保存用户信息失败，参数错误");
//            }
//            /* 修改用户信息 */
//            User user = userRoleVo.generateUser();
//            int updateUser = this.updateUser(user);
//            if(updateUser==1) {
//                // 添加操作日志
//                operationLogService.save(new OperationLog(user.getId(),
//                        OperationLogBusinessTypeEnum.USER.getKey(),
//                        OperationLogOperationTypeEnum.UPDATE.getKey(),
//                        UserInfo.getInstance().getUserName() + "修改用户：" + user.getName() + "（" + user.getLoginAccount() + "）",
//                        UserInfo.getInstance()));
//                /* 处理角色问题 */
//                if(userRoleVo.getRoleList()!=null && userRoleVo.getRoleList().size()>0) {
//                    List<Role> roles = userRoleVo.getRoleList();
//                    List<Long> removeRoleIds = new ArrayList<Long>();
//                    // 查询已存在的角色关系
//                    List<UserRole> userRoles = this.userManagerDao.queryUserRoleList(user.getId());
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
//                        this.userManagerDao.deleteUserRoleByUserIdAndRoleIds(user.getId(), removeRoleIds);
//                    }
//                    if(roles.size()>0) {
//                        /* 新增角色信息 */
//                        this.addUserRoleRelation(user, roles);
//                    }
//                } else {
//                    // 删除用户角色关联信息
//                    this.deleteUserRoleByUserId(user.getId());
//                }
//            } else {
//                logger.error("保存用户信息失败，返回数量为0，参数：" + user);
//                throw new RoleManagerException("保存用户信息失败，用户已被修改，请重新刷新页面重试");
//            }
//            userRoleVo.initializationUser(user);
//        } catch (RoleManagerException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("保存用户信息异常，参数：" + userRoleVo, e);
//            throw new RoleManagerException("保存用户信息异常，请联系管理员");
//        }
//        return userRoleVo;
//    }
//
//    /**
//     * 修改用户信息
//     * @param user
//     * @return
//     */
//    private int updateUser(User user) {
//        InitializeObjectUtil.getInstance().initializeModifyInfo(user, UserInfo.getInstance());
//        return this.userManagerDao.updateUser(user);
//    }
}

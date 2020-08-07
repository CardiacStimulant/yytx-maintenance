package com.yytx.maintenance.base.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.dao.RoleDao;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.constant.OperationLogBusinessTypeEnum;
import com.yytx.maintenance.constant.OperationLogOperationTypeEnum;
import com.yytx.maintenance.context.UserInfo;
import com.yytx.maintenance.excepion.RoleException;
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
public class RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询角色管理分页数据
     *
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 角色管理分页数据
     * @throws RoleException 自定义异常
     */
    public PageInfo<Role> queryPage(SearchParams searchParams) throws RoleException {
        if(searchParams==null || searchParams.getSearchMap()==null) {
            logger.error("查询角色管理失败，参数为空");
            throw new RoleException("查询角色管理失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询角色管理失败，参数错误，pageNum为空");
            throw new RoleException("查询角色管理失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询角色管理失败，参数错误，pageSize为空");
            throw new RoleException("查询角色管理失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<Role> page = roleDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询角色管理分页异常，参数：" + searchParams.getSearchMap(), e);
            throw new RoleException("查询角色管理分页异常，请联系管理员");
        }
    }

    /**
     * 查询所有角色信息
     * @param searchParams 查询条件
     * @return 角色分页数据
     */
    public List<Role> queryList(SearchParams searchParams) throws RoleException {
        try {
            return roleDao.queryList(searchParams);
        } catch (Exception e) {
            logger.error("查询角色信息异常，参数：" + searchParams.getSearchMap(), e);
            throw new RoleException("查询角色信息异常，请联系管理员");
        }
    }

    /**
     * 根据ID查询角色信息
     * @param roleId
     * @return
     * @throws RoleException
     */
    public Role getRoleById(Long roleId) throws RoleException {
        if(roleId==null || roleId<=0) {
            logger.error("查询角色信息失败，ID为空");
            throw new RoleException("查询角色信息失败，角色ID为空");
        }
        try {
            return this.roleDao.getRoleById(roleId);
        } catch (Exception e) {
            throw new RoleException("查询角色信息异常，请联系管理员");
        }
    }

    /**
     * 新增角色信息
     * @param role
     * @return
     */
    public Role addRole(Role role) throws RoleException {
        try {
            // 校验角色编码是否重复
            boolean isExists = this.checkRoleCodeExists(role.getCode());
            if(isExists) {
                throw new RoleException("新增角色信息失败，角色编码重复");
            } else {
                // 设置创建信息和修改信息
                InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(role, UserInfo.getInstance());
                int add = this.roleDao.addRole(role);
                if(add==1) {
                    role = this.getRoleById(role.getId());
                } else {
                    logger.error("新增角色信息失败，返回数量为0");
                    throw new RoleException("保存角色信息失败，请联系管理员");
                }
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增角色信息异常，参数：" + role, e);
            throw new RoleException("保存角色信息异常，请联系管理员");
        }
        return role;
    }

    /**
     * 修改角色信息
     * @param role
     * @return
     */
    public Role updateRole(Role role) throws RoleException {
        if(role==null) {
            logger.error("修改角色信息失败，参数为空");
            throw new RoleException("保存角色信息失败，参数为空");
        }
        if(role.getId()==null || role.getId()<=0) {
            logger.error("修改角色信息失败，角色ID为空");
            throw new RoleException("保存角色信息失败，角色ID为空");
        }
        if(StringUtils.isEmpty(role.getName())) {
            logger.error("修改角色信息失败，角色名称为空");
            throw new RoleException("保存角色信息失败，角色名称为空");
        }
        if(StringUtils.isEmpty(role.getCode())) {
            logger.error("修改角色信息失败，角色编码为空");
            throw new RoleException("保存角色信息失败，角色编码为空");
        }
        try {
            // 查询角色信息，校验数据
            Role oldRole = this.getRoleById(role.getId());
            // 校验版本号
            if(!oldRole.getVersion().equals(role.getVersion())) {
                logger.error("修改角色信息失败，版本号不一致");
                throw new RoleException("保存角色信息失败，角色已被修改，请重新刷新页面重试");
            }
            // 校验账号，角色编码不可修改
            if(!role.getCode().equals(oldRole.getCode())) {
                logger.error("保存角色信息失败，角色编码不一致");
                throw new RoleException("保存角色信息失败，参数错误");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(role, UserInfo.getInstance());
            int add = this.roleDao.updateRole(role);
            if(add==1) {
                role = this.getRoleById(role.getId());
            } else {
                logger.error("修改角色信息失败，返回数量为0");
                throw new RoleException("保存角色信息失败，请联系管理员");
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("修改角色信息异常，参数：" + role, e);
            throw new RoleException("保存角色信息异常，请联系管理员");
        }
        return role;
    }

    /**
     * 校验角色编码是否存在
     * @param roleCode  角色编码
     * @return
     */
    private boolean checkRoleCodeExists(String roleCode) {
        return this.roleDao.checkRoleCodeExists(roleCode)>0;
    }

    /**
     * 刪除角色
     * @param roles  角色信息
     * @return
     */
    public void batchDeleteRole(List<Role> roles) throws RoleException {
        if(roles==null || roles.size()<=0) {
            logger.error("删除角色失败，参数为空");
            throw new RoleException("删除角色失败，参数为空");
        }
        try {
            for(Role role : roles) {
                this.deleteRole(role);
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除角色异常，参数：" + roles, e);
            throw new RoleException("删除角色异常，请联系管理员");
        }
    }

    /**
     * 刪除角色信息
     * @param role  角色信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Role role) throws RoleException {
        if(role==null) {
            logger.error("删除角色失败，参数为空");
            throw new RoleException("删除角色失败，参数为空");
        }
        if(role.getId()==null || role.getId()<=0) {
            logger.error("删除角色失败，角色ID为空");
            throw new RoleException("删除角色失败，角色ID为空");
        }
        if(role.getVersion()==null || role.getVersion()<0) {
            logger.error("删除角色失败，版本号为空");
            throw new RoleException("删除角色失败，参数错误");
        }
        try {
            // 删除角色信息
            int delete = this.roleDao.deleteRole(role);
            if(delete==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(role.getId(),
                        OperationLogBusinessTypeEnum.ROLE.getKey(),
                        OperationLogOperationTypeEnum.DELETE.getKey(),
                        UserInfo.getInstance().getUserName() + "删除了角色：" + role.getName() + "（" + role.getCode() + "）",
                        UserInfo.getInstance()));
                // 删除角色关系
                this.roleResourceService.removeAllRoleResource(role.getId());
            } else {
                logger.error("删除角色失败，删除数量返回为0，参数：" + role);
                throw new RoleException("删除角色失败，角色已被修改，请重新刷新页面重试");
            }
        } catch (RoleException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除角色管理异常，参数：" + role, e);
            throw new RoleException("删除角色管理异常，请联系管理员");
        }
    }
}

package com.yytx.maintenance.maintenance.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.service.OperationLogService;
import com.yytx.maintenance.constant.OperationLogBusinessTypeEnum;
import com.yytx.maintenance.constant.OperationLogOperationTypeEnum;
import com.yytx.maintenance.context.UserInfo;
import com.yytx.maintenance.excepion.TenantManagerException;
import com.yytx.maintenance.interfaces.service.CallCenterAbilityService;
import com.yytx.maintenance.maintenance.dao.TenantManagerDao;
import com.yytx.maintenance.maintenance.entity.TelephoneNumber;
import com.yytx.maintenance.maintenance.entity.Tenant;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.CommUtils;
import com.yytx.maintenance.utils.InitializeObjectUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantManagerService {
    private Logger logger = LoggerFactory.getLogger(TenantManagerService.class);
    @Autowired
    private TenantManagerDao tenantManagerDao;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private CallCenterAbilityService callCenterAbilityService;

    /**
     * 查询租户分页数据
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 租户分页数据
     * @throws TenantManagerException 自定义异常
     */
    public PageInfo<Tenant> queryPage(SearchParams searchParams) throws TenantManagerException {
        if(searchParams==null || searchParams.getSearchMap()==null) {
            logger.error("查询租户失败，参数为空");
            throw new TenantManagerException("查询租户失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询租户失败，参数错误，pageNum为空");
            throw new TenantManagerException("查询租户失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询租户失败，参数错误，pageSize为空");
            throw new TenantManagerException("查询租户失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<Tenant> page = tenantManagerDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询租户分页异常", e);
            throw new TenantManagerException("查询租户分页异常，请联系管理员");
        }
    }

    /**
     * 查询租户号码分页数据
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 租户分页数据
     * @throws TenantManagerException 自定义异常
     */
    public PageInfo<TelephoneNumber> queryTelephoneNumberPage(SearchParams searchParams) throws TenantManagerException {
        if(searchParams==null || searchParams.getSearchMap()==null) {
            logger.error("查询租户号码失败，参数为空");
            throw new TenantManagerException("查询租户号码失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询租户号码失败，参数错误，pageNum为空");
            throw new TenantManagerException("查询租户号码失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询租户号码失败，参数错误，pageSize为空");
            throw new TenantManagerException("查询租户号码失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<TelephoneNumber> page = tenantManagerDao.queryTelephoneNumberPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询租户号码分页异常", e);
            throw new TenantManagerException("查询租户号码分页异常，请联系管理员");
        }
    }

    /**
     * 根据ID查询号码信息
     * @param id
     * @return
     * @throws TenantManagerException
     */
    public TelephoneNumber selectTelephoneNumberById(Long id) throws TenantManagerException {
        if(id==null || id<=0) {
            logger.error("根据ID查询号码信息失败，id为空");
            throw new TenantManagerException("查询号码信息失败，id为空");
        }
        try {
            return this.tenantManagerDao.selectTelephoneNumberById(id);
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("查询号码信息异常，请联系管理员，id=" + id, e);
            throw new TenantManagerException("查询号码信息异常，请联系管理员");
        }
    }

    /**
     * 新增租户号码
     * @param telephoneNumber
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TelephoneNumber addTenantTelephoneNumber(TelephoneNumber telephoneNumber) throws TenantManagerException {
        try {
            // 校验参数和号码是否已经存在
            this.checkTelephoneNumber(telephoneNumber);
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(telephoneNumber, UserInfo.getInstance());
            // 新增数据
            int add = this.tenantManagerDao.addTenantTelephoneNumber(telephoneNumber);
            if(add==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(telephoneNumber.getId(),
                        OperationLogBusinessTypeEnum.TENANT_TELEPHONE_NUMBER.getKey(),
                        OperationLogOperationTypeEnum.ADD.getKey(),
                        UserInfo.getInstance().getUser().getName() + "新增了号码：" + telephoneNumber.getTelephoneNum(),
                        UserInfo.getInstance()));
                // 同步呼叫中心，并返回数据
                return this.addCallCenterTenantTelephonNumber(telephoneNumber);
            } else {
                logger.error("保存租户号码失败，保存数据，返回数量为0，参数：" + telephoneNumber);
                throw new TenantManagerException("保存租户号码失败，请联系管理员");
            }
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("保存租户号码异常，参数：" + telephoneNumber, e);
            throw new TenantManagerException("保存租户号码异常，请联系管理员");
        }
    }

    /**
     * 更新租户号码
     * @param telephoneNumber
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TelephoneNumber updateTenantTelephoneNumber(TelephoneNumber telephoneNumber) throws TenantManagerException {
        try {
            // 校验参数和号码是否已经存在
            this.checkTelephoneNumber(telephoneNumber);
            if(telephoneNumber.getId()==null || telephoneNumber.getId()<=0) {
                logger.error("保存号码信息失败，ID为空");
                throw new TenantManagerException("保存号码信息失败，ID为空");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(telephoneNumber, UserInfo.getInstance());
            // 更新数据
            int update = this.tenantManagerDao.updateTenantTelephoneNumber(telephoneNumber);
            if(update==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(telephoneNumber.getId(),
                        OperationLogBusinessTypeEnum.TENANT_TELEPHONE_NUMBER.getKey(),
                        OperationLogOperationTypeEnum.UPDATE.getKey(),
                        UserInfo.getInstance().getUser().getName() + "更新了号码：" + telephoneNumber.getTelephoneNum(),
                        UserInfo.getInstance()));
                // 同步呼叫中心，并返回数据
                return this.addCallCenterTenantTelephonNumber(telephoneNumber);
            } else {
                logger.error("保存租户号码失败，保存数据，返回数量为0，参数：" + telephoneNumber);
                throw new TenantManagerException("保存租户号码失败，请联系管理员");
            }
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("保存租户号码异常，参数：" + telephoneNumber, e);
            throw new TenantManagerException("保存租户号码异常，请联系管理员");
        }
    }

    /**
     * 校验参数是否正确
     * @param telephoneNumber
     * @return
     */
    private void checkTelephoneNumber(TelephoneNumber telephoneNumber) {
        if(telephoneNumber==null) {
            logger.error("保存租户号码失败，参数为空");
            throw new TenantManagerException("保存租户号码失败，参数为空");
        }
        if(StringUtils.isEmpty(telephoneNumber.getTenantId())) {
            logger.error("保存租户号码失败，租户ID为空");
            throw new TenantManagerException("保存租户号码失败，租户ID为空");
        }
        if(StringUtils.isEmpty(telephoneNumber.getTelephoneNum())) {
            logger.error("保存租户号码失败，号码为空");
            throw new TenantManagerException("保存租户号码失败，号码为空");
        }
        if(StringUtils.isEmpty(telephoneNumber.getTelephoneNum())) {
            logger.error("保存租户号码失败，号码为空");
            throw new TenantManagerException("保存租户号码失败，号码为空");
        }
        if(!CommUtils.isMobileOrPhone(telephoneNumber.getTelephoneNum())) {
            logger.error("保存租户号码失败，号码格式错误，telephoneNum" + telephoneNumber.getTelephoneNum());
            throw new TenantManagerException("保存租户号码失败，号码格式错误");
        }
        if(StringUtils.isEmpty(telephoneNumber.getType())) {
            logger.error("保存租户号码失败，线路类型为空");
            throw new TenantManagerException("保存租户号码失败，线路类型为空");
        }
        if(StringUtils.isEmpty(telephoneNumber.getLine())) {
            logger.error("保存租户号码失败，所属线路为空");
            throw new TenantManagerException("保存租户号码失败，所属线路为空");
        }
        // 校验是否重复
        if(this.checkTelephoneNumberExists(telephoneNumber)) {
            logger.error("保存租户号码失败，该租户下号码已经存在，参数：" + telephoneNumber);
            throw new TenantManagerException("保存租户号码失败，该租户下号码已经存在");
        }
    }

    /**
     * 校验租户号码是否重复
     * 如果号码信息存在主键ID，那么在校验时，就会排除这条数据来进行校验
     * @param telephoneNumber  号码信息
     * @return
     */
    private boolean checkTelephoneNumberExists(TelephoneNumber telephoneNumber) {
        return this.tenantManagerDao.checkTelephoneNumberExists(telephoneNumber)>0;
    }

    /**
     * 同步租户号码给呼叫中心
     * @param telephoneNumber
     * @return
     */
    private TelephoneNumber addCallCenterTenantTelephonNumber(TelephoneNumber telephoneNumber) {
        try {
            // 同步呼叫中心
            JSONObject jsonObject = callCenterAbilityService.addLineInfo(telephoneNumber);
            if (jsonObject != null) {
                if (jsonObject.get("success").equals("success")) {
                    // 返回数据
                    return this.selectTelephoneNumberById(telephoneNumber.getId());
                } else if(jsonObject.get("success").equals("fail")) {
                    logger.error("同步呼叫中心失败，" + jsonObject.get("retMsg"));
                    throw new TenantManagerException("保存租户号码失败，请联系管理员");
                } else {
                    logger.error("同步呼叫中心失败，调用接口失败，retMsg=" + jsonObject.get("retMsg"));
                    if (Integer.parseInt(jsonObject.get("success").toString()) > 100900) {
                        throw new TenantManagerException("保存租户号码失败，" + jsonObject.get("retMsg"));
                    } else {
                        throw new TenantManagerException("保存租户号码失败，请联系管理员");
                    }
                }
            } else {
                logger.error("同步呼叫中心失败，返回对象为空");
                throw new TenantManagerException("保存租户号码失败，请联系管理员");
            }
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("同步租户号码给呼叫中心异常，参数：" + telephoneNumber, e);
            throw new TenantManagerException("保存号码信息异常，请联系管理员");
        }
    }

    /**
     * 批量删除号码信息
     * @throws TenantManagerException
     */
    public void batchDeleteTenantTelephoneNumber(List<TelephoneNumber> telephoneNumberList) throws TenantManagerException {
        try {
            if(telephoneNumberList!=null && telephoneNumberList.size()>0) {
                for(TelephoneNumber telephoneNumber : telephoneNumberList) {
                    this.deleteTenantTelephoneNumber(telephoneNumber);
                }
            }
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("批量删除租户号码异常，参数：" + telephoneNumberList, e);
            throw new TenantManagerException("删除租户号码异常，请联系管理员");
        }
    }

    /**
     * 删除租户号码
     * @param telephoneNumber
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTenantTelephoneNumber(TelephoneNumber telephoneNumber) throws TenantManagerException {
        try {
            // 校验ID是否存在
            if(telephoneNumber.getId()==null || telephoneNumber.getId()<=0) {
                logger.error("保存号码信息失败，ID为空");
                throw new TenantManagerException("保存号码信息失败，ID为空");
            }
            // 设置创建信息和修改信息
            InitializeObjectUtil.getInstance().initializeModifyInfo(telephoneNumber, UserInfo.getInstance());
            // 删除数据
            int delete = this.tenantManagerDao.deleteTenantTelephoneNumber(telephoneNumber);
            if(delete==1) {
                // 添加操作日志
                operationLogService.save(new OperationLog(telephoneNumber.getId(),
                        OperationLogBusinessTypeEnum.TENANT_TELEPHONE_NUMBER.getKey(),
                        OperationLogOperationTypeEnum.DELETE.getKey(),
                        UserInfo.getInstance().getUser().getName() + "删除了号码：" + telephoneNumber.getTelephoneNum(),
                        UserInfo.getInstance()));
                // 同步呼叫中心
                JSONObject jsonObject = this.callCenterAbilityService.removeLineInfo(telephoneNumber);
                if (jsonObject != null) {
                    if (!jsonObject.get("success").equals("success")) {
                        if(jsonObject.get("success").equals("fail")) {
                            logger.error("同步呼叫中心失败，" + jsonObject.get("retMsg"));
                            throw new TenantManagerException("删除租户号码失败，请联系管理员");
                        } else {
                            logger.error("同步呼叫中心失败，调用接口失败，retMsg=" + jsonObject.get("retMsg"));
                            if (Integer.parseInt(jsonObject.get("success").toString()) > 100900) {
                                throw new TenantManagerException("删除租户号码失败，" + jsonObject.get("retMsg"));
                            } else {
                                throw new TenantManagerException("删除租户号码失败，请联系管理员");
                            }
                        }
                    }
                } else {
                    logger.error("同步呼叫中心失败，返回对象为空");
                    throw new TenantManagerException("删除租户号码失败，请联系管理员");
                }
            } else {
                logger.error("保存租户号码失败，保存数据，返回数量为0，参数：" + telephoneNumber);
                throw new TenantManagerException("保存租户号码失败，请联系管理员");
            }
        } catch (TenantManagerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除租户号码异常，参数：" + telephoneNumber, e);
            throw new TenantManagerException("删除租户号码异常，请联系管理员");
        }
    }
}

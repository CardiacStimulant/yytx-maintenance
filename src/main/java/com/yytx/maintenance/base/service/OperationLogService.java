package com.yytx.maintenance.base.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.dao.OperationLogDao;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.excepion.OperationLogException;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {
    private Logger logger = LoggerFactory.getLogger(OperationLogService.class);
    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * 查询操作记录分页数据
     * @param searchParams 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 操作记录分页数据
     * @throws OperationLogException 自定义异常
     */
    public PageInfo<OperationLog> queryPage(SearchParams searchParams) throws OperationLogException{
        if(searchParams==null || searchParams.getSearchMap()==null) {
            logger.error("查询操作记录失败，参数为空");
            throw new OperationLogException("查询操作记录失败，参数错误");
        }
        Integer pageNum = MapUtils.getInteger(searchParams.getSearchMap(), "pageNum");
        Integer pageSize = MapUtils.getInteger(searchParams.getSearchMap(), "pageSize");
        if(pageNum==null || pageNum<=0) {
            logger.error("查询操作记录失败，参数错误，pageNum为空");
            throw new OperationLogException("查询操作记录失败，参数错误");
        }
        if(pageSize==null || pageSize<=0) {
            logger.error("查询操作记录失败，参数错误，pageSize为空");
            throw new OperationLogException("查询操作记录失败，参数错误");
        }
        try {
            PageHelper.startPage(pageNum, pageSize);
            Page<OperationLog> page = operationLogDao.queryPage(searchParams);
            return page.toPageInfo();
        } catch (Exception e) {
            logger.error("查询操作记录分页异常", e);
            throw new OperationLogException("查询操作记录分页异常，请联系管理员");
        }
    }

    public void save(OperationLog operationLog) {
        operationLogDao.saveOperationLog(operationLog);
    }

    public void saveByThread(final OperationLog operationLog) {
        new Thread(() -> save(operationLog)).start();
    }
}

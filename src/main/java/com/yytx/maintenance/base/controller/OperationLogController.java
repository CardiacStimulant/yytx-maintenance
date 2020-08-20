package com.yytx.maintenance.base.controller;

import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.service.OperationLogService;
import com.yytx.maintenance.excepion.OperationLogException;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import com.yytx.maintenance.utils.SearchParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("log")
public class OperationLogController {
    private Logger logger = LoggerFactory.getLogger(OperationLogController.class);
    @Autowired
    private OperationLogService logService;

    /**
     * 查询操作记录分页数据
     * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
     * @return 操作记录分页数据
     */
    @RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryPage(@RequestParam Map<String, Object> searchMap) {
        Result<PageInfo<OperationLog>> result;
        try {
            SearchParams searchParams = new SearchParams();
            searchParams.setSearchMap(searchMap);
            // 创建时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
            // 更新时间的查询条件
            SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
            PageInfo<OperationLog> pageInfo = this.logService.queryPage(searchParams);
            result = new ResultUtil<PageInfo<OperationLog>>().setData(pageInfo);
        } catch (OperationLogException e) {
            result = new ResultUtil<PageInfo<OperationLog>>().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("查询操作记录分页数据异常", e);
            result = new ResultUtil<PageInfo<OperationLog>>().setErrorMsg("查询操作记录分页异常，请联系管理员");
        }
        return result;
    }
}

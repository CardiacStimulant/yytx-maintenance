package com.yytx.maintenance.base.controller;

import com.github.pagehelper.Page;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.base.service.OperationLogService;
import com.yytx.maintenance.pojo.SearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class OperationLogController {
    @Autowired
    private OperationLogService logService;

    @RequestMapping(value = "/queryPage")
    public Object queryPage(SearchParams searchParams) {
        // 查询数据
        Page<OperationLog> page = this.logService.queryPage(searchParams);
        return page;
    }
}

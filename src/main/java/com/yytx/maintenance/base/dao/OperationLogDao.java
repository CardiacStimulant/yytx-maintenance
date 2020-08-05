package com.yytx.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.yytx.maintenance.base.entity.OperationLog;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OperationLogDao {
    Page<OperationLog> queryPage(@Param("condition") SearchParams searchParams);

    //  保存服务记录
    int saveOperationLog(OperationLog operationLog);
}

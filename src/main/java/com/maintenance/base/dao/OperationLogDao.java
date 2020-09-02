package com.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.maintenance.base.entity.OperationLog;
import com.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OperationLogDao {
    Page<OperationLog> queryPage(@Param("condition") SearchParams searchParams);

    //  保存服务记录
    int saveOperationLog(OperationLog operationLog);
}

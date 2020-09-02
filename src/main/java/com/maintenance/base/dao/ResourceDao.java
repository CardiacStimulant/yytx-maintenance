package com.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.maintenance.base.entity.Resource;
import com.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResourceDao {
    Page<Resource> queryPage(@Param("condition") SearchParams searchParams);

    List<Resource> queryList(@Param("condition") SearchParams searchParams);

    Resource getResourceById(@Param("id") Long id);

    int addResource(Resource resource);

    int updateResource(Resource resource);

    int checkResourceKeyExists(Resource resource);

    int deleteResource(Resource role);
}

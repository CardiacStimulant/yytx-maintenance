package com.yytx.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceManagerDao {
    Page<Resource> queryPage(@Param("condition") SearchParams searchParams);

    List<Resource> queryList(@Param("condition") SearchParams searchParams);

    Resource getResourceById(@Param("id") Long id);

    int addResource(Resource resource);
//
    int updateResource(Resource resource);
//
    int checkResourceKeyExists(Resource resource);
//
//    int addResourceResourceRelation(ResourceResource roleResource);

    int deleteResource(Resource role);

    int deleteRoleResourceByResourceId(@Param("resourceId") Long resourceId);

    int deleteRoleResourceByRoleId(@Param("roleId") Long roleId);
//
//    List<UserResource> queryUserResourceList(@Param("userId") Long userId);


//
//    int deleteUserResourceByUserIdAndResourceIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
//

}

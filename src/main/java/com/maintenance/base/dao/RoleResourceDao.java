package com.maintenance.base.dao;

import com.maintenance.base.entity.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleResourceDao {
    /**
     * 添加角色-资源关系
     * @param roleResource
     * @return
     */
    int addRoleResource(RoleResource roleResource);

    /**
     * 校验角色-资源关系是否存在
     * @param roleResource
     * @return
     */
    int checkRoleResourceExists(RoleResource roleResource);

    /**
     * 移除角色-资源关系
     * @param roleResource
     * @return
     */
    int removeRoleResource(RoleResource roleResource);

    /**
     * 根据资源ID删除角色-资源关系
     * @param resourceId
     * @return
     */
    int removeRoleResourceByResourceId(@Param("resourceId") Long resourceId);
}

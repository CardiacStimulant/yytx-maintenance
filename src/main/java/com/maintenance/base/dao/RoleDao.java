package com.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.maintenance.base.entity.Role;
import com.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleDao {
    Page<Role> queryPage(@Param("condition") SearchParams searchParams);

    List<Role> queryList(@Param("condition") SearchParams searchParams);

    Role getRoleById(@Param("id") Long id);

    int addRole(Role role);

    int updateRole(Role role);

    int checkRoleCodeExists(@Param("code") String code);

    int deleteRole(Role role);
}

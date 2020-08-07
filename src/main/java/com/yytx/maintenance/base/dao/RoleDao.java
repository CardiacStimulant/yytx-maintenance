package com.yytx.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao {
    Page<Role> queryPage(@Param("condition") SearchParams searchParams);

    List<Role> queryList(@Param("condition") SearchParams searchParams);

    Role getRoleById(@Param("id") Long id);

    int addRole(Role role);

    int updateRole(Role role);

    int checkRoleCodeExists(@Param("code") String code);

    int deleteRole(Role role);
}

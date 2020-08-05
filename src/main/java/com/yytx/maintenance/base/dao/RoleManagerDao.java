package com.yytx.maintenance.base.dao;

import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleManagerDao {
//    Page<UserRoleVo> queryPage(@Param("condition") SearchParams searchParams);
    List<Role> queryList(@Param("condition") SearchParams searchParams);
//
//    User getUserById(@Param("userId") Long userId);
//
//    int addUser(User user);
//
//    int checkUserLoginAccountExists(@Param("loginAccount") String loginAccount);
//
//    int addUserRoleRelation(UserRole userRole);
//
//    int deleteUser(User user);
//
//    List<UserRole> queryUserRoleList(@Param("userId") Long userId);
//
//    int deleteUserRoleByUserId(@Param("userId") Long userId);
//
//    int deleteUserRoleByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
//
//    int updateUser(User user);
}

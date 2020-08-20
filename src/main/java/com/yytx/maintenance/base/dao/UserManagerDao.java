package com.yytx.maintenance.base.dao;

import com.github.pagehelper.Page;
import com.yytx.maintenance.base.entity.User;
import com.yytx.maintenance.base.entity.UserRole;
import com.yytx.maintenance.base.vo.UserRoleVo;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserManagerDao{
    User getUserByLoginAccount(@Param("loginAccount") String loginAccount);

    Page<UserRoleVo> queryPage(@Param("condition") SearchParams searchParams);

    User getUserById(@Param("userId") Long userId);

    User getUserByAccount(@Param("account") String account);

    int addUser(User user);

    int checkUserLoginAccountExists(@Param("loginAccount") String loginAccount);

    int addUserRoleRelation(UserRole userRole);

    int deleteUser(User user);

    List<UserRole> queryUserRoleList(@Param("userId") Long userId);

    int deleteUserRoleByUserId(@Param("userId") Long userId);

    int deleteUserRoleByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    int updateUser(User user);

    int updatePassword(@Param("id") Long id, @Param("version") Integer version, @Param("password") String password);
}

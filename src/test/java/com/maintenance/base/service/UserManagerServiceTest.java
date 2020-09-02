package com.maintenance.base.service;

import com.github.pagehelper.PageInfo;
import com.maintenance.MaintenanceApplicationTests;
import com.maintenance.base.entity.Role;
import com.maintenance.base.vo.UserRoleVo;
import com.maintenance.pojo.SearchParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

class UserManagerServiceTest extends MaintenanceApplicationTests {

    @Autowired
    private UserManagerService userManagerService;

    @Test
    void queryPageTest() {
        SearchParams searchParams = new SearchParams();
        searchParams.addCondition("pageNum", 1);
        searchParams.addCondition("pageSize", 10);
        searchParams.addCondition("loginAccount", "admin");
        searchParams.addCondition("jobNumber", "1");
        searchParams.addCondition("name", "管理");
        searchParams.addCondition("comments", "管理");
        searchParams.addCondition("roleName", "测试");
        PageInfo<UserRoleVo> pageInfo = userManagerService.queryPage(searchParams);
        for(UserRoleVo userRoleVo : pageInfo.getList()) {
            System.out.println(userRoleVo);
            System.out.println(userRoleVo.getRoleList()==null ? null : userRoleVo.getRoleList().size());
        }
    }

    @Test
    void addUserManager() {
//        System.out.println(Encrypt.md5Encrypt("123456"));
//        System.out.println(Encrypt.md5Encrypt("123456").length());
//        System.out.println(Encrypt.md5Encrypt("1234567890"));
//        System.out.println(Encrypt.md5Encrypt("1234567890").length());
//        System.out.println(Encrypt.md5Encrypt("123456").equals("4QrcOUm6Wau+VuBX8g+IPg=="));
//        System.out.println(Encrypt.md5Encrypt("1234567890").equals("6Afx/PgtEy+bsBjKZzihnw=="));
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setName("junit测试数据");
        userRoleVo.setLoginAccount("junit");
        userRoleVo.setComments("junit测试生成数据");
        userRoleVo.setEmail("1404014558@qq.com");
        userRoleVo.setMobile("13312341234");
        userRoleVo.setJobNumber("0004");
        /* 添加角色关联信息 */
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        role.setName("管理员");
        roles.add(role);
        role = new Role();
        role.setId(2L);
        role.setName("测试管理员");
        roles.add(role);
        userRoleVo.setRoleList(roles);
        this.userManagerService.addUserManager(userRoleVo);
    }

    @Test
    void updateUserManager() {
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setId(11L);
        userRoleVo.setPassword("4QrcOUm6Wau+VuBX8g+IPg==");
        userRoleVo.setName("junit测试数据");
        userRoleVo.setLoginAccount("junit");
        userRoleVo.setComments("junit测试生成数据");
        userRoleVo.setEmail("1404014558@qq.com");
        userRoleVo.setMobile("13312341234");
        userRoleVo.setJobNumber("0004");
        userRoleVo.setVersion(3);
        /* 添加角色关联信息 */
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(2L);
        role.setName("测试管理员");
        roles.add(role);
        role = new Role();
        role.setId(3L);
        role.setName("开发管理员");
        roles.add(role);
        userRoleVo.setRoleList(roles);
        this.userManagerService.updateUserManager(userRoleVo);
    }

    @Test
    void deleteUserManager() {
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setId(11L);
        userRoleVo.setName("junit测试数据");
        userRoleVo.setLoginAccount("junit");
        userRoleVo.setComments("junit测试生成数据（测试修改）");
        userRoleVo.setEmail("1404014558@qq.com");
        userRoleVo.setMobile("13312341234");
        userRoleVo.setJobNumber("0004");
        userRoleVo.setVersion(0);
        this.userManagerService.deleteUserManager(userRoleVo);
    }
}
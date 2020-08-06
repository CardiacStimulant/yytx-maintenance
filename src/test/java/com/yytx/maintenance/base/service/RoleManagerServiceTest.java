package com.yytx.maintenance.base.service;

import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.YytxMaintenanceApplicationTests;
import com.yytx.maintenance.base.entity.Resource;
import com.yytx.maintenance.base.entity.Role;
import com.yytx.maintenance.base.vo.RoleResourceVo;
import com.yytx.maintenance.pojo.SearchParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

class RoleManagerServiceTest extends YytxMaintenanceApplicationTests {
    @Autowired
    private RoleManagerService roleManagerService;

    @Test
    void queryPage() {
        SearchParams searchParams = new SearchParams();
        searchParams.addCondition("pageNum", 1);
        searchParams.addCondition("pageSize", 10);
        searchParams.addCondition("name", "管理");
        searchParams.addCondition("code", "admin");
        searchParams.addCondition("comments", "管理");
        PageInfo<Role> pageInfo = roleManagerService.queryPage(searchParams);
        for(Role role : pageInfo.getList()) {
            System.out.println(role);
        }
    }

    @Test
    void addUserManager() {
        RoleResourceVo roleResourceVo = new RoleResourceVo();
        roleResourceVo.setName("junit测试数据");
        roleResourceVo.setCode("junit");
        roleResourceVo.setComments("junit测试生成数据");
        /* 添加角色关联信息 */
        List<Resource> resources = new ArrayList<>();
        Resource resource = new Resource();
        resource.setId(1L);
        resource.setName("用户管理");
        resource.setType("menu");
        resources.add(resource);
        resource = new Resource();
        resource.setId(2L);
        resource.setName("新增");
        resource.setType("button");
        resources.add(resource);
        roleResourceVo.setResourceList(resources);
        this.roleManagerService.addRoleManager(roleResourceVo);
    }
}
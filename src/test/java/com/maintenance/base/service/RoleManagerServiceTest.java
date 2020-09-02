package com.maintenance.base.service;

import com.github.pagehelper.PageInfo;
import com.maintenance.MaintenanceApplicationTests;
import com.maintenance.base.entity.Role;
import com.maintenance.pojo.SearchParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoleManagerServiceTest extends MaintenanceApplicationTests {
    @Autowired
    private RoleService roleManagerService;

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
}
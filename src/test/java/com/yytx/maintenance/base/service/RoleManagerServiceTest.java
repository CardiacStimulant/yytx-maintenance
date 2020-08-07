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
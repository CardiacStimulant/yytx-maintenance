package com.yytx.maintenance.interfaces.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yytx.maintenance.YytxMaintenanceApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CallCenterAbilityServiceTest extends YytxMaintenanceApplicationTests {
    @Autowired
    private CallCenterAbilityService callCenterAbilityService;

    @Test
    void queryLineServerInfo() {
        try {
            int pageNum = 2;
            int pageSize = 10;
            JSONObject jsonObject = this.callCenterAbilityService.queryLineServer();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            jsonArray.addAll(jsonArray);
            jsonArray.addAll(jsonArray);
            jsonArray.addAll(jsonArray);
            int pages = (jsonArray.size() - 1) / pageSize + 1;
            System.out.println(pages);
            if(pageNum==pages) {
                jsonObject.put("list", jsonArray.subList((pageNum-1) * pageSize, jsonArray.size()));
            } else if(pageNum>pages) {
                jsonObject.put("list", new ArrayList<>());
            } else {
                jsonObject.put("list", jsonArray.subList((pageNum-1) * pageSize, pageNum * pageSize));
            }
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
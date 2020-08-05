package com.yytx.maintenance.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询条件多时可以使用这个对象进行封装
 */
public class SearchParams {
    private Map<String, Object> searchMap;

    public SearchParams() {
        searchMap = new HashMap();
    }

    public Map<String, Object> getSearchMap() {
        return this.searchMap;
    }

    public void setSearchMap(Map<String, Object> searchMap) {
        this.searchMap = searchMap;
    }

    public void addCondition(String key, Object value) {
        this.searchMap.put(key, value);
    }

    public Object removeCondition(String key) {
        return this.searchMap.remove(key);
    }
}

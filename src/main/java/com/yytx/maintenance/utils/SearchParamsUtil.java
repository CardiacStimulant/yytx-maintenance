package com.yytx.maintenance.utils;

import com.yytx.maintenance.pojo.SearchParams;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 查询条件工具类
 * 将时间范围字符串解析为数组
 */
public class SearchParamsUtil {
    /**
     * 解析时间范围字符串为数组
     * @param searchParams  查询条件集合
     * @param key   待解析的key
     * @param conditionKey  condition的key
     * @param beginTimeFormat   开始时间格式化
     * @param endTimeFormat 结束时间格式化
     */
    public static void parseTimeGroup(SearchParams searchParams, String key, String conditionKey, String beginTimeFormat, String endTimeFormat) {
        if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(conditionKey)) {
            if(StringUtils.isEmpty(beginTimeFormat)) {
                beginTimeFormat = "yyyy-MM-dd 00:00:00";
            }
            if(StringUtils.isEmpty(endTimeFormat)) {
                endTimeFormat = "yyyy-MM-dd 23:59:59";
            }
            String timeGroupStr = MapUtils.getString(searchParams.getSearchMap(), key);
            List<String> timeList = null;
            if(StringUtils.isNotBlank(timeGroupStr)){
                timeList =  Arrays.asList(timeGroupStr.split(","));

                if (timeList.size() == 2) {
                    String beginTime = timeList.get(0);
                    String formatBeginTime = DateUtil.formatGMTTime(beginTime, beginTimeFormat);
                    timeList.set(0, formatBeginTime);
                    String endTime = timeList.get(1);
                    String formatEndTime = DateUtil.formatGMTTime(endTime, endTimeFormat);
                    timeList.set(1, formatEndTime);
                }
            }
            searchParams.addCondition(conditionKey, timeList);
        }
    }
}

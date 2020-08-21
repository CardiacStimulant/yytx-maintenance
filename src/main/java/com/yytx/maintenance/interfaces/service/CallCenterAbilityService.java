package com.yytx.maintenance.interfaces.service;

import com.alibaba.fastjson.JSONObject;
import com.yytx.maintenance.maintenance.entity.TelephoneNumber;
import com.yytx.maintenance.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CallCenterAbilityService {
    private final static Logger logger = LoggerFactory.getLogger(CallCenterAbilityService.class);

    //支撑平台Ip地址
    @Value("${callcenter.ability.url}")
    private String callCenterAbilityUrl;

    /**
     * 呼叫中心能力接口调用
     *
     * @param requestUrl    请求URL
     * @param requestParams 请求参数
     * @return JSONObject
     * @throws Exception 调用接口异常
     */
    private JSONObject startInterfaceRunning(String requestUrl, String requestParams) throws Exception {
        logger.info("呼叫中心能力接口调用，请求url：" + requestUrl + "，请求参数：" + requestParams );
        String result = HttpRequestUtil.postJSONData(requestUrl, requestParams);
        logger.info("呼叫中心能力接口调用，请求url：" + requestUrl + "，请求参数：" + requestParams + "，接口返回结果：" + result);
        return JSONObject.parseObject(result);
    }

    /**
     * 获取线路服务器接口
     *
     */
    public JSONObject queryLineServer() throws Exception {
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/queryLineServerInfo", new JSONObject().toString());
    }

    /**
     * 新增线路号码
     * @param telephoneNumber
     * @return
     * @throws Exception
     */
    public JSONObject addLineInfo(TelephoneNumber telephoneNumber) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("tenantId", telephoneNumber.getTenantId());
        reqJsonObject.put("callNumber", telephoneNumber.getTelephoneNum());
        reqJsonObject.put("serverId", telephoneNumber.getLine());
        reqJsonObject.put("callPrefix", telephoneNumber.getPrefix());
        reqJsonObject.put("callMiddle", telephoneNumber.getInfix());
        reqJsonObject.put("lineType", telephoneNumber.getType());
        reqJsonObject.put("areaCode", telephoneNumber.getAreaCode());
        reqJsonObject.put("lineName", telephoneNumber.getName());
        reqJsonObject.put("callLimitCount", telephoneNumber.getConcurrencyNumber());
        reqJsonObject.put("lineNumber", telephoneNumber.getRegistrationNumber());
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/addLineInfo", reqJsonObject.toString());
    }

    /**
     * 删除线路号码
     * @param telephoneNumber
     * @return
     * @throws Exception
     */
    public JSONObject removeLineInfo(TelephoneNumber telephoneNumber) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("tenantId", telephoneNumber.getTenantId());
        reqJsonObject.put("callNumber", telephoneNumber.getTelephoneNum());
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/removeLineInfo", reqJsonObject.toString());
    }

    /**
     * 查询全部线路服务信息
     * @return
     * @throws Exception
     */
    public JSONObject queryLineServerInfo() throws Exception {
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/queryLineServerInfo", new JSONObject().toString());
    }

    /**
     * 根据服务名称，查询线路服务信息
     * @return
     * @throws Exception
     */
    public JSONObject queryLineServerInfoByName(String serverName) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("serverName", serverName);
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/queryLineServerInfoByName", reqJsonObject.toString());
    }

    /**
     * 新增线路服务
     * @param serverName
     * @param ipAddress
     * @param port
     * @return
     * @throws Exception
     */
    public JSONObject addLineServerInfo(String serverName, String ipAddress, String port) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("serverName", serverName);
        reqJsonObject.put("ipAddress", ipAddress);
        reqJsonObject.put("port", port);
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/addLineServerInfo", reqJsonObject.toString());
    }

    /**
     * 更新线路服务
     * @param serverId
     * @param serverName
     * @return
     * @throws Exception
     */
    public JSONObject updateLineServerInfoById(String serverId, String serverName) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("serverId", serverId);
        reqJsonObject.put("serverName", serverName);
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/updateLineServerInfoById", reqJsonObject.toString());
    }

    /**
     * 删除线路服务
     * @param serverId
     * @return
     * @throws Exception
     */
    public JSONObject removeLineServerInfoById(String serverId) throws Exception {
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("serverId", serverId);
        // 调用接口
        return startInterfaceRunning(callCenterAbilityUrl + "/v1/system/removeLineServerInfoById", reqJsonObject.toString());
    }
}

package com.yytx.maintenance.interfaces.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.constant.OperationLogOperationTypeEnum;
import com.yytx.maintenance.excepion.CallCenterAbilityException;
import com.yytx.maintenance.excepion.TenantManagerException;
import com.yytx.maintenance.interfaces.service.CallCenterAbilityService;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import com.yytx.maintenance.utils.SearchParamsUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("call_center_ability")
public class CallCenterAbilityController {
	private Logger logger = LoggerFactory.getLogger(CallCenterAbilityController.class);
	@Autowired
	private CallCenterAbilityService callCenterAbilityService;

	/**
	 * 查询线路服务
	 * @return 租户分页数据
	 */
	@RequestMapping(value = "/queryLineServer", method = RequestMethod.GET)
	public Object queryLineServer() {
		Result<JSONObject> result;
		try {
			JSONObject jsonObject = this.callCenterAbilityService.queryLineServer();
			if (jsonObject != null) {
				if (jsonObject.get("success").equals("success")) {
					result = new ResultUtil<JSONObject>().setData(jsonObject);
				} else if(jsonObject.get("success").equals("fail")) {
					logger.error("查询线路服务失败，" + jsonObject.get("retMsg"));
					throw new CallCenterAbilityException("查询线路服务失败，请联系管理员");
				} else {
					logger.error("查询线路服务失败，调用接口失败，retMsg=" + jsonObject.get("retMsg"));
					if (Integer.parseInt(jsonObject.get("success").toString()) > 100900) {
						throw new CallCenterAbilityException("查询线路服务失败，" + jsonObject.get("retMsg"));
					} else {
						throw new CallCenterAbilityException("查询线路服务失败，请联系管理员");
					}
				}
			} else {
				logger.error("查询线路服务失败，返回对象为空");
				throw new CallCenterAbilityException("查询线路服务失败，请联系管理员");
			}
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("查询线路服务异常", e);
			result = new ResultUtil<JSONObject>().setErrorMsg("查询线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 查询线路服务分页
	 * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
	 * @return
	 */
	@RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
	public Object queryPage(@RequestParam Map<String, Object> searchMap) {
		Result<JSONObject> result;
		try {
			Integer pageNum = MapUtils.getInteger(searchMap, "pageNum");
			Integer pageSize = MapUtils.getInteger(searchMap, "pageSize");
			if(pageNum==null || pageNum<=0) {
				logger.error("查询线路服务失败，参数错误，pageNum为空");
				throw new CallCenterAbilityException("查询线路服务失败，参数错误");
			}
			if(pageSize==null || pageSize<=0) {
				logger.error("查询线路服务失败，参数错误，pageSize为空");
				throw new CallCenterAbilityException("查询线路服务失败，参数错误");
			}
			String serverName = MapUtils.getString(searchMap, "serverName");
			JSONObject jsonObject;
			if(StringUtils.isNotEmpty(serverName)) {
				jsonObject = this.callCenterAbilityService.queryLineServerInfoByName(serverName);
			} else {
				jsonObject = this.callCenterAbilityService.queryLineServerInfo();
			}
			if (jsonObject != null) {
				if (jsonObject.get("success").equals("success")) {
					// 进行手工分页
					this.generateLineServicePageInfo(jsonObject, pageNum, pageSize);
					return new ResultUtil<JSONObject>().setData(jsonObject);
				} else if(jsonObject.get("success").equals("fail")) {
					logger.error("查询线路服务失败，" + jsonObject.get("retMsg"));
					throw new CallCenterAbilityException("保存线路服务失败，请联系管理员");
				} else {
					logger.error("查询线路服务失败，调用接口失败，retMsg=" + jsonObject.get("retMsg"));
					if (Integer.parseInt(jsonObject.get("success").toString()) > 100900) {
						throw new CallCenterAbilityException("查询线路服务失败，" + jsonObject.get("retMsg"));
					} else {
						throw new CallCenterAbilityException("查询线路服务失败，请联系管理员");
					}
				}
			} else {
				logger.error("查询线路服务失败，返回对象为空");
				throw new CallCenterAbilityException("查询线路服务失败，请联系管理员");
			}
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("查询线路服务异常", e);
			result = new ResultUtil<JSONObject>().setErrorMsg("查询线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 生成线路服务分页数据
	 * @param jsonObject
	 * @param pageNum
	 * @param pageSize
	 */
	private void generateLineServicePageInfo(JSONObject jsonObject, Integer pageNum, Integer pageSize) {
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		jsonObject.put("total", jsonArray.size());
		int pages = (jsonArray.size() - 1) / pageSize + 1;
		jsonObject.put("pages", pages);
		if(pageNum.equals(pages)) {
			jsonObject.put("list", jsonArray.subList((pageNum-1) * pageSize, jsonArray.size()));
		} else if(pageNum>pages) {
			jsonObject.put("list", new ArrayList<>());
		} else {
			jsonObject.put("list", jsonArray.subList((pageNum-1) * pageSize, pageNum * pageSize));
		}
	}

	/**
	 * 新增线路服务信息
	 * @param jsonData    线路服务信息
	 * @return
	 */
	@RequestMapping(value = "/addLineService", method = RequestMethod.POST)
	public Object addLineService(@RequestBody String jsonData) {
		Result<JSONObject> result;
		try {
			if(StringUtils.isEmpty(jsonData)) {
				logger.error("保存线路服务信息失败，参数为空");
				throw new CallCenterAbilityException("保存线路服务失败，参数为空");
			}
			JSONObject json = JSONObject.parseObject(jsonData);
			// 名称
			String serverName = json.getString("serverName");
			// IP
			String ipAddress = json.getString("ipAddress");
			// 端口
			String port = json.getString("port");
			// 同步数据
			JSONObject jsonObject = this.callCenterAbilityService.addLineServerInfo(serverName, ipAddress, port);
			// 解析返回对象
			result = this.analysisLineServiceJsonObject(jsonObject, OperationLogOperationTypeEnum.ADD.getKey());
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("保存线路服务异常，参数：" + jsonData, e);
			result = new ResultUtil<JSONObject>().setErrorMsg("保存线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 修改线路服务信息
	 * @param jsonData    线路服务信息
	 * @return
	 */
	@RequestMapping(value = "/updateLineService", method = RequestMethod.POST)
	public Object updateRole(@RequestBody String jsonData) {
		Result<JSONObject> result;
		try {
			if(StringUtils.isEmpty(jsonData)) {
				logger.error("保存线路服务信息失败，参数为空");
				throw new CallCenterAbilityException("保存线路服务失败，参数为空");
			}
			JSONObject json = JSONObject.parseObject(jsonData);
			// ID
			String serverId = json.getString("serverId");
			// 名称
			String serverName = json.getString("serverName");
			// 同步数据
			JSONObject jsonObject = this.callCenterAbilityService.updateLineServerInfoById(serverId, serverName);
			// 解析返回对象
			result = this.analysisLineServiceJsonObject(jsonObject, OperationLogOperationTypeEnum.UPDATE.getKey());
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("保存线路服务异常", e);
			result = new ResultUtil<JSONObject>().setErrorMsg("保存线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 批量删除线路服务
	 * @param ids    线路服务
	 * @return
	 */
	@RequestMapping(value = "/batchDeleteLineService", method = RequestMethod.POST)
	public Object batchDeleteLineService(@RequestBody List<String> ids) {
		Result<JSONObject> result = null;
		try {
			if(ids==null || ids.size()<=0) {
				logger.error("删除线路服务信息失败，参数为空");
				throw new CallCenterAbilityException("删除线路服务信息失败，参数为空");
			}
			for (String id : ids) {
				JSONObject jsonObject = this.callCenterAbilityService.removeLineServerInfoById(id);
				// 解析返回对象
				result = this.analysisLineServiceJsonObject(jsonObject, OperationLogOperationTypeEnum.DELETE.getKey());
			}
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("删除线路服务异常", e);
			result = new ResultUtil<JSONObject>().setErrorMsg("删除线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 删除线路服务
	 * @param jsonData    线路服务信息
	 * @return
	 */
	@RequestMapping(value = "/deleteLineService", method = RequestMethod.POST)
	public Object deleteLineService(@RequestBody String jsonData) {
		Result<JSONObject> result;
		try {
			if(StringUtils.isEmpty(jsonData)) {
				logger.error("删除线路服务失败，参数为空");
				throw new CallCenterAbilityException("删除线路服务信息失败，参数为空");
			}
			JSONObject json = JSONObject.parseObject(jsonData);
			// ID
			String serverId = json.getString("serverId");
			if(StringUtils.isEmpty(serverId)) {
				logger.error("删除线路服务信息失败，服务ID为空");
				throw new CallCenterAbilityException("删除线路服务信息失败，服务ID为空");
			}
			// 同步数据
			JSONObject jsonObject = this.callCenterAbilityService.removeLineServerInfoById(serverId);
			// 解析返回对象
			result = this.analysisLineServiceJsonObject(jsonObject, OperationLogOperationTypeEnum.DELETE.getKey());
		} catch (CallCenterAbilityException e) {
			result = new ResultUtil<JSONObject>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("删除线路服务异常", e);
			result = new ResultUtil<JSONObject>().setErrorMsg("删除线路服务异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 解析线路服务返回对象
	 * @param jsonObject
	 * @return
	 */
	private Result<JSONObject> analysisLineServiceJsonObject(JSONObject jsonObject, String operationType) {
		String prefix = "保存";
		if(OperationLogOperationTypeEnum.DELETE.getKey().equals(operationType)) {
			prefix = "删除";
		}
		if (jsonObject != null) {
			if (jsonObject.get("success").equals("success")) {
				return new ResultUtil<JSONObject>().setData(jsonObject);
			} else if(jsonObject.get("success").equals("fail")) {
				logger.error(prefix + "线路服务失败，" + jsonObject.get("retMsg"));
				throw new CallCenterAbilityException("保存线路服务失败，请联系管理员");
			} else {
				logger.error(prefix + "线路服务失败，调用接口失败，retMsg=" + jsonObject.get("retMsg"));
				if (Integer.parseInt(jsonObject.get("success").toString()) > 100900) {
					throw new CallCenterAbilityException(prefix + "线路服务失败，" + jsonObject.get("retMsg"));
				} else {
					throw new CallCenterAbilityException(prefix + "线路服务失败，请联系管理员");
				}
			}
		} else {
			logger.error(prefix + "线路服务失败，返回对象为空");
			throw new CallCenterAbilityException(prefix + "线路服务失败，请联系管理员");
		}
	}
}

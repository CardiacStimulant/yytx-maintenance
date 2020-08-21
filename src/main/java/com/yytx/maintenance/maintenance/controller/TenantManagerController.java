package com.yytx.maintenance.maintenance.controller;

import com.github.pagehelper.PageInfo;
import com.yytx.maintenance.excepion.TenantManagerException;
import com.yytx.maintenance.maintenance.entity.TelephoneNumber;
import com.yytx.maintenance.maintenance.entity.Tenant;
import com.yytx.maintenance.maintenance.service.TenantManagerService;
import com.yytx.maintenance.pojo.Result;
import com.yytx.maintenance.pojo.SearchParams;
import com.yytx.maintenance.utils.ResultUtil;
import com.yytx.maintenance.utils.SearchParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tenant")
public class TenantManagerController {
	private Logger logger = LoggerFactory.getLogger(TenantManagerController.class);
	@Autowired
	private TenantManagerService tenantManagerService;

	/**
	 * 查询租户分页数据
	 * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
	 * @return 租户分页数据
	 */
	@RequestMapping(value = "/queryPage", method = {RequestMethod.POST, RequestMethod.GET})
	public Object queryPage(@RequestParam Map<String, Object> searchMap) {
		Result<PageInfo<Tenant>> result;
		try {
			SearchParams searchParams = new SearchParams();
			searchParams.setSearchMap(searchMap);
			// 创建时间的查询条件
			SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
			// 更新时间的查询条件
			SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
			PageInfo<Tenant> pageInfo = this.tenantManagerService.queryPage(searchParams);
			result = new ResultUtil<PageInfo<Tenant>>().setData(pageInfo);
		} catch (TenantManagerException e) {
			result = new ResultUtil<PageInfo<Tenant>>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("查询租户分页数据异常", e);
			result = new ResultUtil<PageInfo<Tenant>>().setErrorMsg("查询租户分页异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 查询租户号码分页数据
	 * @param searchMap 查询条件，分页条件必传，pageNum：页码，pageSize：页大小
	 * @return 租户分页数据
	 */
	@RequestMapping(value = "/queryTelephoneNumberPage", method = {RequestMethod.POST, RequestMethod.GET})
	public Object queryTelephoneNumberPage(@RequestParam Map<String, Object> searchMap) {
		Result<PageInfo<TelephoneNumber>> result;
		try {
			SearchParams searchParams = new SearchParams();
			searchParams.setSearchMap(searchMap);
			// 创建时间的查询条件
			SearchParamsUtil.parseTimeGroup(searchParams, "createTimeGroup", "createTimeList", null, null);
			// 更新时间的查询条件
			SearchParamsUtil.parseTimeGroup(searchParams, "lastModifiedGroup", "modifyTimeList", null, null);
			PageInfo<TelephoneNumber> pageInfo = this.tenantManagerService.queryTelephoneNumberPage(searchParams);
			result = new ResultUtil<PageInfo<TelephoneNumber>>().setData(pageInfo);
		} catch (TenantManagerException e) {
			result = new ResultUtil<PageInfo<TelephoneNumber>>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("查询租户号码分页数据异常", e);
			result = new ResultUtil<PageInfo<TelephoneNumber>>().setErrorMsg("查询租户号码分页异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 新增租户号码
	 * @param telephoneNumber    号码信息
	 * @return 角色信息
	 */
	@RequestMapping(value = "/addTenantTelephoneNumber", method = RequestMethod.POST)
	public Object addTenantTelephoneNumber(@RequestBody TelephoneNumber telephoneNumber) {
		Result<TelephoneNumber> result;
		try {
			telephoneNumber = this.tenantManagerService.addTenantTelephoneNumber(telephoneNumber);
			result = new ResultUtil<TelephoneNumber>().setData(telephoneNumber);
		} catch (TenantManagerException e) {
			result = new ResultUtil<TelephoneNumber>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("保存号码信息异常", e);
			result = new ResultUtil<TelephoneNumber>().setErrorMsg("保存号码信息异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 更新租户号码
	 * @param telephoneNumber    号码信息
	 * @return 角色信息
	 */
	@RequestMapping(value = "/updateTenantTelephoneNumber", method = RequestMethod.POST)
	public Object updateTenantTelephoneNumber(@RequestBody TelephoneNumber telephoneNumber) {
		Result<TelephoneNumber> result;
		try {
			telephoneNumber = this.tenantManagerService.updateTenantTelephoneNumber(telephoneNumber);
			result = new ResultUtil<TelephoneNumber>().setData(telephoneNumber);
		} catch (TenantManagerException e) {
			result = new ResultUtil<TelephoneNumber>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("保存号码信息异常", e);
			result = new ResultUtil<TelephoneNumber>().setErrorMsg("保存号码信息异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 批量删除号码信息
	 * @param telephoneNumberList    号码信息
	 * @return 角色信息
	 */
	@RequestMapping(value = "/batchDeleteTenantTelephoneNumber", method = RequestMethod.POST)
	public Object batchDeleteTenantTelephoneNumber(@RequestBody List<TelephoneNumber> telephoneNumberList) {
		Result<String> result;
		try {
			this.tenantManagerService.batchDeleteTenantTelephoneNumber(telephoneNumberList);
			result = new ResultUtil<String>().setData("删除号码成功");
		} catch (TenantManagerException e) {
			result = new ResultUtil<String>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("删除号码信息异常", e);
			result = new ResultUtil<String>().setErrorMsg("删除号码信息异常，请联系管理员");
		}
		return result;
	}

	/**
	 * 删除号码信息
	 * @param telephoneNumber    号码信息
	 * @return 角色信息
	 */
	@RequestMapping(value = "/deleteTenantTelephoneNumber", method = RequestMethod.POST)
	public Object deleteTenantTelephoneNumber(@RequestBody TelephoneNumber telephoneNumber) {
		Result<String> result;
		try {
			this.tenantManagerService.deleteTenantTelephoneNumber(telephoneNumber);
			result = new ResultUtil<String>().setData("删除成功");
		} catch (TenantManagerException e) {
			result = new ResultUtil<String>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("删除号码信息异常", e);
			result = new ResultUtil<String>().setErrorMsg("删除号码信息异常，请联系管理员");
		}
		return result;
	}
}

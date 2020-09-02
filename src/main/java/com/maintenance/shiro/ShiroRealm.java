package com.maintenance.shiro;

import com.maintenance.base.dao.UserManagerDao;
import com.maintenance.base.entity.Resource;
import com.maintenance.base.entity.Role;
import com.maintenance.base.entity.User;
import com.maintenance.base.service.ResourceService;
import com.maintenance.base.service.RoleService;
import com.maintenance.context.UserInfo;
import com.maintenance.pojo.SearchParams;
import com.maintenance.utils.HttpRequestUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiroRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

	@Autowired
	private UserManagerDao userManagerDao;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RoleService roleService;

	/**
	 * 获取用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		User user= (User) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid",user.getId());
		/*
		List<Resource> resources = resourceService.getUserResources(user.getId());
		List<Role> roles=roleService.getUserRoles(user.getId());
		// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for(Resource resource: resources){
			info.addStringPermission(resource.getUrl());
		}
		for(Role role: roles){
			info.addRole(role.getName());
		}
		return info;
		*/
		return new SimpleAuthorizationInfo();

	}

	/**
	 * 登录认证
	 * @return
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		HttpSession httpSession = HttpRequestUtil.getSession();
		if(httpSession!=null) {
			String userName = (String) token.getPrincipal();
			String password = new String((char[]) token.getCredentials());
			System.out.println("用户" + userName + "认证");
			// 查询用户信息
			User user = userManagerDao.getUserByAccount(userName);
			if (user == null) {
				throw new UnknownAccountException("用户名或密码错误！");
			}
			if (!password.equals(user.getPassword())) {
				throw new IncorrectCredentialsException("用户名或密码错误！");
			}
			/* 查询用户角色信息 */
			SearchParams searchParams = new SearchParams();
			searchParams.addCondition("userId", user.getId());
			List<Role> roles = roleService.queryList(searchParams);
			/* 查询角色资源权限 */
			searchParams.addCondition("roleList", roles);
			List<Resource> resources = resourceService.queryList(searchParams);
			// 设置用户信息
			UserInfo.setUserInfoCache(httpSession.getId(), user, roles, resources);

			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
			return info;
		} else {
			logger.error("shiro登录认证失败，会话为空");
			throw new AuthenticationException("登录失败，会话为空");
		}
	}

}

package com.maintenance.base.controller;

import com.maintenance.context.UserInfo;
import com.maintenance.excepion.UserInfoException;
import com.maintenance.pojo.Result;
import com.maintenance.utils.Encrypt;
import com.maintenance.utils.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {

	/**
	 * 登录
	 * @param loginAccount
	 * @param password
	 * @return
	 */
    @PostMapping("/login")
	public Object login(@RequestParam("loginAccount") String loginAccount, @RequestParam("password") String password) {
		Result<String> result;
		try {
			password = Encrypt.md5Encrypt(password);
			UsernamePasswordToken token = new UsernamePasswordToken(loginAccount, password);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			result = new ResultUtil<String>().setData("登录成功");
		} catch (UnknownAccountException e) {
			result = new ResultUtil<String>().setErrorMsg("用户不存在");
		} catch (IncorrectCredentialsException e) {
			result = new ResultUtil<String>().setErrorMsg("密码错误");
		} catch (LockedAccountException e) {
			result = new ResultUtil<String>().setErrorMsg("用户被禁用，请联系管理员");
		} catch (AuthenticationException e) {
			result = new ResultUtil<String>().setErrorMsg("认证失败");
		} catch (Exception e) {
			result = new ResultUtil<String>().setErrorMsg("登录失败，请联系管理员");
		}
		return result;
	}

	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public Object getUserInfo() {
		Result<UserInfo> result;
		try {
			result = new ResultUtil<UserInfo>().setData(UserInfo.getInstance());
		} catch (UserInfoException e) {
			result = new ResultUtil<UserInfo>().setErrorMsg(e.getMessage());
		} catch (Exception e) {
			result = new ResultUtil<UserInfo>().setErrorMsg("获取当前登录用户信息异常，请联系管理员");
		}
		return result;
	}

    /**
     * 未登录或登录失败后跳转到登录页
     */
    @Value("${shiro.loginUrl}")
    private String shiroLoginUrl;
    @GetMapping("/login")
	public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		resp.sendRedirect(shiroLoginUrl);
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
	}
}

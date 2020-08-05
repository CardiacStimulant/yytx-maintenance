package com.yytx.maintenance.utils;

import com.yytx.maintenance.base.service.UserManagerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils {
    private Logger logger = LoggerFactory.getLogger(UserManagerService.class);

    /**
     * 正则验证电子邮件
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        email = email.toLowerCase();
        String str = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 正则验证固定电话
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^((\\d{3,4})|\\d{3,4}-)?\\d{7,8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 验证是不是电话号码
     *
     * @return
     */
    public static boolean isMobileOrPhone(String phone) {
        if (isMobilePhone(phone) || isPhone(phone)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为整数
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
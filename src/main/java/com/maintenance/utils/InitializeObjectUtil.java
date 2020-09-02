package com.maintenance.utils;

import com.maintenance.context.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 通过反射机制，初始化对象信息
 * 例如：初始化对象的创建信息，修改信息等
 * 单例
 * @author liangxchc
 * @date 2019-07-29
 */
public class InitializeObjectUtil {
    private Logger logger = LoggerFactory.getLogger(InitializeObjectUtil.class);

    private final boolean SUCCESS = true;
    private final boolean FAILD = false;

    // 单例对象
    private static final InitializeObjectUtil instance = new InitializeObjectUtil();

    /**
     * 私有化构造函数
     */
    private InitializeObjectUtil(){}

    /**
     * 获取对象
     * @return  返回对象
     */
    public static InitializeObjectUtil getInstance(){
        return instance;
    }

    /**
     * 反射机制
     * 初始化对象创建信息和修改信息
     * @param object    待赋值对象
     * @param userInfoInstance  客户实例化后的对象
     * @return  是否初始化成功
     */
    public boolean initializeCreateAndModifyInfo(Object object, UserInfo userInfoInstance) {
        boolean isValid = this.checkArgument(object, userInfoInstance);
        if(!isValid) {
            return FAILD;
        }
        try {
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                try {
                    if(method.getName().equals("setCreateTime") || method.getName().equals("setLastModified")) {
                        method.invoke(object, DateUtil.toDateString(new Date()));
                    } else if (method.getName().equals("setCreateUser") || method.getName().equals("setLastModifyUser")){
                        method.invoke(object, userInfoInstance.getUser().getName());
                    }
                } catch (Exception e) {
                    logger.error("设置对象信息异常，调用方法（" + method.getName() + "）异常", e);
                }
            }
        } catch (Exception e) {
            logger.error("初始化对象信息异常", e);
        }
        return SUCCESS;
    }

    /**
     * 反射机制
     * 初始化对象修改信息
     * @param object    待赋值对象
     * @param userInfoInstance  客户实例化后的对象
     * @return  是否初始化成功
     */
    public boolean initializeModifyInfo(Object object, UserInfo userInfoInstance) {
        boolean isValid = this.checkArgument(object, userInfoInstance);
        if(!isValid) {
            return FAILD;
        }
        try {
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                try {
                    if(method.getName().equals("setLastModified")) {
                        method.invoke(object, DateUtil.toDateString(new Date()));
                    } else if (method.getName().equals("setLastModifyUser")){
                        method.invoke(object, userInfoInstance.getUser().getName());
                    }
                } catch (Exception e) {
                    logger.error("设置对象信息异常，调用方法（" + method.getName() + "）异常", e);
                }
            }
        } catch (Exception e) {
            logger.error("初始化对象信息异常", e);
        }
        return SUCCESS;
    }

    /**
     * 反射机制
     * 初始化对象创建信息
     * @param object    待赋值对象
     * @param userInfoInstance  客户实例化后的对象
     * @return  是否初始化成功
     */
    public boolean initializeCreateInfo(Object object, UserInfo userInfoInstance) {
        boolean isValid = this.checkArgument(object, userInfoInstance);
        if(!isValid) {
            return FAILD;
        }
        try {
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                try {
                    if(method.getName().equals("setCreateTime")) {
                        method.invoke(object, DateUtil.toDateString(new Date()));
                    } else if (method.getName().equals("setCreateUser")){
                        method.invoke(object, userInfoInstance.getUser().getName());
                    }
                } catch (Exception e) {
                    logger.error("设置对象信息异常，调用方法（" + method.getName() + "）异常", e);
                }
            }
        } catch (Exception e) {
            logger.error("初始化对象信息异常", e);
        }
        return SUCCESS;
    }

    /**
     * 校验传入参数
     * @param object    待赋值对象
     * @param userInfoInstance  客户实例化后的对象
     * @return 是否校验成功
     */
    private boolean checkArgument(Object object, UserInfo userInfoInstance) {
        if(object == null) {
            logger.error("传入初始化对象为空");
            return FAILD;
        }
        if(userInfoInstance == null) {
            logger.error("传入客户对象为空");
            return FAILD;
        }
        return SUCCESS;
    }
}

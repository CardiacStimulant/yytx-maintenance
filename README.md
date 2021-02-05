# maintenance
一个简单的spring boot项目。
这是一个运维管理的项目，其中主要包含了用户，角色，资源权限的管理。
可以扩展其他的需要运维管理的功能，比如说：号码管理，企业管理等等

# [资源目录](https://github.com/CardiacStimulant/maintenance/tree/master/%E8%B5%84%E6%BA%90)
包含了创建数据库的脚本，settings.xml文件

# [com.maintenance.context.WebAppConfig](https://github.com/CardiacStimulant/maintenance/blob/master/src/main/java/com/maintenance/context/WebAppConfig.java)
[CORSInterceptor](https://github.com/CardiacStimulant/maintenance/blob/master/src/main/java/com/maintenance/context/interceptor/CORSInterceptor.java)：跨域拦截器

[ResponseResultInterceptor](https://github.com/CardiacStimulant/maintenance/blob/master/src/main/java/com/maintenance/context/response/ResponseResultInterceptor.java)：controller返回包装拦截器

[XssFilter](https://github.com/CardiacStimulant/maintenance/blob/master/src/main/java/com/maintenance/context/xss/XssFilter.java)：防止XSS攻击的filter

# [com.maintenance.context.response.ResponseResultHandler](https://github.com/CardiacStimulant/maintenance/blob/master/src/main/java/com/maintenance/context/response/ResponseResultHandler.java)
controller增强器，统一包装返回体和异常

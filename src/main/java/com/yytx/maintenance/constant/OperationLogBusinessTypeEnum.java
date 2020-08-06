package com.yytx.maintenance.constant;

/**
 * 业务类型枚举类
 */
public enum OperationLogBusinessTypeEnum {
    USER("user", "用户"),
    ROLE("role", "角色"),
    RESOURCE("resource", "资源"),
    USER_ROLE_RELATION("userRoleRelation", "用户角色关系");

    private String key;

    private String value;

    private OperationLogBusinessTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name();
    }

    public String getValue() {
        return this.value;
    }

}


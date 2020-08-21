package com.yytx.maintenance.maintenance.entity;

import com.yytx.maintenance.pojo.BaseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Tenant extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7670860126448900842L;
    private Long id;
    private String iuapTenantId;
    private String iuapTenantName;
    private String status;

    public Tenant(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIuapTenantId() {
        return iuapTenantId;
    }

    public void setIuapTenantId(String iuapTenantId) {
        this.iuapTenantId = iuapTenantId;
    }

    public String getIuapTenantName() {
        return iuapTenantName;
    }

    public void setIuapTenantName(String iuapTenantName) {
        this.iuapTenantName = iuapTenantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", iuapTenantId='" + iuapTenantId + '\'' +
                ", iuapTenantName='" + iuapTenantName + '\'' +
                ", status='" + status + '\'' +
                ", dr=" + dr +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}

package com.yytx.maintenance.maintenance.entity;

import com.yytx.maintenance.pojo.BaseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class TelephoneNumber extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -155905836896068739L;
    private Long id;
    private String tenantId;
    private String telephoneNum;
    private String name;
    private String type;
    private String prefix;
    private String infix;
    private String areaCode;
    private String registrationNumber;
    private String concurrencyNumber;
    private String line;
    private String comments;

    public TelephoneNumber(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getConcurrencyNumber() {
        return concurrencyNumber;
    }

    public void setConcurrencyNumber(String concurrencyNumber) {
        this.concurrencyNumber = concurrencyNumber;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "TelephoneNumber{" +
                "id=" + id +
                ", tenantId='" + tenantId + '\'' +
                ", telephoneNum='" + telephoneNum + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", prefix='" + prefix + '\'' +
                ", infix='" + infix + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", concurrencyNumber='" + concurrencyNumber + '\'' +
                ", line='" + line + '\'' +
                ", comments='" + comments + '\'' +
                ", dr=" + dr +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}

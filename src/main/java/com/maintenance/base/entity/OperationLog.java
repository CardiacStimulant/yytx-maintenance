package com.maintenance.base.entity;

import com.maintenance.pojo.BaseEntity;
import com.maintenance.context.UserInfo;
import com.maintenance.utils.InitializeObjectUtil;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class OperationLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7344534995410472230L;
    private Long id;
    private Long businessId;
    private String businessType;
    private String operationType;
    private String comments;

    public OperationLog(){}

    /**
     * 初始化实体
     * @param businessId    业务ID
     * @param businessType  业务类型
     * @param operationType 操作类型
     * @param comments  备注
     */
    public OperationLog(Long businessId, String businessType, String operationType, String comments) {
        this.setBusinessId(businessId);
        this.setBusinessType(businessType);
        this.setOperationType(operationType);
        this.setComments(comments);
    }

    /**
     * 初始化实体
     * @param businessId    业务ID
     * @param businessType  业务类型
     * @param operationType 操作类型
     * @param comments  备注
     * @param userInfo 用户信息
     */
    public OperationLog(Long businessId, String businessType, String operationType, String comments, UserInfo userInfo) {
        this.setBusinessId(businessId);
        this.setBusinessType(businessType);
        this.setOperationType(operationType);
        this.setComments(comments);
        InitializeObjectUtil.getInstance().initializeCreateAndModifyInfo(this, userInfo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    @Override
    public String toString() {
        return "OperationLog{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", businessType='" + businessType + '\'' +
                ", operationType='" + operationType + '\'' +
                ", comments='" + comments + '\'' +
                ", dr=" + dr +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}

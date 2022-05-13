package com.jsn.cropclassification.view;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ParentMetaData implements Serializable {

    @SerializedName("MetaData")
    public MetaData metaData;

    @SerializedName("Primary")
    public PrimaryData primaryData;

    public ParentMetaData(MetaData metaData, PrimaryData primaryData){
        this.metaData = metaData;
        this.primaryData = primaryData;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public PrimaryData getPrimaryData() {
        return primaryData;
    }

    public void setPrimaryData(PrimaryData primaryData) {
        this.primaryData = primaryData;
    }


    public class MetaData implements Serializable{

        @SerializedName("ActivityId")
        public String activityId;
        @SerializedName("LanguageID")
        public String languageId;
        @SerializedName("ConnString")
        public String connStr;
        @SerializedName("Site-ID")
        public String siteId;
        @SerializedName("Emp_ID")
        public String empId;
        @SerializedName("Emp_Name")
        public String empName;
        @SerializedName("User-ID")
        public String userId;
        @SerializedName("User_Name")
        public String userName;
        @SerializedName("ObjectIDName")
        public String objectName;
        @SerializedName("ObjectIDValue")
        public String objectId;
        @SerializedName("ParentId")
        public String parentId;
        @SerializedName("TenantId")
        public String tenantId;
        @SerializedName("SiteCode")
        public String siteCode;

        public  MetaData(String activityId,String languageId,String connStr,String siteId,String empId,String empName,
                             String userId,String userName,String objectName,String objectId,String parentId,
                         String tenantId,String siteCode){
            this.activityId = activityId;
            this.languageId = languageId;
            this.connStr = connStr;
            this.siteId = siteId;
            this.empId = empId;
            this.empName = empName;
            this.userId = userId;
            this.userName = userName;
            this.objectName = objectName;
            this.objectId = objectId;
            this.parentId = parentId;
            this.siteCode = siteCode;
            this.tenantId = tenantId;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getLanguageId() {
            return languageId;
        }

        public void setLanguageId(String languageId) {
            this.languageId = languageId;
        }

        public String getConnStr() {
            return connStr;
        }

        public void setConnStr(String connStr) {
            this.connStr = connStr;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getParentId() {
            return parentId;
        }

        public String getTenantId() {
            return tenantId;
        }

        public String getSiteCode() {
            return siteCode;
        }

    }

    public class PrimaryData implements Serializable{

        @SerializedName("Params")
        public List<PrimaryParams> primaryParams;

        public PrimaryData(List<PrimaryParams> primaryParams){
            this.primaryParams = primaryParams;
        }
        public List<PrimaryParams> getPrimaryParams() {
            return primaryParams;
        }

    }

    public class PrimaryParams implements Serializable{

        @SerializedName("Name")
        public String name;
        @SerializedName("Value")
        public String value;

        public PrimaryParams (String name,String value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}

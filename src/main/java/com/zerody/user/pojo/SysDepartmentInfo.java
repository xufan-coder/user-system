package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysDepartmentInfo extends BaseModel {


    //企业id
    private String compId;

    //部门名称
    private String departName;

    //父级部门id
    private String parentId;

    private String telephone;

    private Date creatTime;

    private String departCode;

    private String departDesc;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId == null ? null : compId.trim();
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName == null ? null : departName.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode == null ? null : departCode.trim();
    }

    public String getDepartDesc() {
        return departDesc;
    }

    public void setDepartDesc(String departDesc) {
        this.departDesc = departDesc == null ? null : departDesc.trim();
    }
}
package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysJobPosition  extends BaseModel {

    //企业id
    private String compId;

    //岗位名称
    private String positionName;

    //职责范围
    private String jobScope;

    //父级岗位id
    private String parentId;

    //岗位级别
    private Integer level;

    //部门id
    private String departId;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId == null ? null : compId.trim();
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName == null ? null : positionName.trim();
    }

    public String getJobScope() {
        return jobScope;
    }

    public void setJobScope(String jobScope) {
        this.jobScope = jobScope == null ? null : jobScope.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId == null ? null : departId.trim();
    }
}
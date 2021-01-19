package com.zerody.user.domain;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:58
 * @param
 * @return
 */
public class UnionStaffDepart {
    /** id **/
    private String id;

    /** 员工id **/
    private String staffId;

    /** 部门id **/
    private String departmentId;

    public UnionStaffDepart(){
        super();
    }
    public UnionStaffDepart(String staffId,String departmentId){
        this.departmentId=departmentId;
        this.staffId=staffId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId == null ? null : departmentId.trim();
    }
}
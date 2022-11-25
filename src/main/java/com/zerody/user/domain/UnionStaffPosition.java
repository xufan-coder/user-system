package com.zerody.user.domain;

/**
 *
 * 员工岗位关联表
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:59
 * @param
 * @return
 */
public class UnionStaffPosition {
    /** id **/
    private String id;

    /** 员工id **/
    private String staffId;

    /** 岗位id **/
    private String positionId;


    public UnionStaffPosition(){
        super();
    }
    public UnionStaffPosition(String staffId,String positionId){
        this.positionId=positionId;
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

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId == null ? null : positionId.trim();
    }
}
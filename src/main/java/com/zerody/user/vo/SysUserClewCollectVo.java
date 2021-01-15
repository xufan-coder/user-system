package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysUserClewCollectVo
 * @DateTime 2021/1/9_11:48
 * @Deacription TODO
 */
public class SysUserClewCollectVo {

    /**
     * 线索负责人id
     */
    private String userId;
    /**
     * 线索负责人
     */
    private String name;

    /**
     *  所属部门
     */
    private String departName;

    /**
     *  所属岗位
     */
    private String jobName;


    /**
     *线索总条数
     */
    private Integer totalClew;

    /**
     * 已呼叫
     */
    private Integer haveCalled;

    /**
     *  今日呼叫
     */
    private Integer todayCalled;

    /**
     * 最后呼叫时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date lastCallTime;

    /**
     * 最后入库时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date lastStorageTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date loginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getTotalClew() {
        return totalClew == null ? 0 : totalClew;
    }

    public void setTotalClew(Integer totalClew) {
        this.totalClew = totalClew;
    }

    public Integer getHaveCalled() {
        return haveCalled == null ? 0 : haveCalled;
    }

    public void setHaveCalled(Integer haveCalled) {
        this.haveCalled = haveCalled;
    }

    public Integer getTodayCalled() {
        return todayCalled == null ? 0 : todayCalled;
    }

    public void setTodayCalled(Integer todayCalled) {
        this.todayCalled = todayCalled;
    }

    public Date getLastCallTime() {
        return lastCallTime;
    }

    public void setLastCallTime(Date lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    public Date getLastStorageTime() {
        return lastStorageTime;
    }

    public void setLastStorageTime(Date lastStorageTime) {
        this.lastStorageTime = lastStorageTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}

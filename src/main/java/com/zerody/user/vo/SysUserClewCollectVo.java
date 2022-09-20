package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.utils.DataUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysUserClewCollectVo
 * @DateTime 2021/1/9_11:48
 * @Deacription TODO
 */
@Data
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

    /**今日呼叫时长 s转"HH:mm:ss**/
    private String todayTime;

    /**总呼叫时长 s转"HH:mm:ss**/
    private String totalTime;

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

    private Boolean companyAdmin;

    private Boolean departAdmin;

    public Boolean getCompanyAdmin() {

        return companyAdmin != null ? companyAdmin : false;
    }

    public Boolean getDepartAdmin() {
        return departAdmin != null ? departAdmin : false;
    }


    public Integer getTotalClew() {
        return DataUtil.isEmpty(totalClew) ? 0 : totalClew;
    }

    public Integer getHaveCalled() {
        return DataUtil.isEmpty(haveCalled) ? 0 : haveCalled;
    }

    public Integer getTodayCalled() {
        return DataUtil.isEmpty(todayCalled) ? 0 : todayCalled;
    }

    public String getTodayTime() { return  DataUtil.isEmpty(todayTime) ? "00:00:00" : todayTime; }

    public String getTotalTime() { return DataUtil.isEmpty(totalTime) ? "00:00:00" : totalTime; }

}

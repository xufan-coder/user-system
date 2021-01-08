package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import java.util.Date;

@Data
public class SysStaffInfo extends BaseModel {

    //企业id
    private String compId;

    //用户id
    private String userId;

    //用户姓名
    private String userName;

    //工作地点
    private String workPlace;

    //工号
    private String jobNumber;

    //转正时间
    private Date dateJoin;

    //入职时间
    private Date conversionDate;

    //离职时间
    private Date dateLeft;

}
package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import java.util.Date;

@Data
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

}
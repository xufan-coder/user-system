package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class SysDepartmentInfo extends BaseModel {


    //企业id
    private String compId;

    //部门名称
    @NotEmpty(message = "部门名称不能为空")
    private String departName;

    //父级部门id
    private String parentId;

    //成立时间
    private Date creatTime;

    //联系电话
    private String telephone;

    //部门编码 vc
    private String departCode;

    //部门描述
    private String departDesc;

    //部门登录状态
    private Integer loginStatus;


}
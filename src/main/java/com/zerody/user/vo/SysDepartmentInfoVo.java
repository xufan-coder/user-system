package com.zerody.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoVo
 * @DateTime 2020/12/19_13:29
 * @Deacription TODO
 */
@Data
public class SysDepartmentInfoVo {

    private String id;

    //部门名称
    private String departName;

    //父级部门id
    private String parentId;

    //成立时间
    private Date creatTime;

//    //联系电话
    private String telephone;

    //部门编码
    private String departCode;

    //部门描述
    private String departDesc;

    //部门登录状态
    private Integer loginStatus;

    private List<SysDepartmentInfoVo> departChildrens;

    private List<SysJobPositionVo> jobChildrens;

    private Integer staffCount;
}
